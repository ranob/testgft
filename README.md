# Price Service - Inditex Tech Test

[![Java CI with Maven](https://github.com/ranob/testgft/actions/workflows/build.yml/badge.svg)](https://github.com/ranob/testgft/actions/workflows/build.yml)

This project implements a REST service to query product prices, following the requirements of the Inditex technical test.

## Key Design and Technology Decisions

* **Multi-Module Hexagonal Architecture**: The project is built following the principles of **Hexagonal Architecture (Ports and Adapters)**. To enforce this pattern and ensure strict decoupling, the architecture has been implemented in a **multi-module Maven structure**:
    * **`domain`**: A module containing pure business logic with no framework dependencies. Its `pom.xml` forbids coupling with the infrastructure.
    * **`application`**: A module that orchestrates use cases and handles framework responsibilities like transactions. It depends on `domain`.
    * **`infrastructure`**: A module containing the Spring Boot application, web (REST) and persistence (JPA) adapters, and the executable entry point. It depends on `application`.

* **JDK 21 & Virtual Threads**: The service is built on JDK 21 to leverage its modern features. Project Loom's **Virtual Threads** have been enabled via Spring Boot's configuration. This decision was made because the application is primarily **I/O-bound** (limited by database waits). Virtual threads allow for much higher scalability and better system resource utilization under high concurrency.

* **Comprehensive Testing Strategy**: A robust testing pyramid has been implemented:
    * **Unit Tests (Domain & Application)**: Pure tests (with and without Mockito) to verify business logic and orchestration in isolation.
    * **Slice Tests (Infrastructure)**: Tests using `@WebMvcTest` and `@DataJpaTest` to verify adapters (Controller, Repository) independently.
    * **End-to-End Tests (Infrastructure)**: Tests with RestAssured that start the full application and validate the 5 required use cases through real HTTP requests.

## Prerequisites

* JDK 21 or higher.
* Apache Maven 3.8 or higher.

## How to Build and Run

1.  **Clone the repository:**
    ```bash
    git clone [URL-OF-YOUR-REPOSITORY]
    cd price-service
    ```

2.  **Build the full project:**
    ```bash
    mvn clean install
    ```
    This command will compile and test all modules in the correct order.

3.  **Run the application:**
    The executable JAR is generated in the `infrastructure` module.
    ```bash
    java -jar infrastructure/target/infrastructure-0.0.1-SNAPSHOT.jar
    ```
    The service will start on `http://localhost:8080`.

## How to Run Tests

To run the full test suite for all modules:
```bash
mvn test
```

## API Request Example

Once the application is running, you can use `curl` to test the endpoint:

```bash
curl 'http://localhost:8080/api/prices/applicable?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1'
```

## Exception Handling

* **Price not found**: The service returns a `404 Not Found`.
* **Incorrect parameters**: The service returns a `400 Bad Request` with a descriptive JSON message (e.g., if `productId` is not a number).
