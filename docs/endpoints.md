# Endpoints
| Método | Ruta | Acceso | Uso |
|---|---|---|---|
| POST | `/api/v1/auth/registro` | Público | Registrar CLIENTE |
| POST | `/api/v1/auth/login` | Público | Emitir JWT |
| GET/POST/PUT | `/api/v1/categorias` | Lectura autenticada / escritura ADMIN | Categorías |
| GET/POST/PUT | `/api/v1/proveedores` | ADMIN | Proveedores |
| GET/POST/PUT | `/api/v1/productos` | Lectura autenticada / escritura ADMIN | Productos |
| GET/POST/PUT | `/api/v1/clientes` | ADMIN/CLIENTE según operación | Clientes |
| POST/GET | `/api/v1/inventario/movimientos` | ADMIN | Movimientos de stock |
| POST/GET | `/api/v1/ventas` | Crear autenticado; listar ADMIN | Ventas |
| PATCH | `/api/v1/ventas/{id}/cancelar` | ADMIN | Cancelar venta pendiente |
| POST/GET | `/api/v1/pagos` | Crear autenticado; listar ADMIN | Pago simulado |
| GET | `/api/v1/reportes/resumen` | ADMIN | Resumen administrativo |
| GET | `/api/v1/usuarios` | ADMIN | Usuarios sin hashes |

Swagger UI: `/swagger-ui.html`; OpenAPI JSON: `/v3/api-docs`.
