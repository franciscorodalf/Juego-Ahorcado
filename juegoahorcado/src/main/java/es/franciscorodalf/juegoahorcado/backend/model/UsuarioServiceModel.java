package es.franciscorodalf.juegoahorcado.backend.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import es.franciscorodalf.juegoahorcado.backend.model.abstractas.Conexion;

public class UsuarioServiceModel extends Conexion {

    public UsuarioServiceModel() {
    }

    public UsuarioServiceModel(String unaRutaArchivoBD) throws SQLException {
        super(unaRutaArchivoBD);
    }

    public UsuarioEntity obtenerUsuarioPorNombre(String nombre) {
        try {
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

    public UsuarioEntity obtenerDatosUsuario(String informacion) {
        try {
            String sql = "SELECT * FROM usuarios WHERE email='" + informacion + "'";
            ArrayList<UsuarioEntity> usuarios = obtenerUsuario(sql);
            if (usuarios.isEmpty()) {
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

    public ArrayList<UsuarioEntity> obtenerUsuarios() throws SQLException {
        String sql = "SELECT u.user, u.email, u.password, n.nivel " +
                "FROM usuarios u JOIN niveles n ON u.id_nivel = n.id";
        return obtenerUsuario(sql);
    }

    public ArrayList<UsuarioEntity> obtenerUsuario(String sql) throws SQLException {
        ArrayList<UsuarioEntity> usuarios = new ArrayList<>();
        try {
            PreparedStatement sentencia = getConnection().prepareStatement(sql);
            ResultSet resultado = sentencia.executeQuery();

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

    public void actualizarNivelUsuario(UsuarioEntity usuario) throws SQLException {
        String sql = "UPDATE usuarios SET id_nivel = ? WHERE email = ?";
        PreparedStatement ps = getConnection().prepareStatement(sql);
        ps.setString(1, usuario.getNivel());
        ps.setString(2, usuario.getEmail());
        ps.executeUpdate();
        cerrar();
    }

}
