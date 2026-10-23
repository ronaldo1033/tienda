# Arquitectura
El proyecto aplica MVC y separación por capas:
- `controller`: HTTP, rutas y códigos de estado.
- `service`: reglas de negocio y transacciones.
- `repository`: persistencia Spring Data JPA.
- `model`: entidades y enumeraciones.
- `dto`: contratos de entrada/salida para autenticación, inventario y ventas.
- `security`: JWT y filtro de autenticación.
- `config`: configuración Spring Security.
- `exception`: errores centralizados.

Las ventas y movimientos de inventario usan `@Transactional` para que una excepción revierta las modificaciones. El acceso a productos se controla por roles; el registro público nunca permite elegir ADMIN.
