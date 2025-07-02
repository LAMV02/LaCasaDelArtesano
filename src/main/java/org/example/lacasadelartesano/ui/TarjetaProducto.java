package org.example.lacasadelartesano.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.example.lacasadelartesano.dao.ProductoDAO;
import org.example.lacasadelartesano.modelo.Producto;
import org.example.lacasadelartesano.util.ImagenUtil;

public class TarjetaProducto extends VBox {

    public TarjetaProducto(Producto producto, Runnable onActualizar) {
        setSpacing(10);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);
        setPrefSize(200, 260);
        setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8;");

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
        btnEditar.setOnAction(e -> FormularioProducto.mostrar(producto, onActualizar));

        Button btnEliminar = new Button("🗑️");
        btnEliminar.setOnAction(e -> {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar producto?", ButtonType.YES, ButtonType.NO);
            alerta.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.YES) {
                    new ProductoDAO().eliminarPorId(producto.getId());
                    onActualizar.run();
                }
            });
        });

        HBox botones = new HBox(5, btnEditar, btnEliminar);
        botones.setAlignment(Pos.CENTER);

        getChildren().addAll(imageView, nombre, descripcion, precio, stock, botones);
    }
}
