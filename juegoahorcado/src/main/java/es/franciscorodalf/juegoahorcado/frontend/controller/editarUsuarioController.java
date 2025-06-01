package es.franciscorodalf.juegoahorcado.frontend.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controlador para la pantalla de edición de usuarios.
 * Permite modificar la información del perfil de usuario.
 */
public class editarUsuarioController {
    
    /**
     * Navega de vuelta a la pantalla de perfil del usuario.
     * Este método se ejecuta cuando el usuario presiona el botón Regresar.
     * 
     * @param event El evento que desencadenó esta acción
     * @throws IOException Si ocurre un error al cargar la pantalla de perfil
     */
     @FXML
    private void ClickbuttonRegresar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/franciscorodalf/juegoahorcado/perfil.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
