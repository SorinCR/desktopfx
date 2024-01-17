package com.desktopfx.desktopfx;

import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Application extends javafx.application.Application {

    List<String> users;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException, SQLException {

        users = new ArrayList<>();

        Connection connection = Database.getDBConnection();
        String sql = "SELECT * FROM users";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        while(rs.next()) {
            users.add(rs.getString("username"));
        }

        if(users.size() == 0) {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("firstTime.fxml"));
            SceneController controller = new SceneController();
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            scene.getStylesheets().add(this.getClass().getResource("css/style.css").toExternalForm());
            stage.setTitle("Desktop");
            stage.setScene(scene);
            stage.show();

            stage.setMinWidth(800);
            stage.setMinHeight(600);

            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> {
                try {
                    controller.switchScene(stage, "newUserScreen.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            delay.play();
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("userLogin.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            scene.getStylesheets().add(this.getClass().getResource("css/style.css").toExternalForm());

            UserController controller = fxmlLoader.getController();
            controller.loadUsers(users);

            stage.setTitle("Desktop");
            stage.setScene(scene);
            stage.show();

            stage.setMinWidth(800);
            stage.setMinHeight(600);
        }


    }

    public static void main(String[] args) {
        launch();
    }
}