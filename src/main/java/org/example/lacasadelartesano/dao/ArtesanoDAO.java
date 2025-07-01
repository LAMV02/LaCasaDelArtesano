package org.example.lacasadelartesano.dao;

import org.example.lacasadelartesano.database.Database;
import org.example.lacasadelartesano.modelo.Artesano;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtesanoDAO {

    public void guardar(Artesano artesano) {
        String sql = "INSERT INTO artesanos(nombre, negocio, telefono) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artesano.getNombre());
            stmt.setString(2, artesano.getNegocio());
            stmt.setString(3, artesano.getTelefono());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Artesano> obtenerTodos() {
        List<Artesano> lista = new ArrayList<>();
        String sql = "SELECT * FROM artesanos";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Artesano artesano = new Artesano(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("negocio"),
                        rs.getString("telefono"),
                        rs.getBytes("imagen")
                );
                lista.add(artesano);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static void eliminarPorId(int id) {
        String sql = "DELETE FROM artesanos WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Otros métodos como actualizar o buscar por ID se pueden añadir si es necesario
}
