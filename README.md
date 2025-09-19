# GoCardless Banking Shell

A Spring Boot application with an **interactive shell interface** for managing bank integrations through [GoCardless Bank Account Data](https://developer.gocardless.com/bank-account-data).
This service retrieves transactions from GoCardless and persists them in a **MySQL database**.

---

## Prerequisites

1. **GoCardless Developer Account**

   * Follow the [Quick Start Guide](https://developer.gocardless.com/bank-account-data/quick-start-guide)
   * Create an app and generate your API credentials
   * Get a **requisitionId** from the [Bank Account Data API endpoints](https://developer.gocardless.com/bank-account-data/endpoints)

2. **MySQL Database**

   * Create a schema (e.g. `banking_db`)
   * Update your `application.yml` or `application.properties` with DB credentials

3. **Java 17+**

   * Required to run Spring Boot

---

## Getting Started

### Clone the repository

```bash
cd auto-banking
```

### Build the project

Using Maven:

```bash
mvn openapi-generator:generate
mvn clean package -DskipTests
```

### Run the application

```bash
mvn spring-boot:run
or 
java -jar target/auto-banking-0.0.1-SNAPSHOT.jar
```

### Run with Docker

The project includes a `docker-compose.yml` file. It sets up:

* **db**: MySQL database (port `3306`)
* **phpmyadmin**: Web UI for managing MySQL (port `8081`)
* **app**: Spring Boot banking shell application

Start everything with:

```bash
mvn openapi-generator:generate
mvn clean package -DskipTests
docker-compose up --build -d
docker attach <container-id or container name>
```

Once running:

* The **Spring Boot app** will start and show the interactive shell menu in your terminal.
* MySQL is available at `localhost:3306` (user: `root`, password: `root`).
* phpMyAdmin is available at [http://localhost:8081](http://localhost:8081).

---

## Usage

When the application starts, you’ll see a **welcome banner**:

```
██████╗  █████╗ ███╗   ██╗██╗  ██╗██╗███╗   ██╗ ██████╗ 
██╔══██╗██╔══██╗████╗  ██║██║ ██╔╝██║████╗  ██║██╔════╝ 
██████╔╝███████║██╔██╗ ██║█████╔╝ ██║██╔██╗ ██║██║  ███╗
██╔══██╗██╔══██║██║╚██╗██║██╔═██╗ ██║██║╚██╗██║██║   ██║
██████╔╝██║  ██║██║ ╚████║██║  ██╗██║██║ ╚████║╚██████╔╝
╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝ 

🏦 auto - banking - GoCardless Integration

Welcome to the Banking Shell! 
Type 'menu' or 'm' to see available commands.
Type 'help' to see all shell commands.
```

### Menu Commands

Type `menu` or `m` to see the available commands:

```
🏦 auto - banking 
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Available Commands:
   
1️⃣  automation-enable     (ae)  - turn ON automation
2️⃣  automation-disable    (ad)  - turn OFF automation
3️⃣  setup                       - setup credentials
4️⃣  status                (s)   - Show current status
5️⃣  metadata              (meta)- Show metadata placeholder
6️⃣  exit                  (q)   - Exit the application
```

---

## Example Setup Flow

1. Run the application
2. Type `setup`
3. Enter your credentials:

   * `secretKey`
   * `secretId`
   * `requisitionId` (from GoCardless API)
4. Use `status` to verify the connection
5. Enable automation with `automation-enable` (or `ae`)

---
## Automation and Token Management

Once the **setup** process is completed with your `secretKey`, `secretId`, and `requisitionId`, you need to **start automation** using:

```bash
automation-enable
```

(or the short command `ae`).

### How it works:

* The application will **obtain a new access token** from GoCardless using the provided credentials.
* Before each fetch of transactions, the token is automatically **validated**.
* If the token is **expired or invalid**, the application will **refresh it** or obtain a **new one** seamlessly.
* This ensures the automation can continuously fetch and store transactions in the MySQL database without manual intervention.

### Schedule

* By default, transactions are **fetched every 8 hours**.
