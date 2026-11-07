# VozUrbana

API REST académica para que la ciudadanía reporte incidencias urbanas y las municipalidades gestionen su atención y los materiales usados.

Proyecto en equipo del curso de Cibertec. Integrantes en GitHub: Yonidv, panteraxo y AKUmajinn.

## Funciones

- Registro e inicio de sesión con JWT y roles de usuario.
- Gestión de incidencias con estados, municipalidades y materiales.
- Carga de imágenes de las incidencias.
- Migraciones de base de datos con Flyway.
- Prueba unitaria del servicio de incidencias.

## Tecnologías

Java, Spring Boot, Spring Security, JPA/Hibernate, PostgreSQL, Flyway, Maven y JUnit.

## Cómo ejecutarlo

1. Crea una base PostgreSQL local.
2. Ajusta la conexión en `src/main/resources/application.properties`.
3. Ejecuta:

```bash
./mvnw spring-boot:run
```

## Mi aporte

Creé el repositorio y la base del proyecto en Spring Boot, y participé en el análisis del sistema.
