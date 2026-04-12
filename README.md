# ShelfAware — Library Management System

<p align="center">
  <img src="src/view/assets/shelfaware_logo_framed.png" alt="ShelfAware" width="700"/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-11+-F89820?style=flat&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/JavaFX-Framework-1E88E5?style=flat&logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Architecture-MVC-6DB33F?style=flat"/>
  <img src="https://img.shields.io/badge/Database-MySQL%20%2F%20JDBC-4479A1?style=flat&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Status-Completed-2ECC71?style=flat"/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Course-SWE324%20Visual%20Programming-8B0000?style=flat&logo=academia&logoColor=white"/>
</p>

---
## Overview

ShelfAware is a desktop library management system built with Java and JavaFX, developed as a term project for **SWE324 — Visual Programming**. It follows the MVC architectural pattern and provides a complete solution for managing books, members, and circulation workflows within a library environment.

<p align="center">
  <img src="src/view/assets/shelfaware_app_icon.png" alt="ShelfAware" width="200"/>
</p>

---

## Contributors

| Name | GitHub |
|------|--------|
| Samed Tevin | [![GitHub](https://img.shields.io/badge/samedTevin-181717?style=flat&logo=github&logoColor=white)](https://github.com/samedTevin) |
| İlayda Acarlaar | [![GitHub](https://img.shields.io/badge/acarlarilayda-181717?style=flat&logo=github&logoColor=white)](https://github.com/acarlarilayda) |
| Ebrar Sena Kılıçkaya | [![GitHub](https://img.shields.io/badge/ebrarkilickaya-181717?style=flat&logo=github&logoColor=white)](https://github.com/ebrarkilickaya) |

---
## Features

| Module               | Description                                                         |
|----------------------|---------------------------------------------------------------------|
| Book Management      | Add, edit, browse, and track books in the library catalog           |
| Member Management    | Register members and manage their profiles                          |
| Circulation System   | Handle book loans, returns, and borrowing history                   |
| Member Cards         | Generate and manage physical library cards for members              |
| Authentication       | Secure login, registration, and password recovery                   |
| Statistics           | View library usage reports and circulation analytics                |

---

## Project Structure

```
src/
├── controller/    # JavaFX controllers (Book, Member, Circulation, Login, etc.)
├── main/          # Application entry point
├── model/         # Data models: Book, Loan, Member, User
├── repository/    # Data access layer (CRUD operations)
├── session/       # Session management
└── util/          # Alerts, Database, SceneChanger
```

---

## Tech Stack

| Layer        | Technology                  |
|--------------|-----------------------------|
| Language     | Java 11+                    |
| UI Framework | JavaFX                      |
| Database     | MySQL (via JDBC)   |
| Architecture | MVC (Model-View-Controller) |
| IDE          | IntelliJ IDEA / Eclipse     |

---

## Getting Started

### Prerequisites

- Java 11 or higher
- JavaFX SDK
- MySQL or SQLite database

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/LibraryManagement.git
   cd LibraryManagement
   ```

2. Configure the database connection in `src/util/Database.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/shelfaware";
   private static final String USER = "your_username";
   private static final String PASSWORD = "your_password";
   ```

3. Open the project in your IDE and run `Main.java`.

---

## License

This project is developed for educational purposes as part of **SWE324 — Visual Programming**.
