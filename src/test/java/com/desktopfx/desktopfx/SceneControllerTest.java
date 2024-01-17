package com.desktopfx.desktopfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SceneControllerTest{

    @Test
    FXMLLoader switchScene() throws IOException {

        FXMLLoader fxmlLoader =  new FXMLLoader(Application.class.getResource("firstTime.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Desktop");
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(this.getClass().getResource("css/style.css").toExternalForm());
        stage.show();

        fxmlLoader = new FXMLLoader(Application.class.getResource("newUserScreen.fxml"));
        scene = new Scene(fxmlLoader.load(), 800, 600);

        scene.getStylesheets().add(this.getClass().getResource("css/style.css").toExternalForm());
        stage.setTitle("Desktop");
        stage.setScene(scene);
        stage.show();
        return fxmlLoader;
    }
}