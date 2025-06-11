package es.franciscorodalf.juegoahorcado;

import es.franciscorodalf.juegoahorcado.backend.util.DbInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Clase principal que inicia la aplicación del Juego del Ahorcado.
 * Extiende de Application para crear una aplicación JavaFX.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Inicializar la base de datos al inicio de la aplicación
        try {
            String userHome = System.getProperty("user.home");
            File dbDir = new File(userHome, ".juegoahorcado");
            DbInitializer.initializeDatabase(dbDir);
            System.out.println("✅ Inicialización de la base de datos completada");
        } catch (Exception e) {
            System.err.println("❌ Error al inicializar la base de datos:");
            e.printStackTrace();
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 350, 500);
            stage.setTitle("Juego del Ahorcado");
            stage.setScene(scene);
            stage.setResizable(false); // Hace que la ventana no sea redimensionable
            stage.show();
        } catch (Exception e) {
            System.err.println("❌ Error al cargar la interfaz de usuario:");
            e.printStackTrace();
        }
    }

    /**
     * Método principal que inicia la aplicación JavaFX
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        launch();
    }
}