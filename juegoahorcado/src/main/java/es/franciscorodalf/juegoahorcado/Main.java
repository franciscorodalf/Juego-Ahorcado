package es.franciscorodalf.juegoahorcado;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal que inicia la aplicación del Juego del Ahorcado.
 * Extiende de Application para crear una aplicación JavaFX.
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 400);
        stage.setTitle("Pantalla Princial");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Método principal que inicia la aplicación JavaFX
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        launch();
    }
}