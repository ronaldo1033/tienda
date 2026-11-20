# Instalación local

## Inicio rápido con H2
1. Instalar JDK 21 y Maven 3.9+.
2. Desde la carpeta del proyecto ejecutar `mvn spring-boot:run`.
3. Abrir `http://localhost:8080/login`.
4. La aplicación utiliza H2 en memoria por defecto; los datos se reinician al detenerla.
5. Ejecutar pruebas con `mvn test`.

## Usar PostgreSQL
1. Instalar PostgreSQL o levantarlo con `docker compose up -d`.
2. Crear la base `tienda_integral` si no existe.
3. Configurar las variables `DB_URL=jdbc:postgresql://localhost:5432/tienda_integral`, `DB_USERNAME`, `DB_PASSWORD` y `JWT_SECRET` en el entorno del proceso.
4. Ejecutar `mvn spring-boot:run`.

El archivo `.env.example` es una referencia; Spring Boot no lee `.env` automáticamente. Configura las variables en tu terminal o IDE. Para producción, desactiva la consola H2, usa un secreto JWT aleatorio y configura `DDL_AUTO=validate` junto con migraciones de base de datos.
