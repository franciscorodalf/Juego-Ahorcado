package es.franciscorodalf.juegoahorcado.backend.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import es.franciscorodalf.juegoahorcado.backend.model.abstractas.Conexion;

/**
 * Clase de servicio para operaciones relacionadas con usuarios en la base de datos.
 * Proporciona métodos para buscar, insertar y actualizar usuarios.
 */
public class UsuarioServiceModel extends Conexion {

    /**
     * Constructor por defecto que usa la ubicación predeterminada de la base de datos
     */
    public UsuarioServiceModel() {
    }

    /**
     * Constructor que especifica la ruta a la base de datos
     * @param unaRutaArchivoBD Ruta al archivo de base de datos SQLite
     * @throws SQLException Si ocurre un error al conectar a la base de datos
     */
    public UsuarioServiceModel(String unaRutaArchivoBD) throws SQLException {
        super(unaRutaArchivoBD);
    }

    /**
     * Busca un usuario por su nombre de usuario
     * @param nombre Nombre de usuario a buscar
     * @return UsuarioEntity si se encuentra, null si no existe
     */
    public UsuarioEntity obtenerUsuarioPorNombre(String nombre) {
        try {
            // Consulta SQL que une las tablas usuarios y niveles
            String sql = "SELECT u.email, u.user, u.password, n.nivel " +
                    "FROM usuarios u " +
                    "JOIN niveles n ON u.id_nivel = n.id " +
                    "WHERE u.user = '" + nombre + "'";

            ArrayList<UsuarioEntity> usuarios = obtenerUsuario(sql);
            if (usuarios.isEmpty()) {
                return null;
            }
            return usuarios.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Busca un usuario por su correo o nombre de usuario
     * @param informacion Email o nombre de usuario a buscar
     * @return UsuarioEntity si se encuentra, null si no existe
     */
    public UsuarioEntity obtenerDatosUsuario(String informacion) {
        try {
            // Primero busca por email
            String sql = "SELECT * FROM usuarios WHERE email='" + informacion + "'";
            ArrayList<UsuarioEntity> usuarios = obtenerUsuario(sql);
            if (usuarios.isEmpty()) {
                // Si no encuentra, busca por nombre de usuario
                sql = "SELECT * FROM usuarios WHERE user='" + informacion + "'";
                usuarios = obtenerUsuario(sql);
            }

            if (usuarios.isEmpty()) {
                return null;
            }

            return usuarios.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Busca un usuario por su dirección de correo electrónico
     * @param email Email del usuario a buscar
     * @return UsuarioEntity si se encuentra, null si no existe
     */
    public UsuarioEntity obtenerUsuarioPorEmail(String email) {
        try {
            String sql = "SELECT * FROM usuarios WHERE email='" + email + "'";
            ArrayList<UsuarioEntity> usuarios = obtenerUsuario(sql);
            if (usuarios.isEmpty()) {
                return null;
            }
            return usuarios.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema
     * @return Lista de todos los usuarios
     * @throws SQLException Si ocurre un error en la consulta SQL
     */
    public ArrayList<UsuarioEntity> obtenerUsuarios() throws SQLException {
        String sql = "SELECT u.user, u.email, u.password, n.nivel " +
                "FROM usuarios u JOIN niveles n ON u.id_nivel = n.id";
        return obtenerUsuario(sql);
    }

    /**
     * Método genérico para ejecutar consultas SQL que devuelven usuarios
     * @param sql Consulta SQL a ejecutar
     * @return Lista de usuarios que coinciden con la consulta
     * @throws SQLException Si ocurre un error en la consulta SQL
     */
    public ArrayList<UsuarioEntity> obtenerUsuario(String sql) throws SQLException {
        ArrayList<UsuarioEntity> usuarios = new ArrayList<>();
        try {
            PreparedStatement sentencia = getConnection().prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();

            // Recorrer los resultados y crear objetos UsuarioEntity
            while (resultado.next()) {
                String nombreStr = resultado.getString("user");
                String contraseniaStr = resultado.getString("password");
                String emailStr = resultado.getString("email");
                String nivelStr = resultado.getString("nivel");
                UsuarioEntity usuario = new UsuarioEntity(emailStr, nombreStr, contraseniaStr, nivelStr);
                usuarios.add(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrar();
        }
        return usuarios;
    }

    /**
     * Agrega un nuevo usuario a la base de datos
     * @param usuario Objeto UsuarioEntity con los datos del nuevo usuario
     * @return true si la inserción fue exitosa, false en caso contrario
     * @throws SQLException Si ocurre un error en la operación SQL
     */
    public boolean agregarUsuario(UsuarioEntity usuario) throws SQLException {
        if (usuario == null) {
            System.out.println("❌ Usuario es null, no se puede insertar.");
            return false;
        }

        System.out.println("🔍 Verificando si el usuario ya existe...");

        ArrayList<UsuarioEntity> usuarios = obtenerUsuarios();

        if (usuarios.contains(usuario)) {
            System.out.println("⚠️ Usuario ya existe en la base de datos: " + usuario.getEmail());
            return false;
        }

        // Inserta con id_nivel = 1 por defecto (nivel "fácil")
        String sql = "INSERT INTO usuarios (user, email, password, id_nivel) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement sentencia = getConnection().prepareStatement(sql);
            sentencia.setString(1, usuario.getNombre());
            sentencia.setString(2, usuario.getEmail());
            sentencia.setString(3, usuario.getContrasenia());
            sentencia.setInt(4, 1);

            int filas = sentencia.executeUpdate();
            System.out.println("Usuario insertado correctamente. Filas afectadas: " + filas);
            return true;
        } catch (Exception e) {
            System.out.println("Error al insertar usuario:");
            e.printStackTrace();
        } finally {
            cerrar();
        }

        return false;
    }

    /**
     * Actualiza el nivel de un usuario en la base de datos
     * @param usuario Objeto UsuarioEntity con el nivel actualizado
     * @throws SQLException Si ocurre un error en la operación SQL
     */
    public void actualizarNivelUsuario(UsuarioEntity usuario) throws SQLException {
        String sql = "UPDATE usuarios SET id_nivel = ? WHERE email = ?";
        PreparedStatement ps = getConnection().prepareStatement(sql);
        ps.setString(1, usuario.getNivel());
        ps.setString(2, usuario.getEmail());
        ps.executeUpdate();
        cerrar();
    }

}
