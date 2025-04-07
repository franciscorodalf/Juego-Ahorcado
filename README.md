# 🎮 Juego del Ahorcado - Aplicación JavaFX

Una aplicación educativa e interactiva del clásico **juego del ahorcado**, desarrollada con **Java**, **JavaFX** y **SQLite**. Ideal para aprender estructuras básicas de programación, lógica de juego y bases de datos relacionales.

---

## 🧾 ¿Qué hace este proyecto?

Esta aplicación permite al usuario jugar al tradicional **juego del ahorcado**, con las siguientes funcionalidades:

- Inicio de sesión y registro de usuarios con nivel de dificultad personalizado.
- Juego interactivo con palabras aleatorias y validación de letras.
- Dibujo progresivo del muñeco del ahorcado según los errores del jugador.
- Sistema de niveles (fácil, medio, difícil) que cambia automáticamente tras ganar o perder.
- Visualización de una tabla con todos los usuarios registrados y su información.
- Navegación fluida entre las pantallas de login, juego, perfil y listado de usuarios.

---

## 🖥️ Pantallas de la aplicación

### 🔐 1. Pantalla de Login

- Permite al usuario iniciar sesión con su email o nombre de usuario.
- Acceso validado contra la base de datos SQLite.

### 👤 2. Pantalla de Perfil

- Muestra los datos del usuario actual (nombre, email, nivel).
- Botón para iniciar partida o ver lista de usuarios.

### 🕹️ 3. Pantalla del Juego

- Muestra la palabra oculta (con guiones bajos).
- Campo de texto para introducir letras.
- Dibujo del ahorcado que avanza con cada error.
- Botones para reiniciar o volver al perfil.
- Mensajes de victoria o derrota con cambio automático de nivel.

### 📋 4. Pantalla de Lista de Usuarios

- Tabla que muestra:
  - Nombre de usuario
  - Email
  - Nivel actual
- Botón para regresar al login o perfil.
- Ideal para administradores o estadísticas del sistema.
  
---

## 🛠️ Tecnologías utilizadas

| Herramienta     | Descripción                    |
|-----------------|--------------------------------|
| Java            | Lenguaje principal             |
| JavaFX          | Interfaz gráfica               |
| FXML            | Definición de vistas           |
| SQLite          | Base de datos embebida         |
| JDBC            | Conexión a la base de datos    |
| Scene Builder   | (opcional) Diseño visual       |

---

## 🧠 Lógica del juego

- El usuario introduce letras para intentar adivinar una palabra secreta.
- Si acierta, se descubre la letra en la palabra.
- Si falla, se dibuja una parte del ahorcado.
- Con 6 errores, el jugador pierde.
- Al ganar o perder, el nivel del jugador cambia automáticamente:
  - Subirá de nivel si acierta.
  - Bajará si falla.

---

## 👤 Gestión de usuarios

- Los usuarios tienen nombre, correo, contraseña y nivel asociado.
- Se guardan en la base de datos y pueden acceder al sistema.
- Desde la vista de administración, se puede ver la lista de usuarios con su nivel.

---

## 📂 Base de datos

- Archivo SQLite: `usuarios.db`.
- Tablas:
  - `usuarios`: contiene `user`, `email`, `password`, `id_nivel`.
  - `niveles`: contiene `id`, `nivel` (fácil, medio, difícil).
  - `palabras`: palabras categorizadas por dificultad.

---

## 🚀 Cómo ejecutar

1. Clona el repositorio.
2. Abre el proyecto en tu IDE (IntelliJ, VSCode, NetBeans...).
3. Asegúrate de tener JavaFX configurado.
4. Ejecuta la clase `Main` o el `FXMLLoader` inicial.

---

## 📝 Créditos

Desarrollado por: **Francisco Rodalf**  
Proyecto académico - JavaFX 



