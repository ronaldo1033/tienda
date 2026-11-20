# Integración continua, entrega continua y nube

## Integración continua (CI)

El workflow `.github/workflows/maven.yml` se ejecuta en push y Pull Requests para compilar y ejecutar `mvn verify` con Java 21. En GitHub: pestaña **Actions** → **Validar proyecto Java** → abrir ejecución → comprobar pasos y logs. Un workflow rojo es un fallo que debe corregirse; no se presenta como éxito.

## Entrega continua (CD)

El workflow `release.yml` se activa al publicar un tag `v*` y compila el proyecto para adjuntar el JAR a una GitHub Release. Es publicación de artefacto, no despliegue automático a un servidor. El despliegue cloud se documenta aparte porque requiere elegir proveedor y configurar secretos/servicios.

Para crear una versión, solo después de probar:

```bash
git switch main
git pull origin main
git tag -a v1.0.0 -m "Primera versión evaluable"
git push origin v1.0.0
```

Luego crear/publicar la Release desde **GitHub → Releases → Draft a new release** (o usar el tag publicado). Verificar que el workflow adjuntó el JAR.

## Contenedor Docker

El `Dockerfile` compila el JAR con Maven y lo ejecuta en una imagen Java 21. Para construir localmente:

```bash
docker build -t tienda-integral:local .
docker run --rm -p 8080:8080 tienda-integral:local
```

La aplicación debe recibir configuración mediante variables de entorno. Para PostgreSQL, definir `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DB_DRIVER` y `JWT_SECRET` en el servicio cloud. Nunca subir valores reales al repositorio.

## Despliegue cloud (pendiente de ejecutar con cuenta del estudiante)

1. Elegir proveedor compatible con Docker (por ejemplo Render, Railway o un servicio cloud institucional).
2. Crear un servicio web desde el repositorio `ronaldo1033/tienda` o desde una imagen Docker.
3. Configurar PostgreSQL administrado y variables de entorno en el panel del proveedor.
4. Configurar el comando/puerto según el proveedor; la aplicación escucha en el puerto `8080` por defecto. Si el proveedor exige `$PORT`, confirmar que Spring Boot lo recibe (la mayoría de plataformas admite `PORT`; de ser necesario añadir `server.port=${PORT:8080}` en configuración).
5. Desplegar y revisar logs de arranque.
6. Abrir la URL pública, probar catálogo, login, carrito y una compra de prueba.
7. Guardar capturas de configuración (sin secretos), logs de despliegue, URL y prueba funcional.

**Estado:** el repositorio incluye soporte de contenedor y documentación. El servicio cloud no queda creado automáticamente: requiere cuenta, selección de proveedor, base de datos y credenciales del propietario. No declarar el Avance 3 completado hasta tener URL pública verificada.
