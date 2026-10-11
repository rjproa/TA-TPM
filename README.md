# Yunta

App Android de control de gastos personales con un asesor al que se le habla o escribe.

El plan de construcción está en [PLAN-DE-FASES.md](PLAN-DE-FASES.md). Estado actual: fase 0 (base y esqueleto) terminada.

## Descargar la app

| Versión | Qué es | Enlace |
|---|---|---|
| Oficial | La última versión promovida a producción | [Descargar](https://github.com/rjproa/TA-TPM/releases/latest) |
| Candidata | Versiones en certificación, etiquetadas `vX.Y.Z-rc.N` | [Ver todas](https://github.com/rjproa/TA-TPM/releases) |
| Yunta Dev | La última compilación de una rama de trabajo | [Descargar](https://github.com/rjproa/TA-TPM/releases/download/dev-latest/yunta-dev.apk) |

![Código QR hacia la página de descargas](docs/descargas-qr.png)

Al abrir el archivo, Android pide permitir la instalación desde el navegador o el gestor de archivos. "Yunta Dev" se instala como una app aparte y convive con la oficial.

## Ramas y publicación

| Rama | Workflow | Qué ocurre en cada push |
|---|---|---|
| `feature/*` | [dev](.github/workflows/dev.yml) | Busca secretos, prueba, compila y publica "Yunta Dev" |
| `develop` | [cert](.github/workflows/cert.yml) | Revisa secretos, configuración y código; si todo pasa, compila, firma y publica una versión candidata |
| `main` | [prod](.github/workflows/prod.yml) | No compila: promueve a versión oficial el instalador ya certificado, previa aprobación manual |

Reglas del flujo:

- Todo cambio entra por una rama `feature/*` y un PR hacia `develop`. No hay cambios directos en `main`, ni siquiera correcciones urgentes: prod solo promueve código idéntico al que se certificó.
- Para publicar una versión oficial nueva hay que subir `yunta.versionName` en `gradle.properties`.
- La clave de firma no está en el repositorio. Vive en los secretos `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS` y `KEY_PASSWORD`. El archivo `app/debug.keystore` es una clave de pruebas compartida y no es secreta.
- La versión instalada se ve en la app, en Ajustes.

## Requisitos

- Android Studio 2026.1 o superior, con el SDK de Android 37.
- Un emulador o un teléfono con Android 8 o superior.

## Ejecutar

Abre la carpeta en Android Studio y pulsa Run, o desde la terminal:

```
./gradlew installDebug        # compila e instala "Yunta Dev" en el dispositivo conectado
./gradlew testDebugUnitTest   # ejecuta las pruebas unitarias
```

## Estructura

El código está en `app/src/main/java/com/yunta/app/`:

| Carpeta | Contenido |
|---|---|
| `domain/` | Modelos y cálculos, sin dependencias de Android |
| `data/` | Repositorios y fuentes de datos |
| `di/` | Contenedor que crea los objetos compartidos |
| `ui/` | Pantallas, ViewModels, navegación, componentes y tema |
