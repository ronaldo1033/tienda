# Cuentas de demostración y datos iniciales

Este documento es para **desarrollo local y demostraciones**, no para producción.

## Cuentas para iniciar sesión

Abre `http://localhost:8080/login` después de iniciar la aplicación.

| Perfil | Usuario | Contraseña | Acceso esperado |
|---|---|---|---|
| Administrador | `admin.demo` | `Admin123!` | Panel administrativo en `/tienda/admin` |
| Cliente | `cliente.demo` | `Cliente123!` | Catálogo público y sesión de cliente |

También puedes abrir el catálogo sin iniciar sesión: `http://localhost:8080/` o `http://localhost:8080/tienda`.

## Datos de demostración que se cargan automáticamente

Al arrancar la aplicación se crean, si aún no existen:
- 2 cuentas de acceso: administrador y cliente.
- 3 categorías: Abarrotes, Bebidas y Limpieza.
- 1 proveedor ficticio: Distribuidora Demo.
- 6 productos con precios y stock inicial.
- 1 ficha de cliente de demostración en la tabla `clientes`.

Las contraseñas se almacenan como hashes BCrypt; no se guardan en texto plano. El cargador es idempotente y evita volver a crear los mismos registros cuando reinicias la aplicación.

## Cómo ejecutar

1. Usa Java 21 y Maven.
2. Configura y levanta la base de datos indicada en `src/main/resources/application.properties` o mediante tus variables de entorno.
3. Desde la carpeta del proyecto ejecuta `mvn spring-boot:run`.
4. Espera a que termine el arranque y abre `http://localhost:8080/login`.
5. Ingresa una de las cuentas de la tabla anterior.

La aplicación crea/actualiza las tablas mediante Hibernate cuando `spring.jpa.hibernate.ddl-auto=update`. La carga de datos necesita que la base de datos esté accesible y que el usuario configurado tenga permisos para insertar.

## Desactivar la carga automática

Para evitar que se creen los datos demo, agrega esta propiedad a `application.properties`:

```properties
app.demo-data.enabled=false
```

O define la variable de entorno `APP_DEMO_DATA_ENABLED=false`.

## Importante

- Estas contraseñas son públicas y solo deben usarse para una demo local.
- Cambia las contraseñas, el secreto JWT y las credenciales de base de datos antes de desplegar el sistema.
- La cuenta de acceso `cliente.demo` se relaciona con su ficha de cliente mediante el mismo correo electrónico para permitir el flujo de compra desde la web.
- Los pagos son simulados; no se usan tarjetas ni servicios bancarios reales.
