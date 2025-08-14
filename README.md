# Price Service - Inditex Tech Test

[![Java CI with Maven](https://github.com/YOUR_USERNAME/YOUR_REPOSITORY/actions/workflows/build.yml/badge.svg)](https://github.com/YOUR_USERNAME/YOUR_REPOSITORY/actions/workflows/build.yml)

This project implements a REST service to query product prices, following the requirements of the Inditex technical test.

## Key Design and Technology Decisions

* **Multi-Module Hexagonal Architecture**: The project is built following the principles of **Hexagonal Architecture (Ports and Adapters)**. To enforce this pattern and ensure strict decoupling, the architecture has been implemented in a **multi-module Maven structure**:
    * **`domain`**: A module containing pure business logic with no framework dependencies.
    * **`application`**: A module that orchestrates use cases and handles framework responsibilities like transactions.
    * **`infrastructure`**: A module containing the Spring Boot application, web (REST) and persistence (JPA) adapters.

* **Manual DTO/Entity Mapping**: For object mapping between layers (e.g., Entity to Domain, Domain to DTO), manual mapping was chosen over libraries like MapStruct. Given the simplicity of the models in this project, manual mapping is more direct, avoids extra dependencies, and adheres to the principle of keeping the solution minimal, as requested.

* **Comprehensive Testing Strategy**: A robust testing pyramid has been implemented:
    * **Unit Tests**: To verify business logic and component behavior in isolation.
    * **Slice Tests**: Using `@WebMvcTest` and `@DataJpaTest` to verify web and persistence adapters independently.
    * **End-to-End Tests**: Using RestAssured to validate the 5 required use cases through real HTTP requests.

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

3.  **Run the application:**
    ```bash
    java -jar infrastructure/target/infrastructure-0.0.1-SNAPSHOT.jar
    ```
    The service will start on `http://localhost:9090`.

## How to Run Tests

To run the full test suite for all modules:
```bash
mvn test
```

## API Request Examples

* **Test 1:** Request at 10:00 on day 14
    ```bash
    curl 'http://localhost:9090/api/prices/applicable?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1'
    ```

* **Test 2:** Request at 16:00 on day 14
    ```bash
    curl 'http://localhost:9090/api/prices/applicable?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1'
    ```

* **Test 3:** Request at 21:00 on day 14
    ```bash
    curl 'http://localhost:9090/api/prices/applicable?applicationDate=2020-06-14T21:00:00&productId=35455&brandId=1'
    ```

* **Test 4:** Request at 10:00 on day 15
    ```bash
    curl 'http://localhost:9090/api/prices/applicable?applicationDate=2020-06-15T10:00:00&productId=35455&brandId=1'
    ```

* **Test 5:** Request at 21:00 on day 16
    ```bash
    curl 'http://localhost:9090/api/prices/applicable?applicationDate=2020-06-16T21:00:00&productId=35455&brandId=1'
    ```

## Exception Handling

* **Price not found (404)**:
    ```bash
    curl -i 'http://localhost:9090/api/prices/applicable?applicationDate=2023-01-01T10:00:00&productId=99999&brandId=1'
    ```

* **Incorrect parameters (400)**:
    ```bash
    curl -i 'http://localhost:9090/api/prices/applicable?applicationDate=2020-06-14T10:00:00&productId=not-a-number&brandId=1'
    