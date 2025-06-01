package es.franciscorodalf.juegoahorcado.frontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

/**
 * Controlador para la pantalla de recuperación de contraseña.
 * Gestiona la funcionalidad de recuperación mediante correo electrónico.
 */
public class RecuperarController {

    @FXML
    private TextField emailOrUserField;

    /**
     * Maneja el evento del botón de recuperar contraseña.
     * Valida que se haya ingresado un correo o nombre de usuario
     * y simula el envío de un correo de recuperación.
     * 
     * @param event El evento de acción que desencadenó este método
     */
    @FXML
    private void handleRecuperar(ActionEvent event) {
        String input = emailOrUserField.getText().trim();

        if (input.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campo vacío", "Por favor, introduce tu usuario o correo.");
            return;
        }

        mostrarAlerta(AlertType.INFORMATION, "Recuperación iniciada", "Se ha enviado un correo");
    }

    /**
     * Navega de vuelta a la pantalla de inicio de sesión.
     * 
     * @param event El evento desde el cual se obtiene la ventana actual
     * @throws IOException Si ocurre un error al cargar la pantalla de login
     */
    @FXML
    private void clickVolver(ActionEvent event) throws IOException {
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
