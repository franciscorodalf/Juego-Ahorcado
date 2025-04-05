package es.franciscorodalf.juegoahorcado.backend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class perfilController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField nivelField;

    @FXML
    private void initialize() {
        // Aquí podrías cargar los datos del usuario desde sesión o base de datos
        usernameField.setText("usuarioEjemplo");
        emailField.setText("usuario@correo.com");
        nivelField.setText("FÁCIL");

        // Para evitar edición directa desde esta vista
        usernameField.setEditable(false);
        emailField.setEditable(false);
        nivelField.setEditable(false);
    }

    @FXML
    private void handleEditar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/es/franciscorodalf/juegoahorcado/editarUsuario.fxml");
    }

    @FXML
    private void handleJugar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/es/franciscorodalf/juegoahorcado/juego.fxml");
    }

    @FXML
    private void handleRegresar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/es/franciscorodalf/juegoahorcado/login.fxml");
    }

    private void cambiarEscena(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
