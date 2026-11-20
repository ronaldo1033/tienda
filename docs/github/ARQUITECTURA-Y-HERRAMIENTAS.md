# Arquitectura de herramientas del proyecto

## Herramientas y propósito

| Herramienta | Uso en Tienda Integral | Evidencia |
|---|---|---|
| Git | Control local de versiones | Commits, ramas, merge, tags |
| GitHub | Repositorio remoto y colaboración | Repo, PR, Issues, Releases |
| GitHub Projects | Planificación y seguimiento | Tablero y vistas |
| GitHub Issues | Tareas, errores e incidentes | Issues con etiquetas y milestones |
| GitHub Actions | Integración continua | Workflow Maven |
| Maven | Compilación, dependencias y pruebas | `mvn verify` |
| Docker | Empaquetado reproducible | Dockerfile e imagen |
| Plataforma cloud | Hospedaje de la aplicación | URL pública y logs |

## Flujo propuesto

`Issue → rama feature/fix → commits → Pull Request → GitHub Actions → revisión → merge a develop → PR a main → tag vX.Y.Z → Release → despliegue cloud`.

## Estrategia de ramas

- `main`: versión estable y demostrable.
- `develop`: integración de cambios terminados.
- `feature/*`: funcionalidades nuevas.
- `fix/*`: correcciones.
- `docs/*`: documentación.

En un proyecto individual, las ramas y PR siguen siendo útiles para separar trabajo y dejar trazabilidad. La revisión será propia; no se debe afirmar que hubo revisión de compañeros.

## Tablero GitHub Projects

Crear un Project vinculado al repositorio con columnas/estados:

- Pendiente
- En curso
- En revisión
- Terminado

Agregar campos `Prioridad` (Alta/Media/Baja), `Tipo` (Feature/Bug/Docs/DevOps) y `Avance` (1/2/3). Vincular Issues y Milestones.

## Gestión de incidentes

Cada bug debe incluir: resumen, entorno, pasos para reproducir, resultado esperado, resultado obtenido, evidencia (captura/log sin secretos), severidad y criterio de cierre. Usar las plantillas de `.github/ISSUE_TEMPLATE`.

## Seguridad

No subir `.env`, tokens, claves privadas, contraseñas ni datos personales. Usar GitHub Secrets para credenciales de despliegue. Cambiar secretos de ejemplo antes de cualquier publicación pública.
