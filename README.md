# Laboratorio V: Creación de APIs REST con Spring Framework

Estudiante: Angel Ricardo Cinto Gonzalez — Sección C
Curso: Programación 2 — Universidad Mariano Gálvez de Guatemala

## Objetivo

Desarrollar 10 APIs REST independientes utilizando Spring Boot y Maven, aplicando los conceptos de HTTP, JSON, controladores REST y operaciones CRUD (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`) sobre listas en memoria.

## Tecnologías

- Java 17 (compilado con `maven.compiler.release=17`)
- Spring Boot 3.3.5 (`spring-boot-starter-web`)
- Maven

## APIs incluidas

| # | Recurso | Endpoint base |
|---|---------|----------------|
| 1 | Productos | `/api/productos` |
| 2 | Estudiantes | `/api/estudiantes` |
| 3 | Libros | `/api/libros` |
| 4 | Empleados | `/api/empleados` |
| 5 | Películas | `/api/peliculas` |
| 6 | Cursos | `/api/cursos` |
| 7 | Vehículos | `/api/vehiculos` |
| 8 | Tareas | `/api/tareas` |
| 9 | Clientes | `/api/clientes` |
| 10 | Pedidos | `/api/pedidos` |

Cada recurso expone los mismos 6 endpoints:

```
GET     /api/{recurso}
GET     /api/{recurso}/{id}
POST    /api/{recurso}
PUT     /api/{recurso}/{id}
PATCH   /api/{recurso}/{id}
DELETE  /api/{recurso}/{id}
```

Cada API arranca con 5 registros de ejemplo cargados en memoria.

## Cómo ejecutar el proyecto

```bash
mvn spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

También se puede abrir la carpeta `spring-apis-lab/` directamente en Visual Studio Code (con el Spring Boot Extension Pack) y ejecutar `ApisApplication.java` desde el Spring Boot Dashboard o el botón "Run".

## Probar con Postman

Importar la colección incluida en [`postman/LaboratorioV-APIs.postman_collection.json`](postman/LaboratorioV-APIs.postman_collection.json). Contiene una carpeta por recurso (`01 Productos` … `10 Pedidos`) con los 6 requests de cada uno, usando la variable `{{baseUrl}}` (por defecto `http://localhost:8080`).

Ejemplo de `PATCH` (actualización parcial) en Tareas:

```json
{
    "completada": true
}
```

Ejemplo de `PATCH` en Pedidos:

```json
{
    "estado": "ENVIADO"
}
```

## Estructura del proyecto

```
spring-apis-lab/
├── pom.xml
├── postman/
│   └── LaboratorioV-APIs.postman_collection.json
└── src/main/java/com/lab/apis/
    ├── ApisApplication.java
    ├── controller/
    │   └── ... (10 controladores REST)
    └── model/
        └── ... (10 modelos)
```
