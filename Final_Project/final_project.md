Proyecto Final: "Plataforma de Analíticas de Rendimiento Académico"

### 1. Contexto del Proyecto (El Porqué)

Nuestra "Academia Digital" está creciendo exponencialmente. Tenemos miles de estudiantes tomando cursos, viendo
lecciones, presentando exámenes y participando en foros. Sin embargo, toda esta actividad genera una cantidad masiva de
datos crudos que no estamos aprovechando.

Los instructores y administradores están "ciegos". No pueden responder preguntas básicas como:

* Qué estudiantes están en riesgo de abandonar un curso?

* Cuál es la calificación promedio de un curso en tiempo real?

* Qué estudiantes están muy activos pero tienen bajo rendimiento?

**El objetivo de este proyecto** es construir un sistema de back-end (basado en microservicios) capaz de ingerir todos
estos eventos crudos y transformarlos en un reporte de analíticas de rendimiento accionable para un curso específico.

---

### 2. Arquitectura y Requerimientos Técnicos (El Cómo)

Construirás un sistema compuesto por **4 microservicios** independientes.

1. **`servicio-estudiantes`**: Gestiona la información base de los estudiantes (CRUD).

2. **`servicio-cursos`**: Gestiona el catálogo de cursos y las inscripciones (CRUD).

3. **`servicio-progreso`**: Actúa como un "registrador de eventos". Almacena cada interacción del estudiante (datos
   crudos).

4. **`servicio-reportes`**: El cerebro. Orquesta a los otros servicios y **procesa los datos crudos** para generar el
   reporte final.

#### Requerimientos Técnicos Obligatorios:

* **Lenguaje:** Java 17+ y Spring Boot 3+.

* **Perfiles (Profiles):** El código debe funcionar con dos perfiles:

    * `dev`: Configurado para usar una base de datos **MySQL**.

    * `prod`: Configurado para usar una base de datos **PostgreSQL**.

* **Comunicación:** Los servicios deben comunicarse usando **Feign Client** (preferido) o `RestTemplate`.

* **Patrón DTO:** Obligatorio! Ninguna entidad (`@Entity`) debe ser expuesta fuera de la capa de servicio/controlador.
  Todo lo que entra y sale de la API deben ser DTOs.

* **Validación:** Los DTOs de entrada (Ej: `CrearEstudianteDto`) deben usar **Jakarta Validation** (`@Valid`,
  `@NotBlank`, `@Email`, `@Size`, etc.).

* **Manejo de Errores:** Cada servicio debe tener un manejador de excepciones global (`@RestControllerAdvice`) que
  capture errores (como `EstudianteNotFoundException` o `MethodArgumentNotValidException`) y devuelva un DTO de error
  estandarizado (`ErrorResponseDto`).

* **No "Magic Strings":** No debe haber strings "mágicos" en el código. Usen clases de constantes (Ej: `ApiConstants`,
  `EventTypes`) para rutas de API, nombres de perfiles, tipos de eventos, etc.

* **Logs y Trazabilidad:**

    * Implementar logging (SLF4J) en puntos clave.

    * Implementar **MDC** con un `traceId` (pueden usar `spring-cloud-sleuth` o un `Filter` manual) para poder seguir
      una petición a través de todos los microservicios.

* **Documentación:** Cada servicio debe exponer su documentación de API usando **OpenAPI (swagger)**.

---

### 3. Modelo de Datos y Contratos de API (El Qué)

A continuación se definen las entidades mínimas y los endpoints clave que cada servicio debe exponer.

#### A. `servicio-estudiantes`

* **Responsabilidad:** CRUD de Estudiantes.

* **Entidad (`Estudiante`):**

    * `Long id` (PK)
    * `String nombre`
    * `String apellido`
    * `String email` (Único)
    * `LocalDateTime fechaCreacion`

* **Contrato de API (Endpoints):**

    * `POST /api/v1/estudiantes`
        * Recibe: `CrearEstudianteDto`
        * Devuelve: `EstudianteDto`

    * `GET /api/v1/estudiantes/{id}`
        * Devuelve: `EstudianteDto`

    * `GET /api/v1/estudiantes`
        * Devuelve: `List<EstudianteDto>`

#### B. `servicio-cursos`

* **Responsabilidad:** CRUD de Cursos y gestión de inscripciones.

* **Entidades (2):**
    1. **`Curso`**:
        * `Long id` (PK)
        * `String titulo`
        * `String descripcion`

    2. **`Inscripcion`**:
        * `Long id` (PK)
        * `Long cursoId`
        * `Long estudianteId`
        * `LocalDateTime fechaInscripcion`

* **Contrato de API (Endpoints):**

    * `POST /api/v1/cursos`
        * Recibe: `CrearCursoDto`
        * Devuelve: `CursoDto`

    * `GET /api/v1/cursos/{id}`
        * Devuelve: `CursoDto`

    * `POST /api/v1/inscripciones`
        * Recibe: `InscripcionDto` (Ej: `{ "cursoId": 1, "estudianteId": 5 }`)
        * Devuelve: `InscripcionDto`

    * **`GET /api/v1/cursos/{cursoId}/estudiantes`**
        * Devuelve: `List<Long>` (Una lista de los IDs de estudiantes inscritos en ese curso).

#### C. `servicio-progreso`

* **Responsabilidad:** Almacenar *todos* los eventos de interacción (datos crudos).

* **Entidad (`EventoProgreso`):**
    * `Long id` (PK)
    * `Long cursoId`
    * `Long estudianteId`
    * `String tipoEvento` (Ej: "LOGIN", "LECCION_VISTA", "EXAMEN_ENTREGADO", "FORO_POST")
    * `String valor` (Opcional. Ej: "uuid-leccion-abc" o la nota "8.5")
    * `LocalDateTime timestamp`

* **Contrato de API (Endpoints):**

    * `POST /api/v1/progreso`
        * Recibe: `CrearEventoDto` (Debe contener todos los campos de la entidad, excepto `id`).
        * Devuelve: `EventoProgresoDto`

    * **`GET /api/v1/progreso/curso/{cursoId}/eventos`**
        * Devuelve: `List<EventoProgresoDto>` (Devuelve **todos** los eventos crudos para un curso).

#### D. `servicio-reportes`

* **Responsabilidad:** Orquestar y procesar los datos para generar el reporte.

* **Base de Datos:** Este servicio *no* necesita una base de datos propia para este ejercicio (o puede tener una solo
  para cachear reportes, si lo desean como bonus).

* **Contrato de API (El Endpoint Clave):**

    * **`GET /api/v1/reportes/curso/{cursoId}/analitica`**
        * Devuelve: `ReporteAnaliticaDto` (Un DTO complejo que contiene el resultado del análisis).

---

### 4. El Desafío Central: El "Tratamiento de Datos"

El verdadero reto está en el `servicio-reportes`. Cuando recibe la petición
`GET /api/v1/reportes/curso/{cursoId}/analitica`, debe ejecutar el siguiente flujo:

1. **Orquestar (Feign):** Llamar a `servicio-cursos` (`.../curso/{cursoId}/estudiantes`) para obtener la lista de IDs de
   estudiantes.

2. **Orquestar (Feign):** Llamar a `servicio-progreso` (`.../progreso/curso/{cursoId}/eventos`) para obtener la lista
   gigante de **todos los eventos crudos**.

3. **Procesar:** Aquí está el núcleo del trabajo. Deben usar Streams o bucles, para procesar la lista de eventos crudos
   en memoria y calcular, *por cada estudiante*, las siguientes métricas:

    * **Promedio de Calificaciones:** (Filtrar eventos `EXAMEN_ENTREGADO`, parsear el `valor` a `Double`, y calcular el
      `average()`).

    * **Total de Lecciones Vistas:** (Contar eventos `LECCION_VISTA`).

    * **Participación en Foros:** (Contar eventos `FORO_POST`).

    * **Días desde el Último Login:** (Filtrar `LOGIN`, encontrar el `max(timestamp)`, y calcular los días hasta hoy
      usando `ChronoUnit.DAYS.between`).

4. **Inferir (Lógica de Negocio):** Basado en las métricas calculadas, deben asignar un `estadoRendimiento` a cada
   estudiante (Ej: `EN_RIESGO_POR_INACTIVIDAD` si `diasDesdeUltimoLogin > 20`, `BAJO_RENDIMIENTO` si `promedio < 5`,
   etc.).

5. **Enriquecer (Feign):** Ahora que tienen las métricas por `estudianteId`, deben volver a llamar (en un bucle) a
   `servicio-estudiantes` (`.../estudiantes/{id}`) para obtener el nombre y email de cada estudiante.

6. **Ensamblar y Devolver:** Construir el `ReporteAnaliticaDto` final con la información del curso y la lista de
   estudiantes con sus métricas calculadas.

---

### 5. Manual de Pruebas

Dado que no hay login ni front-end, deberán usar un cliente API (como **Postman** o `curl`) para cargar datos de prueba
y verificar su reporte.

**Flujo de Pruebas Sugerido:**

1. **Crear 2-3 Estudiantes:**
    * `POST /api/v1/estudiantes` (Body:
      `{ "nombre": "Cesar", "apellido": "Fernandez", "email": "cfernandez@cesarlead.com" }`)

2. **Crear 1 Curso:**
    * `POST /api/v1/cursos` (Body: `{ "titulo": "Curso de Spring Boot", ... }`)

3. **Inscribir a los Estudiantes al Curso:**
    * `POST /api/v1/inscripciones` (Body: `{ "cursoId": 1, "estudianteId": 1 }`)
    * `POST /api/v1/inscripciones` (Body: `{ "cursoId": 1, "estudianteId": 2 }`)

4. **Simular Actividad! (El paso más importante):**
    * `POST /api/v1/progreso` (Body: `{ "cursoId": 1, "estudianteId": 1, "tipoEvento": "LOGIN", "timestamp": "..." }`)
    * `POST /api/v1/progreso` (Body:
      `{ "cursoId": 1, "estudianteId": 1, "tipoEvento": "LECCION_VISTA", "valor": "lec-01-intro" ... }`)
    * `POST /api/v1/progreso` (Body:
      `{ "cursoId": 1, "estudianteId": 1, "tipoEvento": "EXAMEN_ENTREGADO", "valor": "8.5" ... }`)
    * `POST /api/v1/progreso` (Body: `{ "cursoId": 1, "estudianteId": 1, "tipoEvento": "FORO_POST", ... }`)
    * `POST /api/v1/progreso` (Body: `{ "cursoId": 1, "estudianteId": 2, "tipoEvento": "LOGIN", ... }`)
    * `POST /api/v1/progreso` (Body: `{ "cursoId": 1, "estudianteId": 2, "tipoEvento": "LECCION_VISTA", ... }`)
    * `POST /api/v1/progreso` (Body:
      `{ "cursoId": 1, "estudianteId": 2, "tipoEvento": "EXAMEN_ENTREGADO", "valor": "4.2" ... }`)
    * *(Repetir esto varias veces para generar suficientes datos crudos)*

5. **Obtener el Reporte!**
    * `GET /api/v1/reportes/curso/1/analitica`
    * *(Verificar que el JSON de respuesta contenga las métricas calculadas correctamente para Cesar y el otro
      estudiante).*

Para validar nuestro proyecto, implementaremos dos tipos principales de pruebas, cada una con un objetivo diferente.

Pruebas Unitarias (Unit Tests):

Objetivo: Probar una única clase (un "ladrillo") de forma aislada.

Velocidad: Extremadamente rápidas.

Herramientas: JUnit 5, Mockito.

Ejemplo: Probar que el ReporteService calcula correctamente el promedio, sin llamar a Feign ni a la base de datos.

Pruebas de Integración (Integration Tests):

Objetivo: Probar cómo interactúan varios componentes de Spring (una "pared").

Velocidad: Más lentas (levantan el contexto de Spring).

Herramientas: @SpringBootTest, MockMvc, @DataJpaTest.

Ejemplo: Probar que el endpoint GET /api/v1/estudiantes/{id} funciona, llama al servicio, este al repositorio, y
devuelve un JSON correcto.