package com.desktopfx.desktopfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class PassController {
    @FXML
    private Label nameField;

    @FXML
    private PasswordField passField;

    private String username;
    private String password;

    @FXML
    private void changeToLogin(javafx.scene.input.KeyEvent event) throws IOException, SQLException {
        if (event.getCode() == KeyCode.ENTER) {
            password = passField.getText();
            Stage stage = (Stage) passField.getScene().getWindow();
            FXMLLoader fxmlLoader = switchScene(stage, "userLogin.fxml");

            UserController controller = fxmlLoader.getController();
            controller.setCredentials(username, password);
        }
    }

    @FXML
    public void setUsername(String username) {
        this.username = username;
        String text = "Hello, " + username;
        nameField.setText(text);
        nameField.setMinWidth(Region.USE_PREF_SIZE);
//        nameField.prefColumnCountProperty?().bind(nameField.textProperty().length());
    }

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