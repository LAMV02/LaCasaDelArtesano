package org.example.lacasadelartesano;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.lacasadelartesano.database.Database;
import org.example.lacasadelartesano.ui.MainWindow;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Database.inicializar();

        MainWindow mainView = new MainWindow();
        Scene scene = new Scene(mainView, 900, 600);
        stage.setTitle("La Casa del Artesano - Punto de Venta");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
