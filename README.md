# Tienda Integral — Backend REST

Backend Java 21 / Spring Boot 3.4 / PostgreSQL con arquitectura por capas, autenticación JWT, roles ADMIN/CLIENTE, inventario, ventas y pagos simulados.

## Requisitos
- JDK 21
- Maven 3.9+
- Docker Desktop (opcional) o PostgreSQL 14+
- Git (opcional)

## Inicio rápido
1. Copia `.env.example` a `.env` y cambia las credenciales. Spring Boot no carga automáticamente `.env`; exporta esas variables en tu terminal o configura el entorno de ejecución del IDE.
2. Inicia PostgreSQL: `docker compose up -d`.
3. Configura `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` y `JWT_SECRET` como variables del sistema.
4. Ejecuta: `mvn spring-boot:run`.
5. Swagger: http://localhost:8080/swagger-ui.html

Para Windows PowerShell, por ejemplo:
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/tienda_integral"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="tu_clave"
$env:JWT_SECRET=[Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
mvn spring-boot:run
```

## Autenticación
`POST /api/v1/auth/registro`
```json
{"username":"cliente1","correo":"cliente1@example.com","password":"ClaveSegura123"}
```
El registro público siempre asigna `CLIENTE`. No existe registro público de administradores.
`POST /api/v1/auth/login`
```json
{"username":"cliente1","password":"ClaveSegura123"}
```
Usa el token devuelto como `Authorization: Bearer <token>`.

## Endpoints principales
- `/api/v1/auth`: registro e inicio de sesión.
- `/api/v1/categorias`, `/api/v1/proveedores`, `/api/v1/productos`
- `/api/v1/clientes`, `/api/v1/usuarios`
- `/api/v1/inventario/movimientos`
- `/api/v1/ventas`, `/api/v1/pagos`
- `/api/v1/reportes/resumen`

Los cambios administrativos requieren rol ADMIN. Para crear un administrador inicialmente, registra un usuario cliente y asigna el rol ADMIN directamente en la base de datos mediante un procedimiento controlado. Nunca expongas esa operación públicamente.

## Ejemplo: producto
Las relaciones `categoria` y `proveedor` se representan con sus IDs en las solicitudes; consulta primero los recursos relacionados.
```json
{"nombre":"Arroz","descripcion":"Bolsa 5 kg","precio":18.90,"stock":20,"stockMinimo":5,"estado":"ACTIVO","categoria":{"id":1},"proveedor":{"id":1}}
```

## Ventas y pagos simulados
`POST /api/v1/ventas`
```json
{"clienteId":1,"direccionId":null,"items":[{"productoId":1,"cantidad":2}]}
```
El backend calcula precios y total, valida stock y registra los movimientos en una transacción.
`POST /api/v1/pagos`
```json
{"ventaId":1,"metodo":"TRANSFERENCIA_SIMULADA","monto":37.80,"aprobar":true}
```
No se conecta a bancos ni almacena datos reales de tarjetas.

## Pruebas y empaquetado
- `mvn test`
- `mvn clean package`
- `java -jar target/tienda-integral-backend-1.0.0.jar`

## Consideraciones
- En desarrollo se usa `ddl-auto=update`. Para producción se recomienda Flyway/Liquibase y `validate`.
- El secreto JWT por defecto solo sirve para desarrollo; reemplázalo en cualquier entorno real.
- Algunos listados devuelven colecciones completas; añade paginación antes de utilizarlo con volúmenes grandes.


## Interfaz web con Thymeleaf

El proyecto incluye una interfaz web responsive con Bootstrap 5 y Thymeleaf:

- `/` o `/tienda`: catálogo público y búsqueda de productos.
- `/tienda/producto/{id}`: detalle de un producto.
- `/registro`: creación de cuenta de cliente.
- `/login`: inicio de sesión.
- `/tienda/admin`: panel de administración, métricas básicas, inventario y alta de productos.
- `/swagger-ui.html`: documentación de la API REST.

Las cuentas creadas desde el formulario de registro tienen rol `CLIENTE`. Para entrar al panel se necesita una cuenta con rol `ADMIN`; no se crea un administrador por defecto. Puedes asignar el rol desde la base de datos únicamente en un entorno de desarrollo controlado.

El flujo de carrito, generación de pedidos desde el catálogo y checkout real no está implementado todavía; la ficha del producto lo indica para evitar dar a entender que se puede comprar en línea de extremo a extremo.


## Inicio rápido (frontend Thymeleaf)

1. Requiere Java 21 y Maven. Abre esta carpeta en IntelliJ IDEA o ejecuta `mvn spring-boot:run`.
2. Por defecto usa H2 en memoria para que la página pueda abrir sin instalar PostgreSQL. Abre `http://localhost:8080`. La base H2 se reinicia al detener la aplicación.
3. Para usar PostgreSQL, crea la base `tienda_integral` y configura variables de entorno: `DB_URL=jdbc:postgresql://localhost:5432/tienda_integral`, `DB_USERNAME=postgres`, `DB_PASSWORD=tu_clave`, `DB_DRIVER=org.postgresql.Driver`.
4. Si aparece una página 500, mira la consola donde se ejecuta Spring Boot: la causa real aparece en el primer bloque `Caused by:`.

### Solución al error Whitelabel 500

El Whitelabel es solo una página genérica; suele aparecer si la base de datos no está disponible o si falla una plantilla. Esta versión usa H2 por defecto para evitar el fallo de conexión a PostgreSQL cuando no está configurado. En producción, configura PostgreSQL y desactiva la consola H2.


## Cuentas demo y carga inicial

Al iniciar la aplicación se cargan automáticamente cuentas de prueba, categorías, un proveedor, productos y una ficha de cliente. Consulta **[docs/cuentas-demo.md](docs/cuentas-demo.md)** para ver usuarios y contraseñas, URL de acceso, pasos de inicio y cómo desactivar la carga demo. La carga es idempotente y está pensada únicamente para desarrollo local.
