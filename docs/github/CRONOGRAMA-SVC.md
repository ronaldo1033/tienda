# Evidencias de Git y GitHub alineadas al cronograma del curso

Repositorio remoto del proyecto: https://github.com/ronaldo1033/tienda

Este documento organiza las evidencias que conviene mostrar. No afirma que una actividad remota esté hecha si todavía no se ha ejecutado.

| Semana / sesiones | Tema del sílabo | Evidencia que queda en el proyecto |
|---|---|---|
| 1 / 1–2 | Introducción y conceptos de control de versiones | `docs/github/GIT-GUIA-PRACTICA.md`: repositorio, working tree, staging, commit y remoto. |
| 2 / 3–4 | Instalación, configuración y comandos básicos | Requisitos Git + JDK 21 + Maven; BAT de publicación y comandos de configuración. |
| 3 / 5–6 | Repositorios, ramas y commits | Estrategia `main`, `develop`, `feature/*`, `fix/*`; mensajes de commit sugeridos. |
| 4 / 7–8 | Ramificación y fusión | Práctica de merge documentada en la guía práctica. |
| 4–5 / 9–10 | Conflictos y colaboración remota | Práctica controlada para provocar y resolver un conflicto, y sincronización con `origin`. |
| 6 / 11–12 | Nube y flujo básico; Avance 1 | Repositorio GitHub, `.github/workflows/maven.yml`, instrucciones de push y checklist de evidencia. |
| 7 / 13–14 | Trabajo colaborativo en la nube | Issues de tareas/bugs, plantilla de PR y reglas de revisión sugeridas. |
| 8 / 15–16 | Pull requests, Merge Conflict y Release | Flujo de PR y creación de etiqueta `v1.0.0` descritos en la guía práctica. |
| 9 / 17–18 | Herramientas de colaboración | Issues, Pull Requests, conversaciones y revisión de cambios. |
| 10 / 19–20 | Gestión de actividades | Plantillas de Issues y checklist de criterios de aceptación. |
| 11 / 21–22 | Gestión de proyectos; Avance 2 | Usar GitHub Projects para organizar backlog, en curso y terminado; guardar capturas/evidencias. |

## Evidencias que debes capturar en GitHub

1. Página principal del repositorio y su historial de commits.
2. Pestaña **Branches** con `main`, `develop` y ramas de trabajo que realmente hayas publicado.
3. Un Pull Request con descripción, revisión y resultado del merge.
4. Un conflicto de merge reproducido y resuelto, mostrando el commit final.
5. Una etiqueta/release, por ejemplo `v1.0.0`, solo después de verificar el proyecto.
6. La ejecución de **Actions** del workflow Maven. Si falla, registra y corrige el error; no presentes un fallo como éxito.
7. Issues y, si el docente lo solicita, un tablero GitHub Projects.

## Cuidado

- El BAT no crea automáticamente PRs, no aprueba revisiones ni inventa actividad colaborativa. Esas acciones se hacen en GitHub y deben corresponder a trabajo real.
- No guardes tokens, contraseñas ni `.env` en el repositorio.
- Antes de publicar, revisa `git status` y confirma que no se subirán credenciales o datos personales.
