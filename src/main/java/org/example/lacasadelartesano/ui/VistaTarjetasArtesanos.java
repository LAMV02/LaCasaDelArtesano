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
import org.example.lacasadelartesano.database.Database;
import org.example.lacasadelartesano.modelo.Artesano;
import org.example.lacasadelartesano.util.ImagenUtil;

import java.util.List;

public class VistaTarjetasArtesanos extends BorderPane {

    private final FlowPane contenedorTarjetas;

    public VistaTarjetasArtesanos() {
        contenedorTarjetas = new FlowPane();
        contenedorTarjetas.setHgap(15);
        contenedorTarjetas.setVgap(15);
        contenedorTarjetas.setPadding(new Insets(20));
        contenedorTarjetas.setAlignment(Pos.CENTER_LEFT);

        ScrollPane scroll = new ScrollPane(contenedorTarjetas);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color:transparent;");

        setCenter(scroll);
        cargarTarjetas();
    }

    private void cargarTarjetas() {
        contenedorTarjetas.getChildren().clear();

        // Tarjeta para agregar nuevo artesano
        VBox tarjetaAgregar = new VBox();
        tarjetaAgregar.setAlignment(Pos.CENTER);
        tarjetaAgregar.setSpacing(10);
        tarjetaAgregar.setPadding(new Insets(10));
        tarjetaAgregar.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 8; -fx-background-radius: 8;");
        tarjetaAgregar.setPrefSize(180, 220);

        ImageView iconoAgregar = new ImageView(new Image(getClass().getResourceAsStream("/img/add_image.jpg")));
        iconoAgregar.setFitWidth(60);
        iconoAgregar.setFitHeight(60);

        Text texto = new Text("Agregar Artesano");
        texto.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        tarjetaAgregar.getChildren().addAll(iconoAgregar, texto);
        tarjetaAgregar.setOnMouseClicked(e -> abrirFormularioNuevo());

        contenedorTarjetas.getChildren().add(tarjetaAgregar);

        // Tarjetas de artesanos existentes
        List<Artesano> lista = Database.obtenerArtesanos();
        for (Artesano artesano : lista) {
            TarjetaArtesano tarjeta = new TarjetaArtesano(
                    artesano,
                    () -> {
                        // Acción de editar
                        FormularioArtesano.mostrar(artesano, this::cargarTarjetas);
                    },
                    () -> {
                        // Acción de eliminar con confirmación
                        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar al artesano?", ButtonType.YES, ButtonType.NO);
                        alerta.showAndWait().ifPresent(respuesta -> {
                            if (respuesta == ButtonType.YES) {
                                Database.eliminarArtesano(artesano.getId());
                                cargarTarjetas();
                            }
                        });
                    },
                    this::abrirVistaDetalle
            );
            contenedorTarjetas.getChildren().add(tarjeta);
        }
    }


    private void abrirFormularioNuevo() {
        FormularioArtesano.mostrar(null, this::cargarTarjetas);
    }

    private void abrirVistaDetalle(Artesano artesano) {
        // TODO: Implementar VistaDetalleArtesano
        System.out.println("Ver detalles de: " + artesano.getNombre());
    }
}
