package es.franciscorodalf.juegoahorcado.frontend.controller;

import es.franciscorodalf.juegoahorcado.backend.model.UsuarioEntity;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class perfilController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField nivelField;

    private UsuarioEntity usuario;

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        if (usuario != null) {
            usernameField.setText(usuario.getNombre());
            emailField.setText(usuario.getEmail());
            nivelField.setText(usuario.getNivel());
        }
    }

    @FXML
    private void handleEditar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/es/franciscorodalf/juegoahorcado/editarUsuario.fxml");
    }

    @FXML
    private void handleJugar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/franciscorodalf/juegoahorcado/juego.fxml"));
        Parent root = loader.load();

        juegoController controller = loader.getController();
        controller.setUsuario(this.usuario);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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
