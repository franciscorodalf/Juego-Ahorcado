package es.franciscorodalf.juegoahorcado.frontend.controller;

import es.franciscorodalf.juegoahorcado.backend.model.UsuarioEntity;
import es.franciscorodalf.juegoahorcado.backend.model.abstractas.Conexion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador para la pantalla del juego del ahorcado.
 * Gestiona la lógica del juego, incluyendo la selección de palabras,
 * validación de letras y dibujo del ahorcado.
 */
public class juegoController extends Conexion {

    @FXML
    private Label usernameLabel;     
    @FXML
    private Label nivelLabel;         
    @FXML
    private Label palabraLabel;       
    @FXML
    private TextField inputLetra;     
    @FXML
    private Canvas canvas;          
    @FXML
    private Button btnProbar;
    @FXML
    private Label letrasUsadasLabel;
    @FXML
    private Label errorLabel;

    private UsuarioEntity usuario;          
    private String palabraSecreta;           
    private StringBuilder palabraOculta;      
    private Set<Character> letrasUsadas = new HashSet<>();  
    private int errores = 0;                 
    private final int MAX_ERRORES = 6;       

    /**
     * Método de inicialización que se ejecuta al cargar la vista
     */
    @FXML
    public void initialize() {
        btnProbar.setOnAction(this::probarLetra);
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }

    /**
     * Establece el usuario actual y configura la interfaz con sus datos
     * @param usuario Usuario actual del juego
     */
    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
        usernameLabel.setText(usuario.getNombre());
        nivelLabel.setText(usuario.getNivel());
        inicializarJuego();
    }

    /**
     * Inicializa o reinicia el juego con una nueva palabra
     */
    private void inicializarJuego() {
        palabraSecreta = obtenerPalabraAleatoria(usuario.getNivel());
        
        // Crear la palabra oculta con mejor visualización
        palabraOculta = new StringBuilder();
        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (i > 0) {
                palabraOculta.append(" ");
            }
            palabraOculta.append("_");
        }
        
        palabraLabel.setText(palabraOculta.toString());
        errores = 0;
        letrasUsadas.clear();
        actualizarLetrasUsadas();
        if (errorLabel != null) {
            errorLabel.setText("");
        }
        dibujarBase();
    }

    /**
     * Actualiza el label que muestra las letras que ya se han utilizado
     */
    private void actualizarLetrasUsadas() {
        if (letrasUsadas.isEmpty()) {
            letrasUsadasLabel.setText("-");
        } else {
            // Convertir el conjunto de letras usadas a una cadena ordenada
            String letrasStr = letrasUsadas.stream()
                                          .sorted()
                                          .map(String::valueOf)
                                          .collect(Collectors.joining(", "));
            letrasUsadasLabel.setText(letrasStr);
        }
    }

    /**
     * Obtiene una palabra aleatoria de la base de datos según el nivel
     * @param nivel Nivel del juego (fácil, medio, difícil)
     * @return Palabra aleatoria para el nivel especificado
     */
    private String obtenerPalabraAleatoria(String nivel) {
        String palabra = "error";
        String sql = "SELECT palabra FROM palabras INNER JOIN niveles ON palabras.id_nivel = niveles.id WHERE niveles.nivel = ? ORDER BY RANDOM() LIMIT 1";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nivel.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                palabra = rs.getString("palabra");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return palabra.toLowerCase();
    }

    /**
     * Procesa el intento del usuario de adivinar una letra
     * @param event Evento de acción del botón
     */
    private void probarLetra(ActionEvent event) {
        String letra = inputLetra.getText().trim().toLowerCase();
        if (letra.isEmpty()) {
            mostrarError("No has ingresado ninguna letra");
            return;
        }
        if (letra.length() > 1) {
            mostrarError("Ingresa solo una letra");
            return;
        }
        if (!letra.matches("[a-zA-ZñÑ]")) {
            mostrarError("Carácter no válido");
            return;
        }
        char letraChar = letra.charAt(0);
        if (letrasUsadas.contains(letraChar)) {
            mostrarError("Ya has usado esa letra");
            return;
        }
        mostrarError("");

        letrasUsadas.add(letraChar);
        actualizarLetrasUsadas();

        boolean acierto = false;
        StringBuilder palabraActualizada = new StringBuilder();
        
        for (int i = 0; i < palabraSecreta.length(); i++) {
            // Añadir espacio entre caracteres excepto antes del primer caracter
            if (i > 0) {
                palabraActualizada.append(" ");
            }
            
            if (palabraSecreta.charAt(i) == letraChar) {
                palabraActualizada.append(letraChar);
                acierto = true;
            } else {
                // Conservar letras ya descubiertas o mantener guión
                char charEnPosicion = palabraOculta.charAt(i * 2 > 0 ? i * 2 : i);
                palabraActualizada.append(charEnPosicion == '_' ? '_' : palabraSecreta.charAt(i));
            }
        }
        
        palabraOculta = palabraActualizada;
        palabraLabel.setText(palabraOculta.toString());

        if (!acierto) {
            errores++;
            dibujarParte(errores);
            if (errores >= MAX_ERRORES) {
                mostrarAlerta("Has perdido", "La palabra era: " + palabraSecreta);
                cambiarNivel(false);
                inicializarJuego();
            }
        } else if (!palabraOculta.toString().contains("_")) {
            mostrarAlerta("Felicidades!", "Has adivinado la palabra: " + palabraSecreta);
            cambiarNivel(true);
            inicializarJuego();
        }

        inputLetra.clear();
    }

    /**
     * Cambia el nivel del usuario según el resultado del juego
     * @param subir true si debe subir de nivel, false si debe bajar
     */
    private void cambiarNivel(boolean subir) {
        int nivelActual;
        String nivelUsuario = usuario.getNivel().toLowerCase();

        switch (nivelUsuario) {
            case "facil":
                nivelActual = 1;
                break;
            case "medio":
                nivelActual = 2;
                break;
            case "dificil":
                nivelActual = 3;
                break;
            default:
                nivelActual = 1;
                break;
        }

        int nuevoNivel;
        if (subir) {
            if (nivelActual < 3) {
                nuevoNivel = nivelActual + 1;
            } else {
                nuevoNivel = 3;
            }
        } else {
            if (nivelActual > 1) {
                nuevoNivel = nivelActual - 1;
            } else {
                nuevoNivel = 1;
            }
        }

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement("UPDATE usuarios SET id_nivel = ? WHERE email = ?")) {
            stmt.setInt(1, nuevoNivel);
            stmt.setString(2, usuario.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String nuevoNivelTexto;
        switch (nuevoNivel) {
            case 1:
                nuevoNivelTexto = "facil";
                break;
            case 2:
                nuevoNivelTexto = "medio";
                break;
            case 3:
                nuevoNivelTexto = "dificil";
                break;
            default:
                nuevoNivelTexto = "facil";
                break;
        }

        usuario.setNivel(nuevoNivelTexto);
        nivelLabel.setText(usuario.getNivel());
    }

    /**
     * Muestra una alerta informativa al usuario
     * @param titulo Título de la alerta
     * @param contenido Mensaje de la alerta
     */
    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();

    }

    /**
     * Muestra un mensaje de error en la interfaz de juego
     * @param mensaje Texto a mostrar
     */
    private void mostrarError(String mensaje) {
        if (errorLabel != null) {
            errorLabel.setText(mensaje);
        }
    }

    /**
     * Dibuja la base del ahorcado (estructura inicial)
     */
    private void dibujarBase() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Fondo suave
        gc.setFill(Color.rgb(248, 249, 250));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Configuración de líneas
        gc.setStroke(Color.rgb(60, 99, 130));
        gc.setLineWidth(3);
        
        // Base horizontal
        gc.strokeLine(30, 200, 170, 200);
        
        // Poste vertical
        gc.strokeLine(50, 200, 50, 30);
        
        // Soporte superior
        gc.strokeLine(50, 30, 120, 30);
        
        // Cuerda
        gc.setStroke(Color.rgb(193, 154, 107));
        gc.setLineWidth(2);
        gc.strokeLine(120, 30, 120, 50);
    }

    /**
     * Dibuja una parte del ahorcado según el número de error
     * @param error Número de error actual (1-6)
     */
    private void dibujarParte(int error) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        switch (error) {
            case 1:
                // Cabeza
                gc.setStroke(Color.rgb(44, 62, 80));
                gc.setLineWidth(2);
                gc.strokeOval(105, 50, 30, 30);
                break;
            case 2:
                // Cuerpo
                gc.setStroke(Color.rgb(44, 62, 80));
                gc.setLineWidth(2);
                gc.strokeLine(120, 80, 120, 130);
                break;
            case 3:
                // Brazo izquierdo
                gc.setStroke(Color.rgb(44, 62, 80));
                gc.setLineWidth(2);
                gc.strokeLine(120, 90, 95, 110);
                break;
            case 4:
                // Brazo derecho
                gc.setStroke(Color.rgb(44, 62, 80));
                gc.setLineWidth(2);
                gc.strokeLine(120, 90, 145, 110);
                break;
            case 5:
                // Pierna izquierda
                gc.setStroke(Color.rgb(44, 62, 80));
                gc.setLineWidth(2);
                gc.strokeLine(120, 130, 95, 170);
                break;
            case 6:
                // Pierna derecha
                gc.setStroke(Color.rgb(44, 62, 80));
                gc.setLineWidth(2);
                gc.strokeLine(120, 130, 145, 170);
                
                // Cara triste en el último error
                gc.setStroke(Color.RED);
                gc.setLineWidth(1);
                // Ojos X
                gc.strokeLine(110, 60, 117, 67);
                gc.strokeLine(117, 60, 110, 67);
                gc.strokeLine(123, 60, 130, 67);
                gc.strokeLine(130, 60, 123, 67);
                // Boca triste
                gc.strokeArc(110, 70, 20, 10, 0, 180, javafx.scene.shape.ArcType.OPEN);
                break;
        }
    }

    /**
     * Reinicia el juego con una nueva palabra
     * @param event Evento de acción del botón
     */
    @FXML
    private void reiniciarJuego(ActionEvent event) {
        inicializarJuego();
    }

    /**
     * Navega de vuelta a la pantalla de perfil
     * @param event Evento de acción del botón
     */
    @FXML
    private void volverAPerfil(ActionEvent event) {
        try {
          
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/franciscorodalf/juegoahorcado/perfil.fxml"));
            Parent root = loader.load();

           
            perfilController controller = loader.getController();
            controller.setUsuario(usuario);
            
          
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
