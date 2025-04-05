package es.franciscorodalf.juegoahorcado.backend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import java.io.IOException;

public class loginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    /**
     * Cambia de escena cargando el FXML dado
     */
    private void cambiarEscena(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleAceptar(ActionEvent event) throws IOException {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.equals("admin") && pass.equals("1234")) {
            cambiarEscena(event, "/es/franciscorodalf/juegoahorcado/perfil.fxml");
        } else {
            System.out.println("Credenciales inválidas");
        }
    }

    @FXML
    private void handleRegistrar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/es/franciscorodalf/juegoahorcado/registrarUsuario.fxml");
    }

    @FXML
    private void handleListarUsuarios(ActionEvent event) throws IOException {
        cambiarEscena(event, "/es/franciscorodalf/juegoahorcado/listaUsuarios.fxml");
    }
    @FXML
    private void handleRecuperar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/es/franciscorodalf/juegoahorcado/recuperar.fxml");
    }
}
