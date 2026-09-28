# Assignment 5 - Software Quality and Security – Support Ticket Application
## Overview
This project is a Spring Boot support ticket application used to evaluate and improve software quality and application security.
The starter application was reviewed for significant quality and security concerns. Identified issues were documented, remediated, and verified through automated testing, end-to-end testing, and dependency security analysis.
## Technologies
- Java 21
- Spring Boot
- Spring MVC
- Thymeleaf
- Spring JDBC
- H2 Database
- Maven
- JUnit 5
- Mockito
- Selenium WebDriver
- GitHub Dependabot
## Quality and Security Improvements
The following improvements were implemented during the application review:
### SQL Injection Prevention
Ticket search originally incorporated user-controlled search input into SQL statements.
The repository implementation was updated to use parameterized JDBC queries so user input is handled as data rather than executable SQL.
### Authorization Enforcement
Protected ticket operations now require the ticket's generated access token.
Authorization checks are performed before allowing access to protected ticket information or status changes. Invalid access tokens are rejected.
### Secure Error Handling
Application error handling was updated so internal exception details are not returned directly to users.
Users receive a controlled error response while implementation details remain hidden.
### Sensitive Logging Reduction
Logging was revised to avoid recording unnecessary ticket information or other sensitive user-controlled data.
Operational logging retains the information necessary to identify application activity without exposing complete ticket contents.
### Server-Side Input Validation
Ticket creation now validates input before persistence.
Validation includes:
- Required requester name
- Required and valid email input
- Required category
- Required description
- Maximum requester-name length
- Maximum email length
- Maximum category length
- Maximum description length
  Invalid or oversized input is rejected before being stored.
## Automated Testing
The automated test suite was expanded to verify both expected application behavior and security-related conditions.
Testing includes:
- Repository search behavior
- SQL-injection-style search input
- Successful ticket creation
- Missing and invalid ticket input
- Input length boundaries
- Authorized ticket access
- Rejection of incorrect access tokens
- Authorized status updates
- Rejection of unauthorized status updates
- Controlled application error handling
  Run the complete automated test suite with:
```bash
mvn clean test
```
A successful execution should complete with zero test failures and zero test errors.
## End-to-End Testing
Selenium WebDriver tests exercise the application from the user's perspective.
The end-to-end tests verify representative workflows including:
- Creating a support ticket through the web interface
- Entering valid ticket information
- Verifying successful ticket creation
- Submitting invalid ticket information
- Confirming invalid input is rejected
  These tests provide additional verification that the application remains functional after the quality and security improvements.
## Dependency Security
The project's external dependencies were reviewed using GitHub's Dependency Graph and Dependabot Alerts.
The final dependency analysis reported:
- **Open Dependabot alerts: 0**
- **Actionable dependency vulnerabilities identified: 0**
  No dependency remediation was required based on the completed analysis.
## Running the Application
From the project root, run:
```bash
mvn spring-boot:run
```
After the application starts, access it locally through:
```text
http://localhost:8080
```
## Verification
Final verification included:
1. Clean Maven build and automated test execution.
2. Repository, service, controller, validation, and security-related testing.
3. Automated Selenium end-to-end testing.
4. Manual verification of representative application functionality.
5. GitHub Dependabot dependency security analysis.
6. Review of corrected security and quality issues to confirm they are no longer reproducible.
   The final application remains operational after implementation of the software quality and security improvements.
## Project Purpose
This project was completed for **CEN4025 – Software Development 2** as an exercise in systematic software quality assessment, secure coding, automated testing, end-to-end verification, and dependency security analysis.