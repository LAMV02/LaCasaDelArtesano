package org.example.lacasadelartesano.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.example.lacasadelartesano.database.Database;
import org.example.lacasadelartesano.modelo.Artesano;
import org.example.lacasadelartesano.util.ImagenUtil;

import java.util.function.Consumer;

public class TarjetaArtesano extends VBox {

    private Artesano artesano;
    private Runnable onEditar;
    private Runnable onEliminar;
    private Consumer<Artesano> onVerDetalle;

    public TarjetaArtesano(Artesano artesano, Runnable onEditar, Runnable onEliminar, Consumer<Artesano> onVerDetalle) {
        this.artesano = artesano;
        this.onEditar = onEditar;
        this.onEliminar = onEliminar;
        this.onVerDetalle = onVerDetalle;

        setPadding(new Insets(10));
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setPrefWidth(180);
        setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 8; -fx-background-radius: 8;");
        crearContenido();
    }

    private void crearContenido() {
        // Imagen
        Image imagen = ImagenUtil.convertirBytesAImagen(artesano.getImagen());
        if (imagen == null) {
            imagen = ImagenUtil.cargarImagenPorDefecto();
        }

        ImageView imageView = new ImageView(imagen);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        // Info
        Label lblNombre = new Label(artesano.getNombre());
        Label lblNegocio = new Label(artesano.getNegocio());
        Label lblTelefono = new Label("📞 " + artesano.getTelefono());

        // Botones
        HBox botones = new HBox(5);
        botones.setAlignment(Pos.CENTER);

        Button btnEditar = new Button("✏️");
        btnEditar.setOnAction(e -> onEditar.run());

        Button btnEliminar = new Button("🗑️");
        btnEliminar.setOnAction(e -> {
            Database.eliminarArtesano(artesano.getId());
            onEliminar.run(); // recargar tarjetas
        });

        botones.getChildren().addAll(btnEditar, btnEliminar);

        setOnMouseClicked(e -> onVerDetalle.accept(artesano)); // click en la tarjeta completa

        getChildren().addAll(imageView, lblNombre, lblNegocio, lblTelefono, botones);
    }

    public Artesano getArtesano() {
        return artesano;
    }
}
