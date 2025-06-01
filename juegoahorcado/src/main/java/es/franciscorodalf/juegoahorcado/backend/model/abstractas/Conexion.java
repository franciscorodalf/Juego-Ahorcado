package es.franciscorodalf.juegoahorcado.backend.model.abstractas;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase abstracta que proporciona funcionalidades básicas para la conexión a la base de datos SQLite.
 * Todos los servicios que requieren acceso a datos deben extender esta clase.
 */
public abstract class Conexion {

    private String rutaArchivoBD;
    private static Connection connection;

    /**
     * Constructor por defecto que localiza automáticamente la base de datos en los recursos
     */
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

    /**
     * Constructor que permite especificar la ruta de la base de datos
     * @param unaRutaArchivoBD Ruta al archivo de la base de datos
     * @throws SQLException Si la ruta no es válida o el archivo no existe
     */
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

    /**
     * Obtiene la ruta del archivo de la base de datos
     * @return Ruta al archivo de la base de datos
     */
    public String getRutaArchivoBD() {
        return this.rutaArchivoBD;
    }

    /**
     * Obtiene o crea una conexión a la base de datos
     * @return Objeto Connection para interactuar con la base de datos
     */
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

    /**
     * Establece una conexión a la base de datos
     * @return Objeto Connection para interactuar con la base de datos
     * @throws SQLException Si ocurre un error al conectar
     */
    public Connection conectar() throws SQLException {
        return getConnection();
    }

    /**
     * Cierra la conexión a la base de datos si está abierta
     * @throws SQLException Si ocurre un error al cerrar la conexión
     */
    public static void cerrar() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }
}
