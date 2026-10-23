@echo off
setlocal EnableExtensions
chcp 65001 >nul
 title Publicar Tienda en GitHub

set "REPO_URL=https://github.com/ronaldo1033/tienda.git"
set "BRANCH=main"

 echo =====================================================
 echo   PUBLICAR PROYECTO TIENDA EN GITHUB
 echo =====================================================
 echo Repositorio: %REPO_URL%
 echo Carpeta: %CD%
 echo.

where git >nul 2>&1
if errorlevel 1 (
  echo ERROR: Git no esta instalado o no esta en PATH.
  echo Instala Git desde https://git-scm.com/download/win y vuelve a ejecutar.
  pause
  exit /b 1
)

if not exist "pom.xml" (
  echo ERROR: No encuentro pom.xml.
  echo Ejecuta este BAT desde la carpeta tienda-integral-backend que contiene pom.xml.
  pause
  exit /b 1
)

if not exist ".git" (
  echo [1/6] Inicializando repositorio local...
  git init -b %BRANCH%
  if errorlevel 1 goto :error
) else (
  echo [1/6] Ya existe un repositorio Git local.
)

git branch --show-current | findstr /x "%BRANCH%" >nul
if errorlevel 1 (
  echo Cambiando/creando rama %BRANCH%...
  git switch -C %BRANCH%
  if errorlevel 1 goto :error
)

for /f "delims=" %%N in ('git config user.name') do set "GIT_NAME=%%N"
if not defined GIT_NAME (
  set /p "GIT_NAME=Escribe tu nombre para los commits: "
  git config user.name "%GIT_NAME%"
)
for /f "delims=" %%E in ('git config user.email') do set "GIT_EMAIL=%%E"
if not defined GIT_EMAIL (
  set /p "GIT_EMAIL=Escribe tu correo para los commits: "
  git config user.email "%GIT_EMAIL%"
)

 echo.
 echo [2/6] Revisando archivos que se van a publicar...
 git status --short
 echo.
 echo IMPORTANTE: revisa que no haya contrasenas, tokens ni archivos .env.
 choice /c SN /n /m "Continuar y preparar los archivos? [S/N] "
if errorlevel 2 exit /b 0

git add -A
if errorlevel 1 goto :error

git diff --cached --quiet
if not errorlevel 1 (
  echo No hay cambios nuevos para confirmar.
) else (
  echo [3/6] Creando commit inicial/de cambios...
  git commit -m "chore: preparar proyecto Tienda y evidencias GitHub"
  if errorlevel 1 goto :error
)

 echo.
 echo [4/6] Configurando repositorio remoto...
 git remote get-url origin >nul 2>&1
if errorlevel 1 (
  git remote add origin %REPO_URL%
) else (
  git remote set-url origin %REPO_URL%
)
if errorlevel 1 goto :error

 echo [5/6] Comprobando si GitHub ya tiene historial...
 git ls-remote --heads origin %BRANCH% > "%TEMP%\tienda_remote_branch.txt" 2>nul
if errorlevel 1 (
  echo No se pudo consultar GitHub. Revisa Internet y la autenticacion.
  del "%TEMP%\tienda_remote_branch.txt" >nul 2>&1
  goto :error
)
for %%F in ("%TEMP%\tienda_remote_branch.txt") do if %%~zF GTR 0 set "REMOTE_HAS_MAIN=1"
del "%TEMP%\tienda_remote_branch.txt" >nul 2>&1

if defined REMOTE_HAS_MAIN (
  echo GitHub ya contiene la rama main; no se sobrescribira.
  echo Intentando integrar el historial remoto con rebase...
  git pull --rebase origin %BRANCH%
  if errorlevel 1 (
    echo.
    echo ATENCION: no se pudo integrar automaticamente. Puede haber un conflicto.
    echo Resuelve los conflictos, ejecuta git add y git rebase --continue.
    echo Luego ejecuta: git push -u origin %BRANCH%
    pause
    exit /b 2
  )
)

 echo [6/6] Publicando en GitHub...
 git push -u origin %BRANCH%
if errorlevel 1 (
  echo.
  echo No se pudo publicar. Comprueba tu sesion de GitHub y los permisos.
  echo No se han usado tokens dentro de este archivo BAT.
  goto :error
)

 echo.
 echo =====================================================
 echo   PUBLICACION COMPLETADA
 echo =====================================================
 echo Repositorio: https://github.com/ronaldo1033/tienda
 echo Rama publicada: %BRANCH%
 echo.
 echo Siguiente paso: abre GitHub Actions y comprueba el workflow Maven.
 echo Para practicar ramas, PR, conflictos y Releases, sigue:
 echo docs\github\GIT-GUIA-PRACTICA.md
 start "" "https://github.com/ronaldo1033/tienda"
 pause
 exit /b 0

:error
 echo.
 echo ERROR: la operacion no se completo. Lee el mensaje anterior.
 pause
 exit /b 1
