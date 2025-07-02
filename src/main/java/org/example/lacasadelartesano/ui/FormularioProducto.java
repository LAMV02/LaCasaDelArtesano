package org.example.lacasadelartesano.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.lacasadelartesano.dao.ProductoDAO;
import org.example.lacasadelartesano.database.Database;
import org.example.lacasadelartesano.modelo.Artesano;
import org.example.lacasadelartesano.modelo.Producto;
import org.example.lacasadelartesano.util.ImagenUtil;

import java.io.File;
import java.util.List;

public class FormularioProducto extends VBox {

    private Producto producto;
    private Runnable onClose;
    private ComboBox<Artesano> comboArtesanos;
    private byte[] imagenSeleccionada = null;
    private ImageView vistaPrevia;

    public FormularioProducto(Producto producto, Runnable onClose) {
        this.producto = producto;
        this.onClose = onClose;

        setSpacing(12);
        setPadding(new Insets(20));

        boolean esEdicion = producto != null;

        Label titulo = new Label(esEdicion ? "Editar Producto" : "Agregar Producto");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del producto");

        TextArea txtDescripcion = new TextArea();
        txtDescripcion.setPromptText("Descripción");
        txtDescripcion.setPrefRowCount(3);

        TextField txtStock = new TextField();
        txtStock.setPromptText("Inventario");

        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("Precio");

        comboArtesanos = new ComboBox<>();
        comboArtesanos.setPromptText("Selecciona un artesano");
        comboArtesanos.setPrefWidth(300);
        cargarArtesanos();

        if (esEdicion) {
            txtNombre.setText(producto.getNombre());
            txtDescripcion.setText(producto.getDescripcion());
            txtStock.setText(String.valueOf(producto.getInventario()));
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
            imagenSeleccionada = producto.getImagen();

            if (imagenSeleccionada != null) {
                vistaPrevia = new ImageView(ImagenUtil.convertirBytesAImagen(imagenSeleccionada));
            }
        }

        vistaPrevia = vistaPrevia != null ? vistaPrevia : new ImageView();
        vistaPrevia.setFitWidth(120);
        vistaPrevia.setFitHeight(120);
        vistaPrevia.setPreserveRatio(true);

        if (esEdicion && producto.getArtesanoId() > 0) {
            comboArtesanos.getSelectionModel().select(
                    comboArtesanos.getItems().stream()
                            .filter(a -> a.getId() == producto.getArtesanoId())
                            .findFirst().orElse(null)
            );
        }

        Button btnImagen = new Button("Seleccionar Imagen");
        btnImagen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar imagen");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
            );
            File archivo = fileChooser.showOpenDialog(getScene().getWindow());
            if (archivo != null) {
                imagenSeleccionada = ImagenUtil.convertirArchivoABytes(archivo);
                vistaPrevia.setImage(new Image(archivo.toURI().toString()));
            }
        });

        Label lblMensaje = new Label();

        Button btnGuardar = new Button(esEdicion ? "Actualizar" : "Guardar");
        btnGuardar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnGuardar.setOnAction(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                String descripcion = txtDescripcion.getText().trim();
                int stock = Integer.parseInt(txtStock.getText().trim());
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                Artesano artesano = comboArtesanos.getValue();

                if (nombre.isEmpty() || artesano == null) {
                    lblMensaje.setText("Todos los campos obligatorios deben llenarse.");
                    return;
                }

                ProductoDAO dao = new ProductoDAO();

                if (esEdicion) {
                    producto.setNombre(nombre);
                    producto.setDescripcion(descripcion);
                    producto.setStock(stock);
                    producto.setPrecio(precio);
                    producto.setIdArtesano(artesano.getId());
                    producto.setImagen(imagenSeleccionada);
                    dao.actualizar(producto);
                } else {
                    Producto nuevo = new Producto(
                            artesano.getId(), nombre, descripcion, stock, precio, imagenSeleccionada
                    );
                    dao.guardar(nuevo);
                }

                if (onClose != null) onClose.run();
                ((Stage) getScene().getWindow()).close();

            } catch (NumberFormatException ex) {
                lblMensaje.setText("Stock y precio deben ser valores numéricos.");
            }
        });

        getChildren().addAll(
                titulo, comboArtesanos,
                txtNombre, txtDescripcion, txtStock, txtPrecio,
                btnImagen, vistaPrevia,
                btnGuardar, lblMensaje
        );
    }

    private void cargarArtesanos() {
        List<Artesano> lista = Database.obtenerArtesanos();
        comboArtesanos.getItems().addAll(lista);
    }

    public static void mostrar(Producto producto, Runnable onClose) {
        FormularioProducto formulario = new FormularioProducto(producto, onClose);
        Stage ventana = new Stage();
        ventana.setScene(new Scene(formulario));
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle(producto == null ? "Nuevo Producto" : "Editar Producto");
        ventana.showAndWait();
    }
}
