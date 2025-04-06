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

    @FXML
    private void handleCancelar(ActionEvent event) throws IOException {
        volverAlLogin(event);
    }

    private void volverAlLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/franciscorodalf/juegoahorcado/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
