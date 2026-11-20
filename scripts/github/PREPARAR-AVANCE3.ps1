# Ejecutar desde la carpeta donde se extrajo el ZIP.
# Clona el remoto, copia los archivos del proyecto (sin .git) y crea una rama de trabajo.
$ErrorActionPreference = 'Stop'
$repoUrl = 'https://github.com/ronaldo1033/tienda.git'
$source = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)
$destination = Join-Path (Split-Path -Parent $source) 'tienda-github-trabajo'

if (-not (Get-Command git -ErrorAction SilentlyContinue)) { throw 'Git no estÃ¡ instalado. InstÃ¡lalo desde https://git-scm.com/download/win.' }
if (-not (Test-Path (Join-Path $source 'pom.xml'))) { throw 'Ejecuta este script desde el ZIP extraÃ­do: no se encontrÃ³ pom.xml.' }
if (Test-Path $destination) { throw "Ya existe $destination. No se sobrescribirÃ¡. RenÃ³mbrala o elimÃ­nala conscientemente y vuelve a ejecutar." }

git clone $repoUrl $destination
if ($LASTEXITCODE -ne 0) { throw 'FallÃ³ el clone. Revisa conexiÃ³n, URL y permisos.' }
Set-Location $destination
$branch = 'feature/avance-1-2-3'
git switch -c $branch
if ($LASTEXITCODE -ne 0) { throw 'No se pudo crear la rama de trabajo.' }

# Copia contenido actualizado, excluyendo metadatos Git, secretos y artefactos compilados.
robocopy $source $destination /E /XD .git target .idea .vscode /XF .env *.log
$code = $LASTEXITCODE
if ($code -ge 8) { throw "Robocopy reportÃ³ error $code." }

Write-Host ''
Write-Host 'Copia preparada. No se hizo commit ni push.' -ForegroundColor Green
Write-Host "Carpeta: $destination"
Write-Host "Rama: $branch"
Write-Host 'Siguiente: git status; revisar cambios; ejecutar mvn verify; luego git add, commit y push.'

