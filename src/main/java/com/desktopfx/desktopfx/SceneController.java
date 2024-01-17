package com.desktopfx.desktopfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    @FXML
    private TextField usernameField;

    private String username;

    @FXML
    private void changeToPass(javafx.scene.input.KeyEvent event) throws IOException{
        if (event.getCode() == KeyCode.ENTER) {
            username = usernameField.getText();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader fxmlLoader = switchScene(stage, "newPassScreen.fxml");

            PassController controller = fxmlLoader.getController();
            controller.setUsername(username);
        }
    }

    @FXML
    public FXMLLoader switchScene(Stage stage, String scenePath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(scenePath));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        scene.getStylesheets().add(this.getClass().getResource("css/style.css").toExternalForm());
        stage.setTitle("Desktop");
        stage.setScene(scene);
        stage.show();
        return fxmlLoader;
    }
}