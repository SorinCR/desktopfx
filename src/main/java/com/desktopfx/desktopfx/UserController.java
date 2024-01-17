package com.desktopfx.desktopfx;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.List;

import static javafx.application.Platform.exit;

public class UserController {
    @FXML
    private Label user;

    @FXML
    private PasswordField passField;

    @FXML
    private Text currentUsername;

    @FXML
    private Text currentUserInitial;

    @FXML
    private Label errorLabel;

    @FXML
    private GridPane userList;

    public void handleUserCircleClick(String u) {
        user.setText(u);
    }

    public void loadUsers(List<String> users) {
        for(int i = 0; i < users.size(); i++) {
            String u = users.get(i);
            if(i == 0) {
                user.setText(u);
                currentUsername.setText(u);
                currentUserInitial.setText(String.valueOf(u.charAt(0)));
            }

            Circle userCircle = new Circle(20.0, Color.TRANSPARENT);
            userCircle.setStroke(Color.BLACK);
            userCircle.setCursor(Cursor.CLOSED_HAND);
            userCircle.setOnMouseClicked(event -> handleUserCircleClick(u));

            Text userNameText = new Text(u);
            userNameText.setFont(new Font(25.0));
            userNameText.setCursor(Cursor.CLOSED_HAND);
            userNameText.setOnMouseClicked(event -> handleUserCircleClick(u));

            userList.add(userCircle, 0, i);
            userList.add(userNameText, 1, i);
            userCircle.setTranslateX(25);
            userCircle.setTranslateY(30);

            Text circleText = new Text(String.valueOf(u.charAt(0)));
            circleText.setFont(new Font(25.0));
            circleText.setFill(Color.BLACK);
            circleText.setTextAlignment(TextAlignment.CENTER);
            circleText.setCursor(Cursor.CLOSED_HAND);
            circleText.setOnMouseClicked(event -> handleUserCircleClick(u));

            Group circleWithText = new Group(userCircle, circleText);


            userNameText.setTranslateX(0);
            userNameText.setTranslateY(70);

            userList.add(circleWithText, 0, i);
            GridPane.setMargin(circleWithText, new Insets(0, 0, -100, 10));
            GridPane.setHalignment(circleWithText, HPos.LEFT);
        }
    }

    @FXML
    public void login(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String password = passField.getText();
            String username = this.user.getText();

            Task<Void> loginTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try (Connection connection = Database.getDBConnection()) {
                        String sql = "SELECT * FROM users WHERE username = ? AND password = ? ";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setString(1, username);
                            statement.setString(2, password);

                            try (ResultSet rs = statement.executeQuery()) {
                                if (rs.next()) {
                                    boolean admin = rs.getString("admin").equalsIgnoreCase("1");

                                    Platform.runLater(() -> {
                                        try {
                                            Stage stage = (Stage) passField.getScene().getWindow();
                                            FXMLLoader fxmlLoader = switchScene(stage, "desktopScreen.fxml");
                                            DesktopController controller = fxmlLoader.getController();
                                            controller.setUsername(username, admin);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                } else {

                                    Platform.runLater(() -> errorLabel.setText("User and password combination doesn't exist"));
                                }
                            }
                        }
                    } catch (SQLException e) {

                        e.printStackTrace();
                    }

                    return null;
                }
            };


            Thread thread = new Thread(loginTask);
            thread.setDaemon(true);
            thread.start();
        }
    }

    @FXML
    public void newUser() throws IOException {
        Stage stage = (Stage) passField.getScene().getWindow();
        FXMLLoader fxmlLoader = switchScene(stage, "newUserScreen.fxml");
    }

    @FXML
    public void setCredentials(String username, String password) {
        Task<Void> setCredentialsTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try (Connection connection = Database.getDBConnection()) {

                    String query = "INSERT INTO users (username, password, admin) VALUES (?, ?, ?)";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setString(1, username);
                        statement.setString(2, password);
                        statement.setInt(3, 0);

                        int affectedRows = statement.executeUpdate();
                        System.out.println("Rows affected: " + affectedRows);
                    }
                } catch (SQLException e) {

                    e.printStackTrace();
                }


                Platform.runLater(() -> {

                    user.setText(username);
                    user.setMinWidth(Region.USE_PREF_SIZE);
                    currentUsername.setText(username);
                    currentUserInitial.setText(String.valueOf(username.charAt(0)));
                });

                return null;
            }
        };


        Thread thread = new Thread(setCredentialsTask);
        thread.setDaemon(true);
        thread.start();
    }


    public void kill() {
        exit();
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