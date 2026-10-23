# Modelo de datos
Entidades principales: Categoria, Proveedor, Producto, Cliente, DireccionEnvio, Usuario, Venta, DetalleVenta, Pago y MovimientoInventario.
- Producto referencia Categoria y opcionalmente Proveedor.
- DireccionEnvio pertenece a Cliente.
- Venta referencia Cliente y opcionalmente Usuario/DireccionEnvio.
- DetalleVenta vincula Venta con Producto y conserva precio/cantidad de la operación.
- Pago referencia Venta.
- MovimientoInventario referencia Producto y opcionalmente Usuario.

Hibernate crea/actualiza el esquema local con `DDL_AUTO=update`. Para producción, incorporar migraciones versionadas y cambiar a `validate`.
