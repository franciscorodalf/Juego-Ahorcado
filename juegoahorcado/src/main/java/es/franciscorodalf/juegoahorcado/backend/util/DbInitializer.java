package es.franciscorodalf.juegoahorcado.backend.util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase utilitaria para inicializar la base de datos del juego.
 * Se encarga de crear la base de datos si no existe y ejecutar los scripts necesarios.
 */
public class DbInitializer {
    
    private static final String DB_NAME = "usuarios.db";
    
    /**
     * Inicializa la base de datos del juego en la ubicación especificada.
     * Si la base de datos ya existe, no hace nada.
     * 
     * @param dbDir Directorio donde se creará/verificará la base de datos
     * @return Ruta del archivo de base de datos
     */
    public static String initializeDatabase(File dbDir) {
        try {
            // Nos aseguramos que el directorio exista
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }
            
            File dbFile = new File(dbDir, DB_NAME);
            
            // Si la base de datos ya existe, simplemente devolvemos su ruta
            if (dbFile.exists() && dbFile.length() > 0) {
                System.out.println("📊 Base de datos existente: " + dbFile.getAbsolutePath());
                return dbFile.getAbsolutePath();
            }
            
            // Intentamos copiar la base de datos desde los recursos
            try (InputStream is = DbInitializer.class.getResourceAsStream("/database/" + DB_NAME)) {
                if (is != null) {
                    Files.copy(is, dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("📊 Base de datos copiada desde recursos a: " + dbFile.getAbsolutePath());
                    return dbFile.getAbsolutePath();
                }
            } catch (Exception e) {
                System.err.println("⚠️ No se pudo copiar la base de datos desde recursos: " + e.getMessage());
            }
            
            // Si no se pudo copiar, creamos la base de datos desde los scripts SQL
            return createDatabaseFromScript(dbFile);
            
        } catch (Exception e) {
            throw new RuntimeException("No se pudo inicializar la base de datos", e);
        }
    }
    
    /**
     * Crea una nueva base de datos ejecutando los scripts SQL
     * 
     * @param dbFile Archivo donde se creará la base de datos
     * @return Ruta del archivo de base de datos creado
     * @throws SQLException Si hay error al ejecutar los scripts SQL
     */
    private static String createDatabaseFromScript(File dbFile) throws SQLException {
        System.out.println("🔧 Creando nueva base de datos desde script SQL...");
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath())) {
            // Primero intentamos usar el script completo
            if (executeScript(conn, "/database/script-letters.sql")) {
                System.out.println("✅ Base de datos creada correctamente con script-letters.sql");
                return dbFile.getAbsolutePath();
            }
            
            // Si falló, intentamos con el script básico
            if (executeScript(conn, "/database/script-users.sql")) {
                System.out.println("✅ Base de datos creada correctamente con script-users.sql");
                return dbFile.getAbsolutePath();
            }
            
            throw new SQLException("No se pudieron ejecutar los scripts SQL");
        }
    }
    
    /**
     * Ejecuta un script SQL desde los recursos
     * 
     * @param conn Conexión a la base de datos
     * @param scriptPath Ruta del script en los recursos
     * @return true si se ejecutó correctamente, false en caso contrario
     */
    private static boolean executeScript(Connection conn, String scriptPath) {
        try (InputStream is = DbInitializer.class.getResourceAsStream(scriptPath)) {
            if (is == null) {
                System.err.println("⚠️ No se encontró el script: " + scriptPath);
                return false;
            }
            
            String sqlScript = new String(is.readAllBytes());
            String[] statements = sqlScript.split(";");
            
            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty()) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(statement);
                    } catch (SQLException e) {
                        System.err.println("Error ejecutando SQL: " + statement);
                        e.printStackTrace();
                        // Continuamos con la siguiente instrucción
                    }
                }
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error ejecutando script SQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
