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

    /**
     * Establece el usuario actual y carga sus datos en la interfaz.
     * 
     * @param usuario El objeto usuario cuyos datos se mostrarán
     */
    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
        cargarDatosUsuario();
    }

    /**
     * Carga los datos del usuario actual en los campos de la interfaz.
     * Actualiza los campos de nombre, email y nivel con la información del usuario.
     */
    private void cargarDatosUsuario() {
        if (usuario != null) {
            usernameField.setText(usuario.getNombre());
            emailField.setText(usuario.getEmail());
            nivelField.setText(usuario.getNivel());
        }
    }

    /**
     * Navega a la pantalla de edición del usuario actual.
     * 
     * @param event El evento que desencadenó este método
     * @throws IOException Si ocurre un error al cargar la pantalla de edición
     */
    @FXML
    private void handleEditar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/es/franciscorodalf/juegoahorcado/editarUsuario.fxml");
    }

    /**
     * Inicia el juego del ahorcado con el usuario actual.
     * Carga la pantalla del juego y transfiere los datos del usuario.
     * 
     * @param event El evento que desencadenó este método
     * @throws IOException Si ocurre un error al cargar la pantalla del juego
     */
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

    /**
     * Cierra la sesión actual y vuelve a la pantalla de inicio de sesión.
     * 
     * @param event El evento que desencadenó este método
     * @throws IOException Si ocurre un error al cargar la pantalla de login
     */
    @FXML
    private void handleRegresar(ActionEvent event) throws IOException {
        cambiarEscena(event, "/es/franciscorodalf/juegoahorcado/login.fxml");
    }

    /**
     * Método auxiliar para cambiar entre diferentes pantallas.
     * Carga el archivo FXML especificado y actualiza la escena actual.
     * 
     * @param event El evento desde el cual se obtiene la ventana actual
     * @param fxmlPath Ruta al archivo FXML de la nueva pantalla
     * @throws IOException Si ocurre un error al cargar el archivo FXML
     */
    private void cambiarEscena(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
