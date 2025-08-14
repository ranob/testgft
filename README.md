# Price Service - Inditex Tech Test

[![Java CI with Maven](https://github.com/ranob/testgft/actions/workflows/build.yml/badge.svg)](https://github.com/YOUR_USERNAME/YOUR_REPOSITORY/actions/workflows/build.yml)

This project implements a REST service to query product prices, following the requirements of the Inditex technical test.

## Key Design and Technology Decisions

* **Multi-Module Hexagonal Architecture**: The project is built following the principles of **Hexagonal Architecture (Ports and Adapters)**. To enforce this pattern and ensure strict decoupling, the architecture has been implemented in a **multi-module Maven structure**:
    * **`domain`**: A module containing pure business logic with no framework dependencies.
    * **`application`**: A module that orchestrates use cases and handles framework responsibilities like transactions.
    * **`infrastructure`**: A module containing the Spring Boot application, web (REST) and persistence (JPA) adapters.

* **Manual DTO/Entity Mapping**: Manual mapping was used for converting objects between layers (e.g., Entity to Domain, Domain to DTO) due to the simplicity of this example. For real-world, more complex projects, using a dedicated mapping library like MapStruct would be the recommended approach to ensure type safety and reduce boilerplate code.

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

The following `curl` commands test the required validation cases. The tests are based on the following sample data set, which is loaded into the in-memory database at startup:

| BRAND_ID | START_DATE          | END_DATE            | PRICE_LIST | PRODUCT_ID | PRIORITY | PRICE | CURR |
|----------|---------------------|---------------------|------------|------------|----------|-------|------|
| 1        | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 1          | 35455      | 0        | 35.50 | EUR  |
| 1        | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 | 2          | 35455      | 1        | 25.45 | EUR  |
| 1        | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 | 3          | 35455      | 1        | 30.50 | EUR  |
| 1        | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 | 4          | 35455      | 1        | 38.95 | EUR  |

### Test 1: Request at 10:00 on day 14
```bash
curl 'http://localhost:9090/api/prices/applicable?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1'
```
* **Expected Result:**
    ```json
    {
        "productId": 35455,
        "brandId": 1,
        "priceList": 1,
        "startDate": "2020-06-14T00:00:00",
        "endDate": "2020-12-31T23:59:59",
        "finalPrice": 35.50
    }
    ```
* **Reason:** At this time, only price list 1 (priority 0) is active.

### Test 2: Request at 16:00 on day 14
```bash
curl 'http://localhost:9090/api/prices/applicable?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1'
```
* **Expected Result:**
    ```json
    {
        "productId": 35455,
        "brandId": 1,
        "priceList": 2,
        "startDate": "2020-06-14T15:00:00",
        "endDate": "2020-06-14T18:30:00",
        "finalPrice": 25.45
    }
    ```
* **Reason:** Two price lists are active (1 and 2). List 2 is applied due to its higher priority (1 > 0).

### Test 3: Request at 21:00 on day 14
```bash
curl 'http://localhost:9090/api/prices/applicable?applicationDate=2020-06-14T21:00:00&productId=35455&brandId=1'
```
* **Expected Result:**
    ```json
    {
        "productId": 35455,
        "brandId": 1,
        "priceList": 1,
        "startDate": "2020-06-14T00:00:00",
        "endDate": "2020-12-31T23:59:59",
        "finalPrice": 35.50
    }
    ```
* **Reason:** Price list 2 is no longer active. The only applicable list is 1.

### Test 4: Request at 10:00 on day 15
```bash
curl 'http://localhost:9090/api/prices/applicable?applicationDate=2020-06-15T10:00:00&productId=35455&brandId=1'
```
* **Expected Result:**
    ```json
    {
        "productId": 35455,
        "brandId": 1,
        "priceList": 3,
        "startDate": "2020-06-15T00:00:00",
        "endDate": "2020-06-15T11:00:00",
        "finalPrice": 30.50
    }
    ```
* **Reason:** Two price lists are active (1 and 3). List 3 is applied due to its higher priority (1 > 0).

### Test 5: Request at 21:00 on day 16
```bash
curl 'http://localhost:9090/api/prices/applicable?applicationDate=2020-06-16T21:00:00&productId=35455&brandId=1'
```
* **Expected Result:**
    ```json
    {
        "productId": 35455,
        "brandId": 1,
        "priceList": 4,
        "startDate": "2020-06-15T16:00:00",
        "endDate": "2020-12-31T23:59:59",
        "finalPrice": 38.95
    }
    ```
* **Reason:** Two price lists are active (1 and 4). List 4 is applied due to its higher priority (1 > 0).

## Exception Handling

### Price not found (404)
The service returns a `404 Not Found`. To test this, use parameters that won't match any data:
```bash
curl -i 'http://localhost:9090/api/prices/applicable?applicationDate=2023-01-01T10:00:00&productId=99999&brandId=1'
```
* **Expected Result:** An `HTTP/1.1 404 Not Found` status code with no response body.
* **Reason:** No price list in the database matches the request criteria.

### Incorrect parameters (400)
The service returns a `400 Bad Request` with a descriptive JSON message. To test this, use an invalid data type for a parameter:
```bash
curl -i 'http://localhost:9090/api/prices/applicable?applicationDate=2020-06-14T10:00:00&productId=not-a-number&brandId=1'
```
* **Expected Result:** An `HTTP/1.1 400 Bad Request` status code with a JSON body similar to this:
    ```json
    {"error":"El parametro 'productId' debe ser de tipo 'Long' pero el valor fue: 'not-a-number'"}
    ```
* **Reason:** The `GlobalExceptionHandler` catches the type conversion error and returns a controlled response.
