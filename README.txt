╔══════════════════════════════════════════════════════════════════════════════╗
║                                                                              ║
║                        EMBELLECEDOR - MANUAL DE USUARIO                      ║
║                           Versión 4.0 - JavaFX                               ║
║                        Autor: Ayur Sánchez Lozano                            ║
║                                                                              ║
╚══════════════════════════════════════════════════════════════════════════════╝


═══════════════════════════════════════════════════════════════════════════════
  ★ NUEVA VERSIÓN 4.0 - NOVEDADES ★
═══════════════════════════════════════════════════════════════════════════════

  🆕 NUEVAS CARACTERÍSTICAS:
  ──────────────────────────────────────────────────────────────────────────

  • CONTROL POR VOZ (NUI - NATURAL USER INTERFACE)
    ✓ Dicta texto directamente en el editor usando el micrófono.
    ✓ Ejecuta comandos por voz para una experiencia manos libres.
    ✓ Integración con el motor de reconocimiento de voz Vosk (español).
    ✓ Indicador visual en la barra de estado que muestra si el sistema está escuchando.

  • SISTEMA DE IMPORTACIÓN/EXPORTACIÓN MEJORADO
    ✓ Importa archivos de texto desde tu sistema
    ✓ Exporta con barra de progreso visual en tiempo real
    ✓ Preserva formatos (negrita, cursiva) al exportar en formato HTML
    ✓ Importa archivos con formato HTML y restaura los estilos automáticamente

  • INDICADOR DE PROGRESO PERSONALIZADO
    ✓ ProgressLabel: componente visual que muestra el progreso en porcentaje
    ✓ Feedback visual durante operaciones largas (abrir/exportar archivos)
    ✓ No congela la interfaz durante la carga de archivos grandes

  • PROCESAMIENTO ASÍNCRONO
    ✓ Operaciones de archivo en hilos separados usando Task
    ✓ La interfaz permanece responsive durante operaciones largas
    ✓ Actualización progresiva del contenido al cargar archivos

  🔧 MEJORAS TÉCNICAS:
  ──────────────────────────────────────────────────────────────────────────

  • Integración del motor de reconocimiento de voz Vosk.
  • Uso de RichTextFX (StyleClassedTextArea) para edición avanzada.
  • Arquitectura MVC mejorada con separación de modelos.
  • Componentes personalizados reutilizables (ProgressLabel).

  🎨 MEJORAS DE INTERFAZ:
  ──────────────────────────────────────────────────────────────────────────

  • Icono de micrófono en la barra de estado para indicar el estado del reconocimiento de voz.
  • Mensajes temporales informativos (guardado, copiado, atajos).
  • Iconos y etiquetas descriptivas en todos los botones.


═══════════════════════════════════════════════════════════════════════════════
  1. DESCRIPCIÓN GENERAL
═══════════════════════════════════════════════════════════════════════════════

  EMBELLECEDOR es un editor de texto enriquecido desarrollado en JavaFX que
  permite aplicar diversos estilos y transformaciones al texto. La aplicación
  cuenta con una interfaz intuitiva, capacidades de importación/exportación
  avanzadas y, en su última versión, control por voz para dictado y comandos.


═══════════════════════════════════════════════════════════════════════════════
  2. REQUISITOS DEL SISTEMA
═══════════════════════════════════════════════════════════════════════════════

  • Java 24 o superior
  • JavaFX 23.0.1
  • RichTextFX 0.11.6
  • Vosk API 0.3.21
  • Maven 3.x (para compilación)
  • Sistema operativo: Windows, Linux o macOS
  • Micrófono (para la funcionalidad de voz)
  • Mínimo 2GB de RAM recomendado


═══════════════════════════════════════════════════════════════════════════════
  3. INSTALACIÓN Y EJECUCIÓN
═══════════════════════════════════════════════════════════════════════════════

  OPCIÓN 1: Desde Maven (Recomendado)
  ────────────────────────────────────
  1. Abrir una terminal en el directorio del proyecto
  2. Ejecutar: mvn clean javafx:run

  OPCIÓN 2: Desde IDE (IntelliJ IDEA / Eclipse)
  ──────────────────────────────────────────────
  1. Importar el proyecto como proyecto Maven existente
  2. Esperar a que Maven descargue las dependencias
  3. Ejecutar la clase principal: org.group.practica_3.App

  NOTA: La primera vez que se ejecute, Maven descargará el modelo de Vosk,
        lo que puede tardar unos minutos.


═══════════════════════════════════════════════════════════════════════════════
  4. INTERFAZ DE USUARIO
═══════════════════════════════════════════════════════════════════════════════

  La aplicación presenta una ventana principal de 840x450 píxeles con:

  ┌─────────────────────────────────────────────────────────────────────────┐
  │  [Barra de herramientas superior]                                       │
  │   • Botones de formato, transformaciones y herramientas.                │
  │                                                                         │
  │  [Área de texto principal - StyleClassedTextArea]                       │
  │   • Editor de texto enriquecido con soporte de estilos visuales.        │
  │                                                                         │
  │  [Barra de estado inferior]                                             │
  │   • Contador dinámico (palabras/líneas/caracteres).                     │
  │   • Icono de micrófono para indicar el estado del control por voz.      │
  │   • Botones de utilidad y archivo.                                      │
  └─────────────────────────────────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════════════════════════
  5. FUNCIONALIDADES PRINCIPALES
═══════════════════════════════════════════════════════════════════════════════

  ┌───────────────────────────────────────────────────────────────────────┐
  │  5.1. CONTROL POR VOZ (NUI) (★ NUEVO)                                 │
  └───────────────────────────────────────────────────────────────────────┘

    La aplicación integra un sistema de reconocimiento de voz que permite
    tanto dictar texto como ejecutar comandos.

    • CÓMO FUNCIONA:
      1. La aplicación escucha continuamente a través del micrófono por defecto.
      2. Un icono en la barra de estado indica si el sistema está activo.
      3. El motor de Vosk procesa el audio en tiempo real.

    • DICTADO:
      - Simplemente habla y el texto aparecerá en el editor.
      - El sistema diferencia entre dictado y comandos. Las frases que no
        coinciden con un comando se interpretan como texto a escribir.

    • COMANDOS DE VOZ DISPONIBLES:
      - "abrir": Abre el selector de archivos.
      - "guardar": Guarda el estado actual del texto y formatos.
      - "exportar": Abre el diálogo para exportar a HTML.
      - "negrita": Aplica o quita el formato de negrita al texto seleccionado.
      - "cursiva": Aplica o quita el formato de cursiva al texto seleccionado.
      - "invertir" o "al revés": Invierte el orden de los caracteres del texto.
      - "espacios": Elimina los espacios dobles.
      - "mayúscula": Convierte el texto seleccionado a mayúsculas.
      - "minúscula": Convierte el texto seleccionado a minúsculas.
      - "limpiar": Borra todo el contenido del editor.
      - "copiar": Copia el texto seleccionado al portapapeles.
      - "reiniciar": Restaura el texto al último estado guardado.

  ┌───────────────────────────────────────────────────────────────────────┐
  │  5.2. GESTIÓN DE ARCHIVOS                                             │
  └───────────────────────────────────────────────────────────────────────┘

    • ABRIR ARCHIVO: Carga archivos .txt, aplicando estilos si contienen HTML.
    • EXPORTAR ARCHIVO: Guarda el texto y su formato (negrita/cursiva) en un archivo HTML.

  ┌───────────────────────────────────────────────────────────────────────┐
  │  5.3. FORMATO Y TRANSFORMACIÓN DE TEXTO                               │
  └───────────────────────────────────────────────────────────────────────┘

    • FORMATO: Aplica negrita [Ctrl+B] y cursiva [Ctrl+I] al texto seleccionado.
    • TRANSFORMACIONES: Convierte a mayúsculas/minúsculas, voltea el texto y elimina espacios dobles.

  ┌───────────────────────────────────────────────────────────────────────┐
  │  5.4. OTRAS HERRAMIENTAS                                              │
  └───────────────────────────────────────────────────────────────────────┘

    • BÚSQUEDA Y REEMPLAZO: Busca texto y lo reemplaza a través de una ventana dedicada.
    • CONTADORES: Muestra en tiempo real el número de palabras, líneas o caracteres.
    • UTILIDADES: Copia al portapapeles, limpia el editor, guarda el estado y reinicia al último guardado.


═══════════════════════════════════════════════════════════════════════════════
  6. ATAJOS DE TECLADO
═══════════════════════════════════════════════════════════════════════════════

    Ctrl + B ........... Aplicar/quitar negrita
    Ctrl + I ........... Aplicar/quitar cursiva
    (Y los atajos estándar del sistema como Ctrl+C, Ctrl+V, etc.)


═══════════════════════════════════════════════════════════════════════════════
  7. INFORMACIÓN TÉCNICA
═══════════════════════════════════════════════════════════════════════════════

  TECNOLOGÍAS UTILIZADAS:
  ──────────────────────────────────────────────────────────────────────────
  • JavaFX 23.0.1 - Framework de interfaz gráfica.
  • RichTextFX 0.11.6 - Componente avanzado para edición de texto.
  • Vosk API 0.3.21 - Motor de reconocimiento de voz.
  • Maven - Gestión de dependencias y construcción.
  • Java 24 - Lenguaje de programación.

  ARQUITECTURA DEL PROYECTO:
  ──────────────────────────────────────────────────────────────────────────
  
  /src/main/java/org/group/practica_3/
  ├── App.java                    - Clase principal, punto de entrada
  ├── MainController.java         - Controlador principal (lógica de UI)
  ├── ReemplazarTexto.java        - Controlador de la ventana de reemplazo
  ├── component/                  - Componentes de UI personalizados
  └── nui/                        - Lógica del control por voz (Natural User Interface)
      ├── NuiController.java      - Conecta los comandos de voz con las acciones
      ├── NuiSpeechService.java   - Gestiona el servicio de escucha con Vosk
      ├── NuiSpeechParser.java    - Interpreta el texto reconocido
      └── NuiCommand.java         - Define los comandos de voz disponibles

  /src/main/resources/org/group/practica_3/
  ├── principal.fxml              - Vista de la interfaz principal
  ├── model/vosk-model-small-es...- Modelo de lenguaje para español


═══════════════════════════════════════════════════════════════════════════════
  8. HISTORIAL DE VERSIONES
═══════════════════════════════════════════════════════════════════════════════

  VERSIÓN 4.0 (Diciembre 2025) - ACTUAL
  ──────────────────────────────────────────────────────────────────────────
  + Integración de control por voz (NUI) con motor Vosk.
  + Comandos de voz para todas las funciones principales.
  + Dictado de texto por voz.
  + Indicador de estado del micrófono en la UI.

  VERSIÓN 3.0 (Noviembre 2025)
  ──────────────────────────────────────────────────────────────────────────
  + Sistema de importación/exportación de archivos con barra de progreso.
  + Procesamiento asíncrono y parser HTML para preservar formatos.

  (Versiones anteriores omitidas por brevedad)


═══════════════════════════════════════════════════════════════════════════════
  9. CONTACTO Y SOPORTE
═══════════════════════════════════════════════════════════════════════════════

  Autor: Ayur Sánchez Lozano
  Proyecto académico: DAM 2 - Interfaces Gráficas - Práctica 3
  Documentación: Gemini CLI ♡

═══════════════════════════════════════════════════════════════════════════════

  Última actualización: Diciembre 2025
  Versión del manual: 4.0

═══════════════════════════════════════════════════════════════════════════════