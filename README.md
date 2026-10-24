# Resident and Staff Management System

A web-based management application built using Java, Spring Boot, Spring Security, Thymeleaf, and MySQL. It helps administrators manage resident and staff records through a dashboard and an easy-to-use interface.

## Features

- Admin login and authentication
- Resident management: add, view, edit, delete, and search records
- Staff management: add, view, edit, delete, and search records
- Form validation for user input
- Duplicate email validation
- Dashboard with resident and staff counts
- Department-wise staff analytics
- Recent residents overview
- Export resident records to CSV
- Export staff records to CSV
- Logout and protected page access

## Technologies Used

- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security
- Thymeleaf
- MySQL
- Maven
- HTML and CSS

## Prerequisites

Install the following before running the project:

- JDK compatible with the project's Maven configuration
- IntelliJ IDEA or another Java IDE
- MySQL Server
- Maven

## Setup and Installation

1. Clone or download this repository.
2. Open the project in IntelliJ IDEA.
3. Create a MySQL database for the application.
4. Configure your database URL, username, and password in `src/main/resources/application.properties`.
5. Ensure the configured database credentials match your local MySQL setup.
6. Run the main Spring Boot application class: `DemoApplication.java`.
7. Open `http://localhost:8080/login` in your browser.
8. Sign in using the administrator account configured in your local database.

**Security note:** Never commit real database passwords or administrator credentials to GitHub. Keep local secrets out of version control.

## CSV Export

After signing in, use the following endpoints to download CSV files:

- Residents: `/export/residents`
- Staff: `/export/staff`

The downloaded CSV files can be opened in spreadsheet applications such as Microsoft Excel.

## Project Structure

```text
src/
 └── main/
     ├── java/com/example/demo/
     │   ├── config/
     │   ├── controller/
     │   ├── entity/
     │   ├── repository/
     │   └── service/
     └── resources/
         ├── templates/
         └── application.properties
pom.xml
README.md
```

## Future Improvements

- Pagination for large datasets
- Role-based access for multiple user types
- Excel `.xlsx` export
- Unit and integration tests
- Improved reporting and analytics

## Author

Developed as a Java and Spring Boot learning project.
