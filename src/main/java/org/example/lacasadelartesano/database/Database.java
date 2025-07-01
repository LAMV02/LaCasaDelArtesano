package org.example.lacasadelartesano.database;

import java.sql.*;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.lacasadelartesano.dao.ArtesanoDAO;
import org.example.lacasadelartesano.modelo.Artesano;

import java.util.ArrayList;
import java.util.List;


public class Database {
    private static final String URL = "jdbc:sqlite:database.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void inicializar() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Eliminar si existen (solo para desarrollo)
            stmt.execute("DROP TABLE IF EXISTS productos;");
            stmt.execute("DROP TABLE IF EXISTS artesanos;");

            // Crear tabla ARTESANO con imagen
            String crearArtesano = """
        CREATE TABLE artesanos (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            negocio TEXT NOT NULL,
            telefono TEXT,
            imagen BLOB
        );
        """;

            // Crear tabla PRODUCTO con imagen
            String crearProducto = """
        CREATE TABLE productos (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            id_artesano INTEGER NOT NULL,
            nombre TEXT NOT NULL,
            descripcion TEXT,
            stock INTEGER DEFAULT 0,
            precio REAL NOT NULL,
            imagen BLOB,
            FOREIGN KEY(id_artesano) REFERENCES artesanos(id) ON DELETE CASCADE
        );
        """;

            stmt.execute(crearArtesano);
            stmt.execute(crearProducto);

            System.out.println("Base de datos inicializada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al inicializar la base de datos:");
            e.printStackTrace();
        }
    }

    public static boolean insertarArtesano(Artesano artesano) {
        String sql = "INSERT INTO artesanos (nombre, negocio, telefono, imagen) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, artesano.getNombre());
            pstmt.setString(2, artesano.getNegocio());
            pstmt.setString(3, artesano.getTelefono());
            pstmt.setBytes(4, artesano.getImagen());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar artesano:");
            e.printStackTrace();
            return false;
        }
    }

    public static List<Artesano> obtenerArtesanos() {
        List<Artesano> lista = new ArrayList<>();

        String sql = "SELECT * FROM artesanos";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
            System.out.println("Error al obtener artesanos:");
            e.printStackTrace();
        }

        return lista;
    }

    public static void eliminarArtesano(int artesano) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que deseas eliminar al artesano?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                ArtesanoDAO.eliminarPorId(artesano);

            }
        });
    }


    public static void actualizarArtesano(Artesano artesano) {
        String sql = "UPDATE artesanos SET nombre=?, negocio=?, telefono=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artesano.getNombre());
            stmt.setString(2, artesano.getNegocio());
            stmt.setString(3, artesano.getTelefono());
            stmt.setInt(4, artesano.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
