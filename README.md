---
## 💬 Alumnos

*Mirko Álvarez, Kevin Cabrera, Joaquín Perez Romero*  
Desarrollador Android - Estudiantes UCES  
Argentina, 21/06/2025

---

# 🎬 PelículasApp - Registro y Gestión de Películas

Aplicación Android desarrollada en *Kotlin* que permite registrar, visualizar, filtrar y editar películas. Implementa buenas prácticas modernas de desarrollo móvil: ViewModel, ViewBinding, Intents, RecyclerView y almacenamiento persistente con SharedPreferences.

---

## 🚀 Instrucciones de Ejecución

1. *Abrir el proyecto en Android Studio.*
2. Asegurarse de tener configurado el emulador o un dispositivo físico con Android API 21+.
3. Ejecutar el proyecto con el botón ▶ o desde Run > Run app.

---

## 🔁 Flujo de la Aplicación

1. *Pantalla principal (Registro de película)*  
   - Ingresá título, año, reseña, género, rating.
   - Validación de campos obligatorios.
   - Opción para registrar película o ver listado.

2. *Pantalla de listado de películas*  
   - Muestra las películas registradas en un RecyclerView.
   - Acciones: Editar, Eliminar, Filtrar por género, Filtrar por estado, Ordenar por rating.
   - Filtro y búsqueda por título.

3. *Pantalla de edición de película* (Bonus)  
   - Permite editar datos o eliminar una película.

---

## 🧪 Ejemplo de Uso

1. Abrís la app y completás:
   - 🎬 Título: “Inception”
   - 📅 Año: “2010”
   - 📝 Reseña: “Una obra maestra de ciencia ficción”
   - 🎭 Género: Ciencia Ficción
   - ⭐ Rating: 5 estrellas
2. Tocás *"Registrar"* → vas a la lista.
3. La película aparece listada con posibilidad de editarla o filtrarla.
4. Opcional: marcás la película como *Favorita, **Vista* o *Pendiente*.

---

## ✅ Requisitos Técnicos Cumplidos

| Requisito                         | Estado       |
|----------------------------------|--------------|
| Proyecto Android en Kotlin       | ✅ Cumplido   |
| ViewBinding en Activities        | ✅ Cumplido   |
| Uso de al menos 2 Activities     | ✅ Cumplido   |
| Input de datos por usuario       | ✅ Cumplido   |
| RecyclerView para el listado     | ✅ Cumplido   |
| Intents para pasar datos         | ✅ Cumplido   |
| ViewModel implementado           | ✅ Cumplido   |
| Validaciones de campos           | ✅ Cumplido   |

---


## 🎁 Funcionalidades Bonus Implementadas

| Bonus                                      |  Implementado por    |
|--------------------------------------------|----------------------|
| Pantalla de Edición/Detalle de película    | Joaquín Perez Romero |
| Persistencia con SharedPreferences         | Joaquín Perez Romero |
| Filtros por género y estado personalizado  | Mirko Álvarez        |
| Estados: Pendiente / Vista / Favorita      | Mirko Álvarez        |
| Ordenamiento por rating                    | Kevin Cabrera        |
| Validaciones avanzadas (año, título, etc.) | Kevin Cabrera        |

---

## 🧱 Estructura del Proyecto

- models/
  - Movie.kt → Clase principal con campos requeridos y estado
  - Genero.kt y Estado.kt → Enums
- activities/
  - MainActivity.kt → Registro
  - MovieListActivity.kt → Listado
  - EditMovieActivity.kt → Edición
- adapters/
  - MovieAdapter.kt → Adaptador para RecyclerView
- storage/
  - MovieStorage.kt → Guardado local con SharedPreferences
- viewmodels/
  - MovieViewModel.kt → ViewModel para lista de películas
- res/layout/ → Archivos XML con ViewBinding activado

---

## 💬 Autores

*Mirko Álvarez, Kevin Cabrera, Joaquín Perez Romero*  
Desarrollador Android - Estudiantes UCES  
Argentina, 21/06/2025
---
