# Yunta

App Android de control de gastos personales con un asesor al que se le habla o escribe.

El plan de construcción está en [PLAN-DE-FASES.md](PLAN-DE-FASES.md). Estado actual: fase 0 (base y esqueleto).

## Requisitos

- Android Studio 2026.1 o superior, con el SDK de Android 37.
- Un emulador o un teléfono con Android 8 o superior.

## Ejecutar

Abre la carpeta en Android Studio y pulsa Run, o desde la terminal:

```
./gradlew installDebug        # compila e instala en el dispositivo conectado
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
