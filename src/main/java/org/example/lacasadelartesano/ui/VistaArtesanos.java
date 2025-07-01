package org.example.lacasadelartesano.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.lacasadelartesano.database.Database;
import org.example.lacasadelartesano.modelo.Artesano;

import java.util.List;

public class VistaArtesanos extends VBox {

    private final FlowPane contenedorTarjetas;

    public VistaArtesanos() {
        setSpacing(20);
        setPadding(new Insets(20));

        contenedorTarjetas = new FlowPane();
        contenedorTarjetas.setHgap(20);
        contenedorTarjetas.setVgap(20);
        contenedorTarjetas.setPadding(new Insets(10));
        contenedorTarjetas.setPrefWrapLength(800);

        getChildren().add(contenedorTarjetas);

        actualizarVista();
    }

    private void actualizarVista() {
        contenedorTarjetas.getChildren().clear();

        // Tarjeta de "Agregar Artesano"
        VBox tarjetaAgregar = new VBox(10);
        tarjetaAgregar.setPrefSize(180, 220);
        tarjetaAgregar.setAlignment(Pos.CENTER);
        tarjetaAgregar.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 8; -fx-background-radius: 8;");
        tarjetaAgregar.setOnMouseClicked((MouseEvent e) -> abrirFormularioAgregar());

        Image imagenIcono = new Image(getClass().getResourceAsStream("/img/add_icon.png"));
        ImageView icono = new ImageView(imagenIcono);
        icono.setFitHeight(64);
        icono.setFitWidth(64);

        Text texto = new Text("Agregar Artesano");
        texto.setFont(Font.font(14));

        tarjetaAgregar.getChildren().addAll(icono, texto);
        contenedorTarjetas.getChildren().add(tarjetaAgregar);

        // Tarjetas de artesanos
        List<Artesano> artesanos = Database.obtenerArtesanos();
        for (Artesano a : artesanos) {
            Artesano artesanoFinal = a;
            TarjetaArtesano tarjeta = new TarjetaArtesano(
                    artesanoFinal,
                    () -> editarArtesano(artesanoFinal),  // onEditar
                    () -> eliminarArtesano(artesanoFinal), // onEliminar
                    artesano -> abrirVistaDetalle(artesanoFinal)  // onVerDetalle
            );
            contenedorTarjetas.getChildren().add(tarjeta);
        }
    }

    private void abrirFormularioAgregar() {
        FormularioArtesano.mostrar(null, this::actualizarVista);
    }

    private void editarArtesano(Artesano artesano) {
        FormularioArtesano.mostrar(artesano, this::actualizarVista);
    }

    private void eliminarArtesano(Artesano artesano) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar al artesano?", ButtonType.YES, ButtonType.NO);
        alerta.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                Database.eliminarArtesano(artesano.getId());
                actualizarVista();
            }
        });
    }

    private void abrirVistaDetalle(Artesano artesano) {
        // TODO: implementar vista de detalle con productos asociados
        System.out.println("Ver detalles de: " + artesano.getNombre());
    }
}
