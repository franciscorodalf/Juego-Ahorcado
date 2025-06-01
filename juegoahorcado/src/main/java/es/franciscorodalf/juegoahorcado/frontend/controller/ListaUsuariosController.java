package es.franciscorodalf.juegoahorcado.frontend.controller;

import es.franciscorodalf.juegoahorcado.backend.model.UsuarioEntity;
import es.franciscorodalf.juegoahorcado.backend.model.UsuarioServiceModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListaUsuariosController {

    @FXML
    private TableView<UsuarioEntity> tablaUsuarios;

    @FXML
    private TableColumn<UsuarioEntity, String> colUsuario;

    @FXML
    private TableColumn<UsuarioEntity, String> colEmail;

    @FXML
    private TableColumn<UsuarioEntity, String> colNivel;

    private final UsuarioServiceModel usuarioService = new UsuarioServiceModel();
    private final ObservableList<UsuarioEntity> listaUsuarios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        try {
            ArrayList<UsuarioEntity> usuarios = usuarioService.obtenerUsuarios();
            listaUsuarios.setAll(usuarios);
            tablaUsuarios.setItems(listaUsuarios);
        } catch (SQLException e) {
            System.err.println("❌ Error al cargar usuarios:");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegresar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/franciscorodalf/juegoahorcado/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
