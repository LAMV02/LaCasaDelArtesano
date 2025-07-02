package org.example.lacasadelartesano.dao;

import org.example.lacasadelartesano.database.Database;
import org.example.lacasadelartesano.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public void guardar(Producto producto) {
        String sql = "INSERT INTO productos(nombre, descripcion, stock, precio, id_artesano, imagen) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setInt(3, producto.getInventario());
            stmt.setDouble(4, producto.getPrecio());
            stmt.setInt(5, producto.getArtesanoId());

            if (producto.getImagen() != null) {
                stmt.setBytes(6, producto.getImagen());
            } else {
                stmt.setNull(6, Types.BLOB);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Producto> obtenerPorArtesano(int artesanoId) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE id_artesano = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, artesanoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getInt("id_artesano"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("stock"),
                        rs.getDouble("precio"),
                        rs.getBytes("imagen")
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

    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getInt("id_artesano"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("stock"),
                        rs.getDouble("precio"),
                        rs.getBytes("imagen")
                );
                lista.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, stock = ?, precio = ?, imagen = ?, id_artesano = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setInt(3, producto.getInventario());
            stmt.setDouble(4, producto.getPrecio());
            stmt.setBytes(5, producto.getImagen());
            stmt.setInt(6, producto.getArtesanoId());
            stmt.setInt(7, producto.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
