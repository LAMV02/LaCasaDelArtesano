package org.example.lacasadelartesano.dao;

import org.example.lacasadelartesano.database.Database;
import org.example.lacasadelartesano.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public void guardar(Producto producto) {
        String sql = "INSERT INTO productos(nombre, descripcion, inventario, precio, artesano_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setInt(3, producto.getInventario());
            stmt.setDouble(4, producto.getPrecio());
            stmt.setInt(5, producto.getArtesanoId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Producto> obtenerPorArtesano(int artesanoId) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE artesano_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, artesanoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("id"),              // id
                        rs.getInt("artesano_id"),     // idArtesano
                        rs.getString("nombre"),       // nombre
                        rs.getString("descripcion"),  // descripcion
                        rs.getInt("inventario"),      // stock
                        rs.getDouble("precio")        // precio
                );
                lista.add(producto);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void eliminarPorId(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Puedes añadir actualizar o buscar por ID si quieres
}
