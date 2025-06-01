package es.franciscorodalf.juegoahorcado.frontend.controller;

import es.franciscorodalf.juegoahorcado.backend.model.UsuarioEntity;
import es.franciscorodalf.juegoahorcado.backend.model.UsuarioServiceModel;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

/**
 * Controlador para la pantalla de registro de usuarios.
 * Maneja la validación y creación de nuevos usuarios en el sistema.
 */
public class registroController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField confirmEmailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private UsuarioServiceModel servicioUsuario;

    /**
     * Método de inicialización que se ejecuta al cargar la pantalla de registro.
     * Configura la conexión con la base de datos para el registro de usuarios.
     */
    @FXML
    public void initialize() {
        try {
            URL dbUrl = getClass().getResource("/database/usuarios.db");
            if (dbUrl != null) {
                servicioUsuario = new UsuarioServiceModel(dbUrl.getPath());
            } else {
                mostrarAlerta(AlertType.ERROR, "Error", "No se encontró la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento del botón Registrar.
     * Valida que todos los campos sean correctos y registra al nuevo usuario
     * en la base de datos si pasa todas las validaciones.
     * 
     * @param event El evento de acción que desencadenó este método
     */
    @FXML
    private void handleRegistrar(ActionEvent event) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String confirmEmail = confirmEmailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos obligatorios", "Todos los campos son requeridos.");
            return;
        }

        if (!email.equals(confirmEmail)) {
            mostrarAlerta(AlertType.WARNING, "Error de correo", "Los correos no coinciden.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            mostrarAlerta(AlertType.WARNING, "Error de contraseña", "Las contraseñas no coinciden.");
            return;
        }

        if (servicioUsuario == null) {
            mostrarAlerta(AlertType.ERROR, "Error", "No se pudo acceder a la base de datos.");
            return;
        }

        UsuarioEntity nuevoUsuario = new UsuarioEntity(email, username, password);

        try {
            boolean registrado = servicioUsuario.agregarUsuario(nuevoUsuario);
            if (registrado) {
                mostrarAlerta(AlertType.INFORMATION, "Registro exitoso", "Usuario registrado correctamente.");
            } else {
                mostrarAlerta(AlertType.ERROR, "Error", "El usuario ya existe o hubo un problema.");
            }
        } catch (SQLException e) {
            mostrarAlerta(AlertType.ERROR, "Error SQL", e.getMessage());
        }
    }

    /**
     * Maneja el evento del botón Cancelar.
     * Cierra la pantalla de registro y vuelve a la pantalla de login.
     * 
     * @param event El evento de acción que desencadenó este método
     * @throws IOException Si ocurre un error al cargar la pantalla de login
     */
    @FXML
    private void handleCancelar(ActionEvent event) throws IOException {
        volverAlLogin(event);
    }

    /**
     * Navega de vuelta a la pantalla de inicio de sesión.
     * 
     * @param event El evento desde el cual se obtiene la ventana actual
     * @throws IOException Si ocurre un error al cargar la pantalla de login
     */
    private void volverAlLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/franciscorodalf/juegoahorcado/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Muestra una alerta con la información proporcionada.
     * 
     * @param tipo El tipo de alerta (información, advertencia, error)
     * @param titulo El título de la ventana de alerta
     * @param mensaje El mensaje a mostrar en la alerta
     */
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
