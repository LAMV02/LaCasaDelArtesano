package org.example.lacasadelartesano.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.lacasadelartesano.database.Database;
import org.example.lacasadelartesano.modelo.Artesano;

public class FormularioArtesano extends VBox {

    private Artesano artesano;
    private Runnable onClose;

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
                Database.actualizarArtesano(artesano); // Asegúrate de implementar esto
                lblMensaje.setText("Artesano actualizado.");
            } else {
                Artesano nuevo = new Artesano(nombre, negocio, telefono);
                Database.insertarArtesano(nuevo);
                lblMensaje.setText("Artesano agregado.");
            }

            if (onClose != null) onClose.run();
            ((Stage) getScene().getWindow()).close();
        });

        getChildren().addAll(titulo, txtNombre, txtNegocio, txtTelefono, btnGuardar, lblMensaje);
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
