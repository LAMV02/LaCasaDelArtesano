package org.example.lacasadelartesano.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class MainWindow extends BorderPane {

    public MainWindow() {
        // Menú izquierdo
        VBox menu = new VBox(15);
        menu.setPadding(new Insets(20));
        menu.setPrefWidth(220);
        menu.setStyle("-fx-background-color: #f0f4f8;");

        Button btnAgregarProducto = new Button("Agregar Producto");
        Button btnVerArtesanos = new Button("Ver Artesanos");
        Button btnVerProductos = new Button("Ver Productos");

        for (Button b : new Button[]{ btnAgregarProducto, btnVerArtesanos, btnVerProductos}) {
            b.setFont(Font.font(18));
            b.setPrefWidth(Double.MAX_VALUE);
            b.setMinHeight(60);
            b.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white; -fx-font-weight: bold;");
            b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #16a085; -fx-text-fill: white; -fx-font-weight: bold;"));
            b.setOnMouseExited(e -> b.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white; -fx-font-weight: bold;"));
        }

        menu.getChildren().addAll(btnAgregarProducto, btnVerArtesanos, btnVerProductos);

        this.setLeft(menu);

        // Área derecha para contenido dinámico
        StackPane contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));
        this.setCenter(contentArea);



        btnAgregarProducto.setOnAction(e -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(new VistaProductos());

        });

        btnVerArtesanos.setOnAction(e -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(new VistaTarjetasArtesanos());
        });

        btnVerProductos.setOnAction(e -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(new VistaTarjetasProductos());
        });

    }
}
