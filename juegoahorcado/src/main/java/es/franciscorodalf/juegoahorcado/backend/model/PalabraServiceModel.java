package es.franciscorodalf.juegoahorcado.backend.model;

import es.franciscorodalf.juegoahorcado.backend.model.abstractas.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PalabraServiceModel extends Conexion {

    public PalabraServiceModel() {
        super();
    }

    public String obtenerPalabraAleatoriaPorNivel(String nivelTexto) throws SQLException {
        String sql = """
                SELECT palabra FROM palabras 
                JOIN niveles ON palabras.id_nivel = niveles.id 
                WHERE niveles.nivel = ?
            """;

        List<String> palabras = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, nivelTexto.toLowerCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                palabras.add(rs.getString("palabra"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener palabras: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrar();
        }

        if (palabras.isEmpty()) {
            throw new SQLException("No se encontraron palabras para el nivel: " + nivelTexto);
        }

        Random random = new Random();
        return palabras.get(random.nextInt(palabras.size()));
    }
}
