# Guía práctica de Git y GitHub para el proyecto Tienda

Repositorio: https://github.com/ronaldo1033/tienda

## 1. Conceptos básicos

- **Repositorio:** carpeta con archivos e historial de cambios.
- **Working tree:** archivos que estás editando.
- **Staging (`git add`):** cambios seleccionados para el próximo commit.
- **Commit:** registro local de un conjunto de cambios.
- **Rama:** línea independiente de trabajo.
- **Remote (`origin`):** dirección del repositorio alojado en GitHub.
- **Push / Pull:** enviar commits / traer commits del remoto.
- **Pull Request (PR):** propuesta de integrar cambios y revisarlos antes del merge.
- **Tag / Release:** marca una versión concreta; la Release añade notas y distribución en GitHub.

## 2. Requisitos

Instala Git, JDK 21 y Maven. Comprueba con:

```bash
git --version
java -version
mvn -version
```

Configura tu identidad una vez en el equipo:

```bash
git config --global user.name "Tu nombre"
git config --global user.email "tu-correo@example.com"
```

Usa el BAT `PUBLICAR_EN_GITHUB.bat` de la carpeta entregada para inicializar/publicar el proyecto. Cuando GitHub solicite autenticación, usa el navegador o el mecanismo oficial de Git Credential Manager; nunca pegues un token dentro del BAT.

## 3. Flujo recomendado de ramas

- `main`: versión estable.
- `develop`: integración de trabajo probado.
- `feature/nombre-corto`: nueva funcionalidad.
- `fix/nombre-corto`: corrección de error.
- `docs/nombre-corto`: documentación.

Ejemplo de trabajo normal:

```bash
git switch main
git pull origin main
git switch -c feature/catalogo
# editar y probar
git status
git add src docs
git commit -m "feat: mejorar catalogo de productos"
git push -u origin feature/catalogo
```

Luego abre un PR desde `feature/catalogo` hacia `develop` en GitHub. Revisa los cambios y el resultado de Actions. Después de la revisión, haz merge. Para publicar una versión estable, integra `develop` en `main` mediante PR.

## 4. Convención de commits

- `feat:` funcionalidad nueva.
- `fix:` corrección.
- `docs:` documentación.
- `test:` pruebas.
- `refactor:` reestructuración sin cambiar el comportamiento esperado.
- `build:` configuración de compilación o dependencias.
- `ci:` integración continua.

Ejemplos:

```bash
git commit -m "feat: agregar búsqueda de productos"
git commit -m "fix: validar datos del formulario de acceso"
git commit -m "docs: documentar cuentas de demostración"
```

## 5. Merge y resolución de conflictos (práctica controlada)

1. Crea dos ramas desde `develop`, por ejemplo `feature/conflicto-a` y `feature/conflicto-b`.
2. En ambas modifica **la misma línea** de un archivo de práctica, con textos diferentes.
3. Haz commit en cada rama.
4. Integra la primera rama en `develop`.
5. Intenta integrar la segunda. Git puede señalar un conflicto.
6. Abre el archivo y busca `<<<<<<<`, `=======`, `>>>>>>>`; conserva el contenido correcto y elimina esas marcas.
7. Ejecuta `git add <archivo>` y `git commit` para registrar la resolución.
8. Comprueba `git status` y el historial con `git log --oneline --graph --decorate --all`.

No provoques conflictos en archivos de producción ni uses `git push --force` como solución general.

## 6. Sincronizar y recuperar cambios

```bash
git fetch origin
git status
git pull --rebase origin develop
git log --oneline --graph --decorate --all
```

Si el rebase encuentra conflicto, resuélvelo y usa `git add <archivo>` seguido de `git rebase --continue`. Para cancelar el rebase: `git rebase --abort`.

Antes de revertir cambios compartidos, identifica el commit con `git log`. Para deshacer de forma segura un commit ya publicado, normalmente usa `git revert <sha>` y publica el nuevo commit.

## 7. Pull Request y revisión

1. Publica tu rama con `git push -u origin nombre-rama`.
2. En GitHub, crea un PR a `develop`.
3. Explica el problema, el cambio y cómo lo probaste.
4. Comprueba **Files changed** y el workflow **Validar proyecto Java**.
5. Solicita revisión a un compañero si el trabajo es grupal.
6. Resuelve comentarios; integra solo cuando esté listo.
7. Elimina la rama remota después del merge si ya no se necesita.

La plantilla del proyecto está en `.github/pull_request_template.md`.

## 8. Etiqueta y Release

Solo cuando la versión esté revisada y probada:

```bash
git switch main
git pull origin main
git tag -a v1.0.0 -m "Primera versión estable"
git push origin v1.0.0
```

Después abre **Releases → Draft a new release**, elige `v1.0.0` y redacta notas reales. Si esa etiqueta ya existe, no la vuelvas a crear; elige una nueva versión, por ejemplo `v1.0.1`.

## 9. CI con GitHub Actions

El workflow `.github/workflows/maven.yml` intenta ejecutar `mvn verify` en push y Pull Requests usando Java 21. La validación puede fallar si el proyecto tiene errores de compilación o pruebas; usa los logs para corregirlos. No declares la compilación exitosa sin ver el resultado verde.

## 10. Herramientas de colaboración y gestión

- **Issues:** registra bugs y tareas con criterios de aceptación.
- **Pull Requests:** revisión y conversación de cambios.
- **GitHub Projects:** organiza tarjetas en `Pendiente`, `En curso`, `En revisión`, `Terminado`.
- **Milestones:** agrupa tareas por avance/entrega.
- **Actions:** automatiza comprobaciones repetibles.

## 11. Comandos útiles para exponer

```bash
git status
git add .
git commit -m "tipo: descripción"
git branch
git switch -c feature/ejemplo
git merge feature/ejemplo
git remote -v
git fetch origin
git pull origin main
git push -u origin feature/ejemplo
git log --oneline --graph --decorate --all
```
