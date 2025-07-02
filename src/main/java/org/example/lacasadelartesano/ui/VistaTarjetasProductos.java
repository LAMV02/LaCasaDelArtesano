package org.example.lacasadelartesano.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.example.lacasadelartesano.dao.ProductoDAO;
import org.example.lacasadelartesano.modelo.Producto;
import org.example.lacasadelartesano.util.ImagenUtil;

import java.util.List;

public class VistaTarjetasProductos extends BorderPane {

    private final FlowPane contenedor;

    public VistaTarjetasProductos() {
        contenedor = new FlowPane();
        contenedor.setPadding(new Insets(20));
        contenedor.setHgap(20);
        contenedor.setVgap(20);
        contenedor.setAlignment(Pos.CENTER_LEFT);

        ScrollPane scroll = new ScrollPane(contenedor);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color:transparent;");

        setCenter(scroll);
        cargarTarjetas();
    }

    private void cargarTarjetas() {
        contenedor.getChildren().clear();

        // Tarjeta para agregar nuevo producto
        VBox tarjetaAgregar = new VBox();
        tarjetaAgregar.setAlignment(Pos.CENTER);
        tarjetaAgregar.setSpacing(10);
        tarjetaAgregar.setPadding(new Insets(10));
        tarjetaAgregar.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 8; -fx-background-radius: 8;");
        tarjetaAgregar.setPrefSize(180, 220);

        ImageView iconoAgregar = new ImageView(new Image(getClass().getResourceAsStream("/img/add_icon.jpg")));
        iconoAgregar.setFitWidth(60);
        iconoAgregar.setFitHeight(60);

        Text texto = new Text("Agregar Producto");
        texto.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        tarjetaAgregar.getChildren().addAll(iconoAgregar, texto);
        tarjetaAgregar.setOnMouseClicked((MouseEvent e) -> abrirFormularioNuevo());

        contenedor.getChildren().add(tarjetaAgregar);

        // Tarjetas de productos existentes
        List<Producto> lista = new ProductoDAO().obtenerTodos();
        for (Producto producto : lista) {
            VBox tarjeta = crearTarjetaProducto(producto);
            contenedor.getChildren().add(tarjeta);
        }
    }

    private VBox crearTarjetaProducto(Producto producto) {
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

        HBox botones = new HBox(10);
        botones.setAlignment(Pos.CENTER);

        javafx.scene.control.Button btnEditar = new javafx.scene.control.Button("✏️");
        btnEditar.setOnAction(e -> FormularioProducto.mostrar(producto, this::cargarTarjetas));

        javafx.scene.control.Button btnEliminar = new javafx.scene.control.Button("🗑️");
        btnEliminar.setOnAction(e -> {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar producto?", ButtonType.YES, ButtonType.NO);
            alerta.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.YES) {
                    new ProductoDAO().eliminarPorId(producto.getId());
                    cargarTarjetas();
                }
            });
        });

        botones.getChildren().addAll(btnEditar, btnEliminar);

        tarjeta.getChildren().addAll(imageView, nombre, descripcion, precio, stock, botones);
        return tarjeta;
    }

    private void abrirFormularioNuevo() {
        FormularioProducto.mostrar(null, this::cargarTarjetas);
    }
}
