# Instalación local
1. Instalar JDK 21, Maven y PostgreSQL o Docker.
2. Levantar PostgreSQL con `docker compose up -d`.
3. Exportar las variables `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`.
4. Ejecutar `mvn spring-boot:run`.
5. Abrir `http://localhost:8080/swagger-ui.html`.
6. Ejecutar pruebas con `mvn test`.

El archivo `.env.example` es una referencia; Spring Boot no lee `.env` por defecto sin una herramienta adicional.
