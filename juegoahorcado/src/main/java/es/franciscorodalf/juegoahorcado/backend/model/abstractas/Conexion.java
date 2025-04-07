package es.franciscorodalf.juegoahorcado.backend.model.abstractas;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Conexion {

    private String rutaArchivoBD;
    private static Connection connection;

    public Conexion() {
        try {
            URL dbUrl = getClass().getResource("/database/usuarios.db");
            if (dbUrl != null) {
                this.rutaArchivoBD = new File(dbUrl.toURI()).getAbsolutePath();
            } else {
                throw new RuntimeException("No se encontró la base de datos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar la base de datos.", e);
        }
    }

    public Conexion(String unaRutaArchivoBD) throws SQLException {
        if (unaRutaArchivoBD == null || unaRutaArchivoBD.isEmpty()) {
            throw new SQLException("El fichero es nulo o vacío");
        }

        File file = new File(unaRutaArchivoBD);
        if (!file.exists()) {
            throw new SQLException("No existe la base de datos: " + unaRutaArchivoBD);
        }

        this.rutaArchivoBD = unaRutaArchivoBD;
    }

    public String getRutaArchivoBD() {
        return this.rutaArchivoBD;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:" + rutaArchivoBD);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener la conexión a la BD");
            e.printStackTrace();
        }

        return connection;
    }

    public Connection conectar() throws SQLException {
        return getConnection();
    }

    public static void cerrar() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }
}
