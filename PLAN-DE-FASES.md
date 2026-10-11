# Yunta: plan de construcción por fases

Yunta es una app Android de control de gastos personales con un asesor al que se le habla o escribe. Este documento define en qué orden se construye, qué entrega cada fase, qué archivos se crean y qué se necesita tener listo antes de empezar cada una.

El plan incluye todo el alcance acordado y no está atado a fechas. Cada fase termina con una app instalable que hace algo más que la anterior.

## Resumen de fases

| Fase | Nombre | Lo que el usuario puede hacer al terminar | Cuentas nuevas |
|---|---|---|---|
| 0 | Base y esqueleto | Abrir la app y recorrer todas las secciones, aún con datos de ejemplo | GitHub |
| 1 | Movimientos | Registrar, editar, eliminar y consultar ingresos y gastos reales | Ninguna |
| 2 | Presupuesto | Saber cuánto le queda en necesidades, gustos y ahorro, y recibir alertas | Ninguna |
| 3 | Hábitos | Mantener una racha, recibir un recordatorio diario y automatizar gastos fijos | Ninguna |
| 4 | Dashboard | Ver gráficos de sus gastos por categoría y en el tiempo | Ninguna |
| 5 | Asesor Yunta | Chatear con Yunta por texto y registrar gastos desde el chat | Groq, Google AI Studio, OpenRouter |
| 6 | Voz | Dictar gastos, preguntar hablando y escuchar la respuesta | Ninguna |
| 7 | Cuenta y respaldo | Iniciar sesión y recuperar sus datos en otro teléfono | Firebase |
| 8 | Ranking entre amigos | Agregar amigos y comparar rachas | Ninguna |
| 9 | Cierre | Instalar la versión final; informe y anexos completos | Ninguna |

Para la entrega del 15 de octubre, el avance de la app es la fase 0.

## Decisiones técnicas fijas

| Tema | Decisión |
|---|---|
| Lenguaje e interfaz | Kotlin, Jetpack Compose con Material 3 |
| Arquitectura | MVVM: pantalla, ViewModel, Repository, fuente de datos |
| Datos locales | Room. Es la fuente principal; la app funciona sin internet |
| Nube | Firebase Authentication y Cloud Firestore, plan gratuito Spark |
| Red | Retrofit con conversor Gson y Coroutines |
| IA | Selector propio que prueba Groq, luego Gemini, luego OpenRouter, y si todos fallan responde sin IA |
| Voz | Reconocimiento y síntesis de voz incluidos en Android |
| Gráficos | Vico |
| Tareas programadas | WorkManager |
| Dependencias entre clases | Contenedor manual (`AppContainer`), sin librerías de inyección |
| Android | Mínimo Android 8 (API 26); compilación con SDK 37 |
| Entorno | Android Studio 2026.1.4, JDK 21 |
| Costo | Cero. Ningún servicio requiere tarjeta |

Versiones en uso desde la fase 0: plugin de Gradle 9.4.0, Gradle 9.6.0, Kotlin 2.2.10, Compose BOM 2026.09.00, Navigation 2.10.2 y Lifecycle 2.11.0. Las de Room (2.8.5 prevista), Retrofit, Vico y WorkManager se fijan en la fase que las incorpora. Todas quedan anotadas en `gradle/libs.versions.toml`.

## Convenciones

- **Paquete base:** `com.yunta.app`.
- **Rutas:** las rutas de archivos de este documento son relativas a `app/src/main/java/com/yunta/app/`, salvo que empiecen con `app/` o estén en la raíz del repositorio.
- **Nombres:** conceptos del negocio en español (`Movimiento`, `Categoria`, `Presupuesto`) y sufijos técnicos en inglés (`Screen`, `ViewModel`, `Repository`, `Dao`).
- **Ramas:** una rama `feature/...` por fase, que se integra a `develop`. `main` se actualiza en cada entrega al docente.
- **Claves:** las claves de IA van en `local.properties`, que no se sube al repositorio.
- **Cierre de fase:** la fase termina cuando se cumple su lista "Terminado cuando", la rama está integrada y hay capturas guardadas en `docs/evidencias/`.

## Estructura de carpetas

```
com/yunta/app/
├── MainActivity.kt
├── YuntaApplication.kt
├── di/              Contenedor de dependencias
├── domain/          Modelos y cálculos (presupuesto, racha, periodo)
├── data/
│   ├── local/       Room: entidades, DAO, base de datos
│   ├── remote/      Retrofit e IA, Firestore
│   └── repository/  Repositorios que usan los ViewModel
├── ui/
│   ├── theme/       Colores, tipografía, tema
│   ├── navigation/  Rutas y barra inferior
│   ├── components/  Componentes reutilizables
│   └── <sección>/   Una carpeta por sección, con Screen y ViewModel
├── voz/             Reconocimiento y lectura en voz alta
└── trabajo/         Tareas programadas y notificaciones
```

## Modelo de datos

| Tabla (Room) | Contenido | Fase |
|---|---|---|
| `categorias` | Nombre, icono, grupo (necesidad, gusto o ahorro), tipo (ingreso o gasto), visible | 1 |
| `movimientos` | Tipo, monto, categoría, fecha, medio de pago, nota, origen (manual, voz, chat, recurrente) | 1 |
| `ajustes` | Fila única: ingreso mensual, día de inicio del periodo, porcentajes, hora del recordatorio, lectura en voz alta | 2 |
| `dias_sin_gasto` | Días marcados como "hoy no gasté" | 3 |
| `gastos_recurrentes` | Monto, categoría, medio de pago, día del mes, activo, último mes aplicado | 3 |
| `mensajes_chat` | Autor (usuario o Yunta), texto, fecha | 5 |

En Firestore (fase 7) cada usuario tiene sus datos bajo `usuarios/{uid}/...` con las mismas colecciones. El perfil público para el ranking (fase 8) vive aparte en `perfiles/{uid}` y solo contiene nombre, código de amigo y rachas.

---

## Fase 0: Base y esqueleto

**Entregable:** app instalable con la identidad de Yunta y navegación completa entre todas las secciones, mostrando datos de ejemplo.

**Qué haremos**
- Crear el proyecto Android en el repositorio y configurar las dependencias base.
- Definir colores, tipografía y tema de Yunta, con modo claro y oscuro.
- Montar la navegación: barra inferior con Inicio, Historial, Dashboard, Yunta y Ajustes.
- Construir la pantalla de inicio y el formulario de movimiento con datos de ejemplo en memoria.
- Dejar las demás pantallas como marcadores con su título.

**Archivos que se crean**
- Raíz: `settings.gradle.kts`, `build.gradle.kts`, `gradle/libs.versions.toml`, `.gitignore`, `README.md`.
- `app/build.gradle.kts`, `app/src/main/AndroidManifest.xml`, `app/src/main/res/values/strings.xml`.
- `MainActivity.kt`, `YuntaApplication.kt`, `di/AppContainer.kt`.
- `ui/theme/Color.kt`, `ui/theme/Type.kt`, `ui/theme/Theme.kt`.
- `ui/navigation/Destinos.kt`, `ui/navigation/YuntaNavHost.kt`, `ui/navigation/BarraInferior.kt`.
- `ui/components/BotonPrimario.kt`, `ui/components/CampoMonto.kt`, `ui/components/TarjetaResumen.kt`.
- `ui/inicio/InicioScreen.kt`, `ui/inicio/InicioViewModel.kt`.
- `ui/movimientos/FormularioMovimientoScreen.kt`.
- `ui/historial/HistorialScreen.kt`, `ui/dashboard/DashboardScreen.kt`, `ui/asesor/AsesorScreen.kt`, `ui/ajustes/AjustesScreen.kt` (marcadores).
- `domain/model/Movimiento.kt`, `domain/model/Categoria.kt`, `data/DatosDeEjemplo.kt`.

**Qué necesitas tener**
- Cuenta de GitHub y el repositorio remoto creado, con los compañeros como colaboradores.
- Un teléfono Android con depuración USB activada, o el emulador.
- Instalar `adb` en el sistema (paquete `android-tools` en Arch) para instalar y probar desde la terminal.

**Terminado cuando**
- La app compila e instala en el emulador y en un teléfono.
- Se puede ir y volver entre todas las secciones sin cierres inesperados.
- El formulario valida monto vacío, cero o negativo.

**Informe:** secciones 1 a 4 (portada, introducción, análisis del problema, objetivos).

---

## Fase 1: Movimientos

**Entregable:** una libreta de gastos funcional. Los datos se guardan en el teléfono y sobreviven al cerrar la app.

**Requerimientos:** RF03, RF04, RF05, RF09.

**Qué haremos**
- Crear la base de datos Room con categorías y movimientos.
- Cargar las categorías predefinidas la primera vez que se abre la app.
- Registrar, editar y eliminar ingresos y gastos, con confirmación al eliminar.
- Historial agrupado por día con filtros por fecha, categoría y medio de pago.
- Gestión de categorías: crear, editar, ocultar y asignar grupo.
- Reemplazar los datos de ejemplo de la fase 0 por datos reales.

**Archivos que se crean**
- `data/local/YuntaDatabase.kt`, `data/local/Convertidores.kt`.
- `data/local/entity/CategoriaEntity.kt`, `data/local/entity/MovimientoEntity.kt`.
- `data/local/dao/CategoriaDao.kt`, `data/local/dao/MovimientoDao.kt`.
- `data/local/CategoriasIniciales.kt`.
- `data/repository/MovimientoRepository.kt`, `data/repository/CategoriaRepository.kt`.
- `domain/model/MedioPago.kt`, `domain/model/TipoMovimiento.kt`, `domain/model/GrupoPresupuesto.kt`.
- `ui/movimientos/FormularioMovimientoViewModel.kt`.
- `ui/historial/HistorialViewModel.kt`, `ui/historial/FiltrosHistorial.kt`.
- `ui/categorias/CategoriasScreen.kt`, `ui/categorias/CategoriasViewModel.kt`, `ui/categorias/FormularioCategoriaDialog.kt`.
- `ui/components/SelectorCategoria.kt`, `ui/components/SelectorFecha.kt`, `ui/components/DialogoConfirmacion.kt`.

**Qué necesitas tener:** nada nuevo.

**Terminado cuando**
- Un movimiento registrado aparece en el historial tras cerrar y reabrir la app.
- Editar y eliminar actualizan la lista al instante.
- Los tres filtros funcionan solos y combinados.
- Ocultar una categoría la quita del formulario sin borrar sus movimientos.

**Informe:** sección 5, modelo de datos.

---

## Fase 2: Presupuesto

**Entregable:** la app deja de ser una libreta y pasa a decir cuánto se puede gastar.

**Requerimientos:** RF07, RF08, RF10, RF11.

**Qué haremos**
- Asistente de primera vez: ingreso mensual, día de cobro y porcentajes (50/30/20 por defecto).
- Cálculo del periodo según el día de inicio elegido.
- Cálculo de lo asignado, gastado y disponible por grupo.
- Pantalla de inicio real: resumen del periodo con barras de avance por grupo.
- Alertas dentro de la app al llegar al 80 % y al 100 % de un grupo.
- Pruebas automáticas de los cálculos.

**Archivos que se crean**
- `data/local/entity/AjustesEntity.kt`, `data/local/dao/AjustesDao.kt`, `data/repository/AjustesRepository.kt`.
- `domain/Periodo.kt`, `domain/CalculadoraPeriodo.kt`, `domain/CalculadoraPresupuesto.kt`, `domain/model/EstadoPresupuesto.kt`.
- `ui/bienvenida/BienvenidaScreen.kt`, `ui/bienvenida/BienvenidaViewModel.kt`.
- `ui/presupuesto/PresupuestoScreen.kt`, `ui/presupuesto/PresupuestoViewModel.kt`.
- `ui/components/BarraGrupo.kt`, `ui/components/AvisoLimite.kt`.
- `app/src/test/java/com/yunta/app/domain/CalculadoraPresupuestoTest.kt`, `CalculadoraPeriodoTest.kt`.

**Qué necesitas tener:** nada nuevo.

**Terminado cuando**
- Con ingreso de 2 000 soles, la app asigna 1 000, 600 y 400 a los tres grupos.
- Cambiar los porcentajes recalcula el resumen; no se aceptan porcentajes que no sumen 100.
- Con día de inicio 30, un gasto del 29 cae en el periodo anterior y uno del 30 en el actual.
- Las alertas aparecen al cruzar el 80 % y el 100 %.
- Las pruebas automáticas pasan.

**Informe:** sección 5, arquitectura y flujos principales.

---

## Fase 3: Hábitos

**Entregable:** la app ayuda a mantener la constancia: racha, recordatorio y gastos fijos automáticos.

**Requerimientos:** RF06, RF20, RF21.

**Qué haremos**
- Racha de días consecutivos con al menos un movimiento o con "hoy no gasté".
- Contador de racha y mejor racha en la pantalla de inicio.
- Recordatorio diario por notificación a una hora configurable.
- Gastos recurrentes: definirlos una vez y que se registren solos cada mes.

**Archivos que se crean**
- `data/local/entity/DiaSinGastoEntity.kt`, `data/local/dao/DiaSinGastoDao.kt`.
- `data/local/entity/GastoRecurrenteEntity.kt`, `data/local/dao/GastoRecurrenteDao.kt`.
- `data/repository/RachaRepository.kt`, `data/repository/GastoRecurrenteRepository.kt`.
- `domain/CalculadoraRacha.kt`, `domain/model/Racha.kt`.
- `trabajo/RecordatorioWorker.kt`, `trabajo/RecurrentesWorker.kt`, `trabajo/Notificaciones.kt`, `trabajo/ProgramadorTareas.kt`.
- `ui/components/TarjetaRacha.kt`.
- `ui/recurrentes/RecurrentesScreen.kt`, `ui/recurrentes/RecurrentesViewModel.kt`.
- `ui/ajustes/AjustesViewModel.kt` (hora del recordatorio).
- `app/src/test/java/com/yunta/app/domain/CalculadoraRachaTest.kt`.

**Qué necesitas tener**
- Un teléfono físico para probar notificaciones; en Android 13 o superior la app pedirá permiso para mostrarlas.

**Terminado cuando**
- Registrar tres días seguidos muestra racha de 3; saltarse un día la reinicia.
- "Hoy no gasté" mantiene la racha sin crear un movimiento.
- La notificación llega a la hora configurada y no llega si ya se registró ese día.
- Un gasto recurrente se registra una sola vez por mes, aunque la app se abra varias veces.

**Informe:** sección 3, historias de usuario de racha y recurrentes.

---

## Fase 4: Dashboard

**Entregable:** el usuario entiende sus gastos de un vistazo con gráficos.

**Requerimientos:** RF12, RF13.

**Qué haremos**
- Gráfico de gasto por categoría del periodo.
- Gráfico de evolución del gasto por día o semana.
- Comparación con periodos anteriores.
- Selector de periodo y estado vacío cuando no hay datos.

**Archivos que se crean**
- `data/local/dao/ResumenDao.kt` (consultas agregadas).
- `data/repository/ResumenRepository.kt`.
- `domain/model/GastoPorCategoria.kt`, `domain/model/PuntoEvolucion.kt`.
- `ui/dashboard/DashboardViewModel.kt`.
- `ui/dashboard/GraficoCategorias.kt`, `ui/dashboard/GraficoEvolucion.kt`, `ui/dashboard/ComparacionPeriodos.kt`.
- `ui/components/SelectorPeriodo.kt`, `ui/components/EstadoVacio.kt`.

**Qué necesitas tener:** nada nuevo. Conviene cargar datos de varios meses para probar.

**Terminado cuando**
- Los totales de los gráficos coinciden con la suma del historial filtrado por el mismo periodo.
- Cambiar de periodo actualiza los tres bloques.
- Sin movimientos se muestra un mensaje, no un gráfico roto.

**Informe:** sección 5, diseño de interfaces y mapa de navegación.

---

## Fase 5: Asesor Yunta

**Entregable:** el usuario conversa con Yunta por texto, recibe consejos basados en sus números y puede registrar gastos desde el chat.

**Requerimientos:** RF15, RF16, RF17, RF19.

**Qué haremos**
- Conectar los tres proveedores de IA con Retrofit.
- Construir el selector: prueba los proveedores en orden, salta al siguiente ante límite alcanzado o error, y marca al agotado hasta el día siguiente.
- Armar el contexto financiero con cálculos de la app (disponible por grupo, gastado por categoría, días restantes).
- Definir la personalidad de Yunta: trato de tú, directo y con humor.
- Interpretar frases como "gasté 30 soles en comida" y mostrar una tarjeta de confirmación antes de guardar.
- Guardar el historial del chat en el teléfono y permitir borrarlo.
- Respuesta de respaldo sin IA cuando todos los proveedores fallan.

**Archivos que se crean**
- `data/remote/ia/ChatCompletionsApi.kt` (Groq y OpenRouter comparten formato), `data/remote/ia/GeminiApi.kt`.
- `data/remote/ia/dto/ChatCompletionsDto.kt`, `data/remote/ia/dto/GeminiDto.kt`.
- `data/remote/ia/ProveedorIA.kt`, `ProveedorGroq.kt`, `ProveedorGemini.kt`, `ProveedorOpenRouter.kt`.
- `data/remote/ia/SelectorIA.kt`, `data/remote/ia/EstadoProveedores.kt`, `data/remote/ia/ClienteRetrofit.kt`.
- `domain/asesor/ContextoFinanciero.kt`, `domain/asesor/ConstructorContexto.kt`, `domain/asesor/PersonalidadYunta.kt`.
- `domain/asesor/InterpreteMensaje.kt`, `domain/asesor/model/Intencion.kt`, `domain/asesor/RespuestaSinIA.kt`.
- `data/local/entity/MensajeChatEntity.kt`, `data/local/dao/MensajeChatDao.kt`, `data/repository/AsesorRepository.kt`.
- `ui/asesor/AsesorViewModel.kt`, `ui/asesor/BurbujaMensaje.kt`, `ui/asesor/TarjetaConfirmacionGasto.kt`.
- `app/src/test/java/com/yunta/app/data/remote/ia/SelectorIATest.kt`.
- Cambios: permiso de internet en `AndroidManifest.xml`; lectura de claves en `app/build.gradle.kts`.

**Qué necesitas tener**
- Cuenta en Groq y una clave de API (console.groq.com).
- Clave de API de Gemini desde Google AI Studio (aistudio.google.com), con tu cuenta de Google.
- Cuenta en OpenRouter y una clave de API (openrouter.ai).
- Las tres claves en `local.properties` como `GROQ_API_KEY`, `GEMINI_API_KEY` y `OPENROUTER_API_KEY`.
- Ninguna de las tres pide tarjeta. Los límites gratuitos cambian; se revisan en cada consola antes de la exposición.

**Terminado cuando**
- "¿Puedo comprarme un polo de 60 soles?" recibe una respuesta coherente con el disponible en gustos.
- "Gasté 30 soles en comida" muestra la confirmación y, al aceptar, el movimiento aparece en el historial.
- Con la clave del primer proveedor inválida, la respuesta llega desde el segundo.
- Sin internet, la app responde con el cálculo sin redacción y no se cierra.
- El historial del chat persiste y se puede borrar.

**Informe:** sección 6, APIs y servicios; requerimientos no funcionales de privacidad.

---

## Fase 6: Voz

**Entregable:** todo lo que se hace escribiendo en el chat se puede hacer hablando, y Yunta responde en voz alta.

**Requerimientos:** RF14, RF18, y la parte de voz de RF16.

**Qué haremos**
- Botón de micrófono en la pantalla de inicio para dictar un gasto rápido.
- Botón de micrófono en el chat para preguntar hablando.
- Mostrar el texto reconocido antes de enviarlo.
- Leer en voz alta las respuestas de Yunta, con opción de silenciar.
- Manejar el permiso de micrófono y los casos de error (sin permiso, no se entendió, sin servicio de voz).

**Archivos que se crean**
- `voz/ReconocedorVoz.kt`, `voz/LectorVoz.kt`, `voz/EstadoVoz.kt`.
- `ui/voz/BotonMicrofono.kt`, `ui/voz/DictadoGastoSheet.kt`, `ui/voz/DictadoViewModel.kt`.
- `ui/components/SolicitudPermiso.kt`.
- Cambios: permiso de micrófono en `AndroidManifest.xml`; `AsesorScreen.kt` y `InicioScreen.kt` incorporan el micrófono.

**Qué necesitas tener**
- Teléfono físico con la app de Google y el idioma español instalado para voz. El micrófono del emulador no es fiable.
- Conexión a internet durante las pruebas.

**Terminado cuando**
- Decir "hoy gasté 30 soles en comida" abre la confirmación con monto, categoría y fecha correctos.
- Una pregunta hablada recibe respuesta escrita y leída en voz alta.
- Negar el permiso de micrófono muestra una explicación y la app sigue funcionando por texto.
- El interruptor de silencio detiene la lectura.

**Informe:** sección 7, funcionalidades; manual de usuario, apartado de voz.

---

## Fase 7: Cuenta y respaldo

**Entregable:** el usuario tiene una cuenta y no pierde sus datos si cambia de teléfono.

**Requerimientos:** RF01, RF02, RF23.

**Qué haremos**
- Registro, inicio de sesión, recuperación de contraseña y cierre de sesión con correo.
- La app exige sesión para entrar; los datos existentes se asocian a la cuenta.
- Respaldo: cada cambio local se sube a Firestore cuando hay conexión.
- Restauración: al iniciar sesión en un teléfono sin datos, se descargan los de la nube.
- Reglas de seguridad para que cada usuario solo lea y escriba lo suyo.

**Archivos que se crean**
- `app/google-services.json` (descargado de Firebase; no se sube al repositorio).
- `firestore.rules` en la raíz.
- `data/remote/firebase/AuthDataSource.kt`, `data/remote/firebase/FirestoreDataSource.kt`, `data/remote/firebase/Mapeadores.kt`.
- `data/repository/AuthRepository.kt`, `data/repository/RespaldoRepository.kt`.
- `trabajo/RespaldoWorker.kt`.
- `ui/cuenta/LoginScreen.kt`, `ui/cuenta/RegistroScreen.kt`, `ui/cuenta/RecuperarScreen.kt`, `ui/cuenta/CuentaViewModel.kt`.
- Cambios: plugin de Google Services en los archivos Gradle; columna de estado de sincronización en las entidades, con migración de la base de datos; `YuntaNavHost.kt` separa el flujo con y sin sesión.

**Qué necesitas tener**
- Proyecto en Firebase (console.firebase.google.com) con tu cuenta de Google, en plan Spark.
- App Android registrada en ese proyecto con el paquete `com.yunta.app`.
- Authentication activado con el método correo y contraseña.
- Firestore creado en modo producción, con las reglas de `firestore.rules` publicadas.
- Compartir `google-services.json` con el grupo por un canal privado.

**Terminado cuando**
- Se puede crear una cuenta, cerrar sesión y volver a entrar.
- El correo de recuperación de contraseña llega.
- Tras instalar la app en un segundo teléfono e iniciar sesión, aparecen los mismos movimientos, categorías y ajustes.
- Un movimiento creado sin internet se sube solo al recuperar la conexión.
- Con las reglas publicadas, una cuenta no puede leer los datos de otra.

**Informe:** sección 6, base de datos y servicios; requerimientos no funcionales de seguridad.

---

## Fase 8: Ranking entre amigos

**Entregable:** el usuario agrega amigos con un código y compara rachas, sin exponer montos.

**Requerimientos:** RF22.

**Qué haremos**
- Perfil público con nombre visible, código de amigo, racha actual y mejor racha.
- Agregar amigos ingresando su código; eliminar amigos.
- Ranking ordenado por racha actual, actualizado en vivo.
- Publicar la racha en la nube cada vez que cambia.

**Archivos que se crean**
- `data/remote/firebase/PerfilDataSource.kt`, `data/remote/firebase/AmigosDataSource.kt`.
- `data/repository/AmigosRepository.kt`.
- `domain/model/PerfilPublico.kt`, `domain/GeneradorCodigoAmigo.kt`.
- `ui/amigos/RankingScreen.kt`, `ui/amigos/AmigosViewModel.kt`, `ui/amigos/AgregarAmigoDialog.kt`, `ui/amigos/MiCodigoCard.kt`.
- Cambios: `firestore.rules` añade las reglas de `perfiles`; `Destinos.kt` añade la sección.

**Qué necesitas tener**
- Dos cuentas de prueba y, de preferencia, dos teléfonos.

**Terminado cuando**
- Ingresar el código de otra cuenta la agrega a la lista; un código inexistente muestra un error claro.
- El ranking se reordena cuando cambia la racha de un amigo.
- En Firestore, el perfil público no contiene montos ni movimientos.

**Informe:** sección 7, funcionalidades.

---

## Fase 9: Cierre

**Entregable:** versión final instalable, informe completo y material de exposición.

**Qué haremos**
- Ejecutar el plan de pruebas completo: funcionales, de interfaz, de navegación, de datos y en dispositivos.
- Corregir los errores encontrados y pulir textos, estados vacíos y accesibilidad.
- Generar el instalador y publicarlo en GitHub Releases.
- Reunir capturas, diagramas y código relevante para los anexos.

**Archivos que se crean**
- `docs/pruebas/plan-de-pruebas.md`, `docs/pruebas/resultados.md`.
- `docs/evidencias/` (capturas por funcionalidad).
- `docs/manual-de-usuario.md`.
- `docs/diagramas/` (arquitectura, modelo de datos, mapa de navegación).
- `README.md` actualizado con instalación y configuración de claves.
- Instalador `yunta-v1.0.apk` adjunto a una versión en GitHub Releases.

**Qué necesitas tener**
- Al menos dos teléfonos con versiones distintas de Android para las pruebas en dispositivos.
- Revisar en las consolas de Groq, Google AI Studio, OpenRouter y Firebase que las cuotas gratuitas estén disponibles el día de la exposición.

**Terminado cuando**
- Todas las pruebas del plan tienen resultado registrado.
- El instalador se descarga desde GitHub y funciona en un teléfono que nunca tuvo la app.
- Los cinco anexos del esquema están completos.

**Informe:** secciones 8 a 12 (pruebas, resultados, conclusiones, referencias, anexos).

---

## Cuentas y servicios

| Servicio | Para qué | Cuándo | Costo |
|---|---|---|---|
| GitHub | Repositorio, colaboración e instaladores | Fase 0 | Gratis |
| Groq | Proveedor principal de IA | Fase 5 | Gratis, sin tarjeta |
| Google AI Studio | Gemini, segundo proveedor de IA | Fase 5 | Gratis, sin tarjeta |
| OpenRouter | Tercer proveedor de IA | Fase 5 | Gratis, sin tarjeta |
| Firebase | Inicio de sesión y respaldo en la nube | Fase 7 | Gratis, plan Spark |

## Requerimientos funcionales

| Código | Requerimiento | Fase |
|---|---|---|
| RF01 | Registrarse e iniciar sesión con correo y contraseña | 7 |
| RF02 | Recuperar contraseña y cerrar sesión | 7 |
| RF03 | Registrar ingresos y gastos con monto, categoría, fecha, medio de pago y nota | 1 |
| RF04 | Editar y eliminar movimientos | 1 |
| RF05 | Consultar el historial con filtros | 1 |
| RF06 | Gastos fijos recurrentes | 3 |
| RF07 | Configurar ingreso mensual y día de inicio del periodo | 2 |
| RF08 | Regla 50/30/20 editable | 2 |
| RF09 | Categorías predefinidas y personalizables | 1 |
| RF10 | Alertas al 80 % y 100 % | 2 |
| RF11 | Resumen del periodo | 2 |
| RF12 | Gráfico de gasto por categoría | 4 |
| RF13 | Evolución en el tiempo y comparación entre periodos | 4 |
| RF14 | Dictar un gasto por voz | 6 |
| RF15 | Confirmar antes de guardar lo creado por voz o chat | 5 |
| RF16 | Consultar a Yunta por texto o voz | 5 y 6 |
| RF17 | Registrar un gasto desde el chat | 5 |
| RF18 | Leer en voz alta las respuestas | 6 |
| RF19 | Historial del chat en el teléfono | 5 |
| RF20 | Racha de registro diario | 3 |
| RF21 | Recordatorio diario | 3 |
| RF22 | Ranking de rachas entre amigos | 8 |
| RF23 | Respaldo y restauración en la nube | 7 |

## Fuera del alcance

- Mapa y geolocalización, salvo que el docente los exija.
- Activación por palabra clave ("oye Yunta").
- Varias monedas, metas de ahorro y conexión con bancos.
- Fotos de comprobantes: el almacenamiento de archivos de Firebase ya no es gratuito.
- Publicación en Play Store.
