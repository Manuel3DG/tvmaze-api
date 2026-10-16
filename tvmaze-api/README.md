# TV Maze API - Examen Técnico Pinwox

API REST desarrollada con Java 21, Spring Boot 4.0.8 y MongoDB Atlas, que funciona como intermediario entre el cliente y la API pública de TV Maze.

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.0.8
- Maven
- MongoDB Atlas
- Spring Data MongoDB
- API REST de TV Maze

## Funcionalidades

- Buscar series de televisión mediante la API de TV Maze.
- Consultar la información completa de una serie por su identificador.
- Almacenar series en MongoDB para evitar consultas externas innecesarias.
- Registrar comentarios y calificaciones del 0 al 5.
- Mostrar los comentarios registrados en los resultados de búsqueda y en el detalle de cada serie.

## Requisitos previos

- Java JDK 21 instalado.
- Conexión a Internet.
- Acceso a una instancia de MongoDB Atlas.
- Git (opcional).

## Configuración de MongoDB

La aplicación utiliza la variable de entorno `MONGODB_URI` para establecer la conexión con MongoDB.

El archivo `.env.example` contiene un ejemplo de la cadena de conexión.

En Windows PowerShell, configurar la variable de entorno:

```powershell
$env:MONGODB_URI="mongodb+srv://USUARIO:CONTRASENA@TU_CLUSTER.mongodb.net/tvmaze_db?retryWrites=true&w=majority"
```

Reemplazar los valores de ejemplo por las credenciales y la dirección de una instancia MongoDB válida.

Por seguridad, las credenciales reales no se incluyen en el repositorio.

## Ejecución del proyecto

1. Abrir una terminal en la carpeta donde se encuentra el archivo `pom.xml`.
2. Configurar la variable de entorno `MONGODB_URI`.
3. Ejecutar el siguiente comando:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación estará disponible en:

`http://localhost:8080`

## Endpoints disponibles

### 1. Buscar series

**Método:** GET

**URL:**

`http://localhost:8080/search?search_query=batman`

Permite buscar series mediante su nombre y devuelve los siguientes datos:

- ID
- Nombre
- Canal
- Sinopsis
- Géneros
- Comentarios y calificaciones registrados

### 2. Consultar una serie por ID

**Método:** GET

**URL:**

`http://localhost:8080/shows/975`

Obtiene la información completa de una serie.

Primero consulta MongoDB. Si la serie no está almacenada, realiza una petición a TV Maze y guarda el resultado para futuras consultas.

Los comentarios se obtienen de MongoDB y se agregan a la respuesta.

### 3. Registrar un comentario

**Método:** POST

**URL:**

`http://localhost:8080/comments`

**Cuerpo de la petición (JSON):**

```json
{
  "show_id": 975,
  "comment": "Excelente serie clásica de Batman",
  "rating": 5
}
```

Permite registrar un comentario y una calificación asociada a una serie.

La calificación debe encontrarse entre 0 y 5.

**Respuesta exitosa:** HTTP 201 Created.

## Persistencia de información

MongoDB Atlas se utiliza para almacenar:

- Información de series consultadas por identificador.
- Comentarios y calificaciones asociados a cada serie.

## API externa

La información de las series se obtiene de la API pública de TV Maze.

Documentación oficial: https://www.tvmaze.com/api

## Autor

Juan Manuel Tiznado Flores

**Proyecto desarrollado como parte del examen técnico de Pinwox.**