# gocardless-bank-data-bridge

`gocardless-bank-data-bridge` ist ein Spring-Boot-REST-Wrapper für die [GoCardless Bank Account Data API](https://docs.gocardless.com/docs/bank-account-data/endpoints). Er stellt ausgewählte Funktionen der Upstream-API über eine eigene, mit einem lokalen JWT geschützte HTTP-Schnittstelle bereit und kann importierte Transaktionen lokal persistieren.

Die frühere Spring-Shell ist nicht mehr Teil der aktiven Anwendung. Der Einstiegspunkt ist jetzt die REST-API mit Swagger UI.

## Funktionsumfang

- Lokale Benutzerregistrierung und Anmeldung mit JWT
- GoCardless-Token erzeugen und auffrischen
- Institute abfragen
- End-User-Agreements (EUAs) und Requisitions verwalten
- Kontometadaten, Salden, Details und Transaktionen abrufen
- Transaktionen aus GoCardless-Antworten oder JSON-Dateien persistieren
- Optionaler, datenbankgestützter Automationsschalter

Der generierte Java-Client verwendet die GoCardless-Endpunkte für Token, Institute, EUAs, Requisitions und Konten. Die lokale API hat bewusst andere Pfade als die Upstream-API.

## Architektur

```text
HTTP-Client
    │  lokales Bearer-JWT
    ▼
Spring-Boot-REST-API ──► generierter OpenAPI-Client ──► GoCardless Bank Account Data
    │
    └──► MySQL: Benutzer, lokale Konfigurationsdaten, Tokens, Transaktionen
```

`src/main/resources/api.yaml` ist die API-Beschreibung der GoCardless-Upstream-API. Maven generiert daraus beim Build den Client nach `src/generated/`; dieser Ordner wird nicht versioniert und darf nicht manuell bearbeitet werden.

## Voraussetzungen

- Java 17
- Maven 3.9 oder neuer
- MySQL 8 (oder die mit Docker Compose gestartete Datenbank)
- Ein GoCardless Bank Account Data Konto mit `secret_id` und `secret_key`

Für Entwicklung und Tests kann das GoCardless-Sandboxkonto verwendet werden. Informationen zu Berechtigungen, Requisitions und Institutsgrenzen stehen in der [offiziellen GoCardless-Dokumentation](https://docs.gocardless.com/docs/bank-account-data/endpoints).

## Lokal starten

1. Datenbank konfigurieren. Die Standardwerte verbinden sich mit `jdbc:mysql://localhost:3306/bank` als `root`/`root`. Für abweichende Werte die folgenden Umgebungsvariablen setzen:

   ```text
   SPRING_DATASOURCE_URL
   SPRING_DATASOURCE_USERNAME
   SPRING_DATASOURCE_PASSWORD
   SPRING_JPA_HIBERNATE_DDL_AUTO
   SPRING_JPA_SHOW_SQL
   ```

   Eine Vorlage zum Übernehmen der Werte als Umgebungsvariablen liegt in `.env.example`. Sie enthält keine GoCardless-Zugangsdaten.

2. Anwendung bauen und starten:

   ```bash
   mvn clean verify
   mvn spring-boot:run
   ```

   Die OpenAPI-Codegenerierung ist an die Maven-Phase `generate-sources` gebunden. Ein separater Aufruf von `openapi-generator:generate` ist nicht erforderlich.

3. API-Dokumentation öffnen:

   ```text
   http://localhost:8080/swagger-ui/index.html
   ```

   Die maschinenlesbare Beschreibung der lokalen API ist unter `http://localhost:8080/v3/api-docs` verfügbar.

## Start mit Docker Compose

```bash
mvn clean package
docker compose up --build
```

Docker Compose startet MySQL, phpMyAdmin und die Anwendung. Die API ist anschließend über Port `8080`, phpMyAdmin über [http://localhost:8081](http://localhost:8081) erreichbar. Die Datenbank verwendet ein benanntes Volume und bleibt damit über Container-Neustarts hinweg erhalten.

> Die in `docker-compose.yml` hinterlegten Datenbank-Zugangsdaten sind ausschließlich für die lokale Entwicklung vorgesehen. Vor einer Weitergabe oder Bereitstellung müssen sie ersetzt werden.

## Authentifizierung und erster API-Aufruf

`/auth/**` und Swagger sind öffentlich. Alle übrigen lokalen Endpunkte erwarten ein von dieser Anwendung ausgestelltes JWT:

```http
Authorization: Bearer <lokales-jwt>
```

Beispielablauf mit `curl`:

```bash
# 1. Lokalen Benutzer anlegen
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"me@example.test","password":"change-this-password"}'

# 2. Lokales JWT anfordern
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"me@example.test","password":"change-this-password"}'

# 3. GoCardless-Token erzeugen; <APP_JWT> durch die Antwort aus Schritt 2 ersetzen
curl -X POST http://localhost:8080/token/new \
  -H "Authorization: Bearer <APP_JWT>" \
  -H "Content-Type: application/json" \
  -d '{"secret_id":"<GOCARDLESS_SECRET_ID>","secret_key":"<GOCARDLESS_SECRET_KEY>"}'

# 4. Institute abfragen
curl "http://localhost:8080/institutions?country=DE" \
  -H "Authorization: Bearer <APP_JWT>"
```

Die Antwort aus `/token/new` enthält hochsensible GoCardless-Tokens. Sie darf nicht geloggt, eingecheckt oder an Clients weitergegeben werden, die sie nicht benötigen.

## Lokale API

Die Swagger UI ist die vollständige, laufzeitnahe Referenz. Die wichtigsten Routen sind:

| Bereich | Methode und Pfad | Zweck |
| --- | --- | --- |
| Auth | `POST /auth/register`, `POST /auth/login` | Lokalen Benutzer anlegen bzw. JWT ausstellen |
| Token | `POST /token/new`, `POST /token/refresh` | GoCardless-Access-/Refresh-Token verwalten |
| Institute | `GET /institutions`, `GET /institutions/{id}` | Verfügbare Banken und deren Fähigkeiten abfragen |
| EUA | `GET/POST /agreements/enduser`, `GET/DELETE /agreements/enduser/{id}` | End-User-Agreements verwalten |
| Requisition | `GET/POST /requisitions`, `GET/DELETE /requisitions/{id}` | Bankautorisierungen anlegen und prüfen |
| Konten | `GET /bank/accounts/{id}`, `/balances`, `/details`, `/transactions` | Daten eines durch eine Requisition erhaltenen Kontos abrufen |
| Nutzer | `GET/PUT /users` | Gespeicherte, nicht geheime GoCardless-Metadaten des angemeldeten Benutzers lesen bzw. ändern |
| Import | `POST /import/json` | Eine GoCardless-`AccountTransactions`-JSON als `multipart/form-data` mit Feld `file` importieren |
| Automation | `POST /automation/start`, `POST /automation/stop` | Den Automationsschalter des angemeldeten Benutzers setzen |

Die Listenendpunkte für Requisitions und EUAs erwarten die Query-Parameter `limit` und `offset`. `/institutions` akzeptiert unter anderem `country` sowie die in `InstitutionFilterDto` definierten Feature-Filter.

## GoCardless-Ablauf

1. Mit `POST /token/new` wird aus `secret_id` und `secret_key` ein GoCardless-JWT-Paar erzeugt.
2. `GET /institutions?country=DE` liefert die mögliche Bankauswahl.
3. Optional wird ein EUA angelegt, das Umfang und Gültigkeit des Kontozugriffs festlegt.
4. Eine Requisition wird mit `institution_id` und einer Redirect-URL erzeugt. Der Endnutzer öffnet den zurückgegebenen `link` und autorisiert den Zugriff bei seiner Bank.
5. Nach erfolgreicher Autorisierung enthält die Requisition die Konto-IDs. Diese werden mit den `/bank/accounts/{id}/…`-Routen verwendet.

Die verfügbaren Daten, maximalen Abrufzeiträume, Consent-Laufzeiten und Statuswerte hängen vom Institut und von GoCardless ab. Der Wrapper leitet Upstream-Fehler als strukturierte Fehlerantwort weiter.

## Datenhaltung und Sicherheit

- Lokale JWTs sind eine Stunde gültig und werden beim Neustart der Anwendung ungültig, weil ihr Signaturschlüssel zur Laufzeit erzeugt wird.
- GoCardless-Zugangsdaten und -Tokens sind Geheimnisse. Sie gehören nicht in `application.properties`, `.env.example`, Testdaten, Logs oder Git.
- Für einen produktiven Mehrbenutzerbetrieb muss die gemeinsame `ApiClient`-Instanz durch eine benutzer- bzw. anfragegebundene Token-Verwaltung ersetzt werden. Die aktuelle Implementierung eignet sich für lokale bzw. kontrollierte Einzelbenutzer-Szenarien.
- Die gespeicherten GoCardless-Tokens werden derzeit nicht verschlüsselt abgelegt. Vor einem produktiven Einsatz ist Verschlüsselung auf Anwendungsebene oder ein Secret-Store erforderlich.

## Entwicklung

```bash
# Vollständiger Build einschließlich Client-Generierung und Tests
mvn clean verify

# Nur Tests
mvn test
```

Die Tests verwenden H2 im Speicher und rufen keine echte Bank-API auf. Weitere Arbeitsregeln für Menschen und KI-Agenten stehen in [AGENTS.md](AGENTS.md).

## Projektstruktur

```text
src/main/java/com/example/autobanking/
├── accounts, institutions, agreements, requisitions, tokens  # GoCardless-Wrapper
├── auths, users, configs                                      # lokale API und Sicherheit
├── transactions, imports                                      # Persistenz und JSON-Import
└── automations                                                # optionaler Abrufprozess
src/main/resources/api.yaml                                    # GoCardless-OpenAPI-Spezifikation
src/generated/                                                 # generiert, nicht versioniert
```
