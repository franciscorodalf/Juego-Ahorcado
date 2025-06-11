package es.franciscorodalf.juegoahorcado.backend.model.abstractas;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private static final String DB_NAME = "usuarios.db";

    /**
     * Constructor por defecto que localiza automáticamente la base de datos en los recursos
     */
    public Conexion() {
        try {
            // Intentamos usar una ubicación fija para la base de datos en el directorio del usuario
            String userHome = System.getProperty("user.home");
            File dbDir = new File(userHome, ".juegoahorcado");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }
            
            File dbFile = new File(dbDir, DB_NAME);
            
            // Si la BD no existe en el directorio del usuario, la copiamos desde los recursos
            if (!dbFile.exists()) {
                try (InputStream is = getClass().getResourceAsStream("/database/" + DB_NAME)) {
                    if (is == null) {
                        // Si no existe el recurso, creamos la base de datos ejecutando los scripts SQL
                        inicializarBaseDeDatos(dbFile.getAbsolutePath());
                    } else {
                        Files.copy(is, dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
            
            this.rutaArchivoBD = dbFile.getAbsolutePath();
            System.out.println("📁 Usando base de datos: " + this.rutaArchivoBD);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar la base de datos: " + e.getMessage(), e);
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
        
        // Si el archivo no existe pero está en resources, intentamos copiarlo
        if (!file.exists()) {
            try {
                // Intenta extraer el archivo desde recursos
                try (InputStream is = getClass().getResourceAsStream("/database/" + file.getName())) {
                    if (is != null) {
                        Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("📁 Base de datos copiada a: " + file.getAbsolutePath());
                    } else {
                        // Creamos la base de datos ejecutando los scripts SQL
                        inicializarBaseDeDatos(unaRutaArchivoBD);
                    }
                }
            } catch (IOException e) {
                throw new SQLException("No se pudo copiar la base de datos: " + e.getMessage());
            }
        }

        if (!file.exists()) {
            throw new SQLException("No existe la base de datos: " + unaRutaArchivoBD);
        }

        this.rutaArchivoBD = unaRutaArchivoBD;
    }

    /**
     * Inicializa la base de datos ejecutando scripts SQL
     * @param dbPath Ruta donde crear la base de datos
     * @throws SQLException Si hay error al ejecutar los scripts
     */
    private void inicializarBaseDeDatos(String dbPath) throws SQLException {
        try {
            // Crear conexión a la BD (SQLite creará el archivo si no existe)
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            
            // Leer y ejecutar el script SQL
            try (InputStream is = getClass().getResourceAsStream("/database/script-letters.sql")) {
                if (is == null) {
                    throw new SQLException("No se encontró el script de inicialización de la BD");
                }
                
                String sqlScript = new String(is.readAllBytes());
                String[] statements = sqlScript.split(";");
                
                for (String statement : statements) {
                    if (!statement.trim().isEmpty()) {
                        try {
                            conn.createStatement().execute(statement);
                        } catch (SQLException e) {
                            System.err.println("Error ejecutando: " + statement);
                            throw e;
                        }
                    }
                }
                
                System.out.println("✅ Base de datos inicializada correctamente en: " + dbPath);
            }
            
            conn.close();
            
        } catch (Exception e) {
            throw new SQLException("Error al inicializar la base de datos: " + e.getMessage(), e);
        }
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
            System.err.println("❌ Error al obtener la conexión a la BD: " + e.getMessage());
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
