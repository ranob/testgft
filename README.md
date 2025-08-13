# Servicio de Precios - Prueba Técnica Inditex

[![Java CI with Maven](https://github.com/TU_USUARIO/TU_REPOSITORIO/actions/workflows/build.yml/badge.svg)](https://github.com/TU_USUARIO/TU_REPOSITORIO/actions/workflows/build.yml)

Este proyecto implementa un servicio REST para consultar precios de productos, siguiendo los requisitos de la prueba técnica de Inditex.

## Decisiones Clave de Diseño y Tecnología

-   **JDK 21 y Hilos Virtuales (Virtual Threads)**: El servicio está construido sobre JDK 21 para aprovechar sus características más modernas. Se han habilitado los **Hilos Virtuales** de Project Loom a través de la configuración de Spring Boot. Se ha tomado esta decisión porque la aplicación es principalmente **I/O-bound** (limitada por las esperas a la base de datos). Los hilos virtuales permiten una escalabilidad muy superior y un mejor uso de los recursos del sistema bajo alta concurrencia.

-   **Arquitectura Hexagonal Multi-Módulo**: El proyecto está construido siguiendo los principios de la **Arquitectura Hexagonal (Puertos y Adaptadores)**. Para reforzar este patrón y garantizar un desacoplamiento estricto, la arquitectura se ha implementado en una **estructura Maven multi-módulo**:
    -   **`domain`**: Módulo que contiene la lógica de negocio pura, sin dependencias de frameworks. Su `pom.xml` prohíbe el acoplamiento con la infraestructura.
    -   **`application`**: Módulo que orquesta los casos de uso y gestiona las responsabilidades del framework como las transacciones. Depende de `domain`.
    -   **`infrastructure`**: Módulo que contiene la aplicación Spring Boot, los adaptadores web (REST) y de persistencia (JPA), y el punto de entrada ejecutable. Depende de `application`.

-   **Estrategia de Testing Completa**: Se ha implementado una pirámide de testing robusta:
    -   **Tests Unitarios (Dominio y Aplicación)**: Pruebas puras (con y sin Mockito) para verificar la lógica de negocio y la orquestación de forma aislada.
    -   **Tests de Corte (Infraestructura)**: Pruebas con `@WebMvcTest` y `@DataJpaTest` para verificar los adaptadores (Controller, Repository) de forma independiente.
    -   **Tests End-to-End (Infraestructura)**: Pruebas con RestAssured que levantan la aplicación completa y validan los 5 casos de uso solicitados a través de peticiones HTTP reales.

## Requisitos Previos

-   JDK 21 o superior.
-   Apache Maven 3.8 o superior.

## Cómo Construir y Ejecutar

1.  **Clonar el repositorio:**
    ```bash
    git clone [URL-DE-TU-REPOSITORIO]
    cd price-service
    ```

2.  **Construir el proyecto completo:**
    ```bash
    mvn clean install
    ```
    Este comando compilará y probará todos los módulos en el orden correcto.

3.  **Ejecutar la aplicación:**
    El JAR ejecutable se genera en el módulo de `infrastructure`.
    ```bash
    java -jar infrastructure/target/infrastructure-0.0.1-SNAPSHOT.jar
    ```
    El servicio se iniciará en `http://localhost:8080`.

## Cómo Ejecutar los Tests

Para ejecutar el conjunto completo de tests de todos los módulos:
```bash
mvn test
```

## Ejemplo de Petición a la API

Una vez la aplicación esté en marcha, puedes usar `curl` para probar el endpoint:

```bash
curl 'http://localhost:8080/api/prices/applicable?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1'
```

## Gestión de Excepciones

-   **Precio no encontrado**: El servicio devuelve un `404 Not Found`.
-   **Parámetros incorrectos**: El servicio devuelve un `400 Bad Request` con un mensaje JSON descriptivo (ej: si `productId` no es un número).
