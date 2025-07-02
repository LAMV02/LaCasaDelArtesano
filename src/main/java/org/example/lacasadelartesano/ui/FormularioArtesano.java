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
import org.example.lacasadelartesano.database.Database;
import org.example.lacasadelartesano.modelo.Artesano;
import org.example.lacasadelartesano.util.ImagenUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

public class FormularioArtesano extends VBox {

    private Artesano artesano;
    private Runnable onClose;
    private byte[] imagenSeleccionada;
    private ImageView vistaPrevia;

    public FormularioArtesano(Artesano artesano, Runnable onClose) {
        this.artesano = artesano;
        this.onClose = onClose;

        setSpacing(12);
        setPadding(new Insets(20));
        crearContenido();
    }

    private void crearContenido() {
        boolean esEdicion = artesano != null;

        Label titulo = new Label(esEdicion ? "Editar Artesano" : "Agregar Artesano");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del artesano");

        TextField txtNegocio = new TextField();
        txtNegocio.setPromptText("Nombre del negocio");

        TextField txtTelefono = new TextField();
        txtTelefono.setPromptText("Teléfono");

        if (esEdicion) {
            txtNombre.setText(artesano.getNombre());
            txtNegocio.setText(artesano.getNegocio());
            txtTelefono.setText(artesano.getTelefono());
        }

        // Vista previa de imagen
        vistaPrevia = new ImageView();
        vistaPrevia.setFitWidth(120);
        vistaPrevia.setFitHeight(120);
        vistaPrevia.setPreserveRatio(true);
        vistaPrevia.setStyle("-fx-border-color: #ccc;");

        if (esEdicion && artesano.getImagen() != null) {
            vistaPrevia.setImage(ImagenUtil.convertirBytesAImagen(artesano.getImagen()));
        } else {
            vistaPrevia.setImage(ImagenUtil.cargarImagenPorDefecto());
        }

        Button btnSeleccionarImagen = new Button("Seleccionar imagen");
        btnSeleccionarImagen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar imagen");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
            );
            File archivo = fileChooser.showOpenDialog(getScene().getWindow());
            if (archivo != null) {
                try {
                    imagenSeleccionada = Files.readAllBytes(archivo.toPath());
                    vistaPrevia.setImage(new Image(new ByteArrayInputStream(imagenSeleccionada)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    mostrarError("Error al cargar imagen.");
                }
            }
        });

        Button btnGuardar = new Button(esEdicion ? "Actualizar" : "Guardar");
        btnGuardar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");

        Label lblMensaje = new Label();

        btnGuardar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String negocio = txtNegocio.getText().trim();
            String telefono = txtTelefono.getText().trim();

            if (nombre.isEmpty() || negocio.isEmpty() || telefono.isEmpty()) {
                lblMensaje.setText("Por favor, llena todos los campos.");
                return;
            }

            if (esEdicion) {
                artesano.setNombre(nombre);
                artesano.setNegocio(negocio);
                artesano.setTelefono(telefono);
                if (imagenSeleccionada != null) {
                    artesano.setImagen(imagenSeleccionada);
                }
                Database.actualizarArtesano(artesano);
                lblMensaje.setText("Artesano actualizado.");
            } else {
                Artesano nuevo = new Artesano(nombre, negocio, telefono);
                nuevo.setImagen(imagenSeleccionada != null ? imagenSeleccionada : ImagenUtil.obtenerBytesImagenPorDefecto());
                Database.insertarArtesano(nuevo);
                lblMensaje.setText("Artesano agregado.");
            }

            if (onClose != null) onClose.run();
            ((Stage) getScene().getWindow()).close();
        });

        getChildren().addAll(
                titulo, txtNombre, txtNegocio, txtTelefono,
                vistaPrevia, btnSeleccionarImagen,
                btnGuardar, lblMensaje
        );
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        alerta.showAndWait();
    }

    public static void mostrar(Artesano artesano, Runnable onClose) {
        FormularioArtesano formulario = new FormularioArtesano(artesano, onClose);
        Stage ventana = new Stage();
        ventana.setScene(new Scene(formulario));
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle(artesano == null ? "Agregar Artesano" : "Editar Artesano");
        ventana.showAndWait();
    }
}
