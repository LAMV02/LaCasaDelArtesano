package org.example.lacasadelartesano.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.example.lacasadelartesano.dao.ProductoDAO;
import org.example.lacasadelartesano.modelo.Producto;
import org.example.lacasadelartesano.util.ImagenUtil;

import java.util.List;

public class VistaProductos extends ScrollPane {

    private final FlowPane contenedor;

    public VistaProductos() {
        contenedor = new FlowPane();
        contenedor.setPadding(new Insets(20));
        contenedor.setHgap(20);
        contenedor.setVgap(20);
        contenedor.setAlignment(Pos.TOP_LEFT);
        contenedor.setPrefWidth(800); // fuerza tamaño
        contenedor.setPrefHeight(600);

        setContent(contenedor);
        setFitToWidth(true);
        setPrefHeight(600); // también el scroll

        cargarProductos();
    }

    private void cargarProductos() {
        contenedor.getChildren().clear();

        ProductoDAO dao = new ProductoDAO();
        List<Producto> productos = dao.obtenerTodos(); // este método lo implementaremos si no está

        for (Producto p : productos) {
            VBox tarjeta = crearTarjeta(p);
            contenedor.getChildren().add(tarjeta);


        }

        if (productos.isEmpty()) {
            VBox tarjetaMock = new VBox(10);
            tarjetaMock.setStyle("-fx-background-color: #eee; -fx-padding: 20px;");
            tarjetaMock.setPrefSize(200, 200);
            tarjetaMock.setAlignment(Pos.CENTER);
            tarjetaMock.getChildren().add(new Text("No hay productos"));
            contenedor.getChildren().add(tarjetaMock);
        }
    }

    private VBox crearTarjeta(Producto producto) {
        VBox tarjeta = new VBox(10);
        tarjeta.setPrefSize(200, 260);
        tarjeta.setPadding(new Insets(10));
        tarjeta.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8;");
        tarjeta.setAlignment(Pos.CENTER);

        Image img = ImagenUtil.convertirBytesAImagen(producto.getImagen());
        if (img == null) img = ImagenUtil.cargarImagenPorDefecto();

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        Text nombre = new Text(producto.getNombre());
        Text descripcion = new Text(producto.getDescripcion());
        Text precio = new Text(String.format("$ %.2f", producto.getPrecio()));
        Text stock = new Text("Stock: " + producto.getInventario());

        Button btnEditar = new Button("✏️");
        btnEditar.setOnAction(e -> {
            FormularioProducto.mostrar(producto, this::cargarProductos);
        });

        Button btnEliminar = new Button("🗑️");
        btnEliminar.setOnAction(e -> {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar producto?", ButtonType.YES, ButtonType.NO);
            alerta.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.YES) {
                    new ProductoDAO().eliminarPorId(producto.getId());
                    cargarProductos();
                }
            });
        });

        HBox botones = new HBox(5, btnEditar, btnEliminar);
        botones.setAlignment(Pos.CENTER);

        tarjeta.getChildren().addAll(imageView, nombre, descripcion, precio, stock, botones);
        return tarjeta;
    }
}
