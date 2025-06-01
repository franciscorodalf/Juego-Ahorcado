package es.franciscorodalf.juegoahorcado.frontend.controller;

import es.franciscorodalf.juegoahorcado.backend.model.UsuarioEntity;
import es.franciscorodalf.juegoahorcado.backend.model.UsuarioServiceModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Controlador para la pantalla de inicio de sesión.
 * Maneja la autenticación de usuarios y la navegación a otras pantallas.
 */
public class loginController {

    @FXML
    private TextField usernameField;       
    
    @FXML
    private PasswordField passwordField;  

    private UsuarioServiceModel servicioUsuario;

    /**
     * Método de inicialización que se ejecuta al cargar la vista
     */
    @FXML
    public void initialize() {
        try {
            URL dbUrl = getClass().getResource("/database/usuarios.db");
            if (dbUrl != null) {
                String path = new File(dbUrl.toURI()).getAbsolutePath();
                servicioUsuario = new UsuarioServiceModel(path);
            } else {
                System.err.println("❌ No se encontró la base de datos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento del botón Aceptar para iniciar sesión
     * @param event Evento de acción del botón
     * @throws IOException Si ocurre un error al cambiar de pantalla
     */
    @FXML
    private void handleAceptar(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos vacíos", "Por favor, introduce usuario y contraseña.");
            return;
        }

        if (servicioUsuario == null) {
            mostrarAlerta(AlertType.ERROR, "Error interno", "No se pudo conectar a la base de datos.");
            return;
        }

        UsuarioEntity usuario = servicioUsuario.obtenerUsuarioPorNombre(username);

        if (usuario != null && usuario.getContrasenia().equals(password)) {
            cambiarEscenaGuardandoDatos(event, "/es/franciscorodalf/juegoahorcado/perfil.fxml", usuario);
        } else {
            mostrarAlerta(AlertType.ERROR, "Error de autenticación", "Usuario o contraseña incorrectos.");
        }
    }

    /**
     * Maneja el evento del botón Registrar para ir a la pantalla de registro
     * @param event Evento de acción del botón
     * @throws IOException Si ocurre un error al cambiar de pantalla
     */
    @FXML
    private void handleRegistrar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/es/franciscorodalf/juegoahorcado/registrarUsuario.fxml");
    }

    /**
     * Maneja el evento del botón Listar Usuarios para ver la lista de usuarios
     * @param event Evento de acción del botón
     * @throws IOException Si ocurre un error al cambiar de pantalla
     */
    @FXML
    private void handleListarUsuarios(ActionEvent event) throws IOException {
        cambiarEscena(event, "/es/franciscorodalf/juegoahorcado/listaUsuarios.fxml");
    }

    /**
     * Maneja el evento del botón Recuperar Contraseña
     * @param event Evento de acción del botón
     * @throws IOException Si ocurre un error al cambiar de pantalla
     */
    @FXML
    private void handleRecuperar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/es/franciscorodalf/juegoahorcado/recuperar.fxml");
    }

    /**
     * Cambia a otra pantalla sin pasar datos adicionales
     * @param event Evento que desencadenó la acción
     * @param fxmlPath Ruta al archivo FXML de la nueva pantalla
     * @throws IOException Si ocurre un error al cargar la nueva pantalla
     */
    private void cambiarEscena(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Cambia a otra pantalla pasando datos del usuario
     * @param event Evento que desencadenó la acción
     * @param fxmlPath Ruta al archivo FXML de la nueva pantalla
     * @param usuario Objeto usuario a pasar a la nueva pantalla
     * @throws IOException Si ocurre un error al cargar la nueva pantalla
     */
    private void cambiarEscenaGuardandoDatos(ActionEvent event, String fxmlPath, UsuarioEntity usuario)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        if (fxmlPath.contains("perfil.fxml")) {
            perfilController controller = loader.getController();
            controller.setUsuario(usuario);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Muestra una alerta al usuario
     * @param tipo Tipo de alerta (información, advertencia, error)
     * @param titulo Título de la alerta
     * @param mensaje Contenido de la alerta
     */
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
