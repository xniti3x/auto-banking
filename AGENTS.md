# Arbeitsregeln für KI-Agenten und Mitwirkende

## Projektüberblick

Dies ist eine Java-17-/Spring-Boot-REST-Anwendung, die die GoCardless Bank Account Data API kapselt. Die lokale API wird durch Springdoc dokumentiert; die Upstream-API wird aus `src/main/resources/api.yaml` als Java-Client nach `src/generated/` generiert.

## Wichtige Grenzen

- `src/generated/` ist Build-Ausgabe. Nie manuell ändern oder einchecken; stattdessen `api.yaml` anpassen und den Client neu generieren.
- Behandle `secret_id`, `secret_key`, Access- und Refresh-Tokens sowie Datenbank-Passwörter immer als Geheimnisse. Keine realen Werte in Code, Tests, Dokumentation, Commits oder Logs aufnehmen.
- Öffentliche Controller sind nur `/auth/**` sowie die Swagger-Routen. Änderungen an Sicherheit, Token-Lebenszyklus oder Berechtigungen besonders sorgfältig testen.
- Die gemeinsame `ApiClient`-Bean hält das Bearer-Token pro Prozess. Das ist derzeit keine mehrbenutzerfähige Token-Isolation; keine Änderungen vornehmen, die diese Einschränkung stillschweigend verschleiern.
- Änderungen an GoCardless-Verhalten gegen die offizielle Dokumentation prüfen. Die lokale API darf eigene Pfade haben und soll als Wrapper klar erkennbar bleiben.

## Entwicklungsablauf

1. Java 17 verwenden.
2. Nach Änderungen an `api.yaml` oder an typisierten GoCardless-Modellen `mvn clean verify` ausführen. Die Client-Generierung läuft automatisch in `generate-sources`.
3. Für normale Codeänderungen mindestens `mvn test` ausführen.
4. Controller, Service und Tests gemeinsam anpassen. Die Tests verwenden das Profil `test` mit H2 und dürfen keine echten GoCardless-Anfragen ausführen.
5. README aktualisieren, wenn sich öffentliche Routen, Konfiguration oder der Ablauf der Integration ändern.

## Codekonventionen

- Neue fachliche Logik nach bestehender Paketstruktur in Controller, Service, Repository, Entity/DTO/Mapper einordnen.
- Fehler aus dem generierten GoCardless-Client als `ApiException` an den zentralen Exception-Handler weiterreichen, sofern keine bewusste Übersetzung erforderlich ist.
- Keine generierten Klassen als stabile Handarbeit-Abhängigkeit behandeln: Feldnamen und Signaturen können sich nach einer Spezifikationsaktualisierung ändern.
- Datenbank-Migrationen und persistierte Geheimnisse vor Produktionsänderungen ausdrücklich berücksichtigen; die Entwicklungsdatenbank ist kein Produktionskonzept.
