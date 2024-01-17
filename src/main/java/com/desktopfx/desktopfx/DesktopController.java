package com.desktopfx.desktopfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static javafx.application.Platform.exit;

public class DesktopController {
    @FXML
    private Label username;

    @FXML
    private FlowPane desktopPane;

    @FXML
    private TextArea terminalArea;

    @FXML
    private Button openTerminalBtn;

    private String user;
    private boolean admin;

    public void initialize() {
        setUsername("Guest", false); // Default username, you can set it based on your requirements
        admin = false;

        // Set up a listener for Enter key press in the terminal
        terminalArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                executeCommand(terminalArea.getText().trim());
            }
        });
    }

    @FXML
    private void executeCommand(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read the output of the command and display it in the terminal
            String output = readProcessOutput(process);
            terminalArea.appendText("\n" + output);
        } catch (IOException e) {
            terminalArea.appendText("\nError executing command: " + e.getMessage());
        }
    }

    private String readProcessOutput(Process process) {
        // Read and return the output of the process
        StringBuilder output = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output.toString();
    }

    public void createWindow(String windowTitle, String content) {
        Label windowLabel = new Label(windowTitle);
        windowLabel.setFont(new Font(16));

        TextField textField = new TextField(content);
        textField.setPrefWidth(200);

        FlowPane windowContent = new FlowPane(windowLabel, textField);
        windowContent.setStyle("-fx-background-color: white; -fx-border-color: black;");
        windowContent.setPadding(new Insets(10));
        windowContent.setOnMouseClicked(event -> bringToFront(windowContent));

        desktopPane.getChildren().add(windowContent);

        // Center the window in the 7x10 GridPane
        double centerX = (7 * desktopPane.getWidth() - windowContent.getWidth()) / 2;
        double centerY = (10 * desktopPane.getHeight() - windowContent.getHeight()) / 2;
        windowContent.setLayoutX(centerX);
        windowContent.setLayoutY(centerY);
    }

    private void bringToFront(Node node) {
        desktopPane.getChildren().remove(node);
        desktopPane.getChildren().add(node);
    }

    @FXML
    public void openFileManager() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a Directory");

        File defaultDirectory = null;
        if(admin)
            defaultDirectory = new File("C:\\");
        else
            defaultDirectory = new File("C:\\Users\\sorin\\OneDrive\\Desktop\\manele");


        directoryChooser.setInitialDirectory(defaultDirectory);

        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null && isWithinAllowedPath(selectedDirectory, defaultDirectory)) {
            displayFiles(selectedDirectory);
        } else {
            // Handle invalid directory selection (outside of the allowed path)
            System.out.println("Invalid directory selection!");
        }
    }

    @FXML
    private void openTerminal() {
        // Show the terminalArea
        terminalArea.setVisible(true);
        terminalArea.setManaged(true);

        // Optionally, you can also set focus to the terminalArea
        terminalArea.requestFocus();

        // Create the window (if needed) or perform any other initialization
//        createWindow("Terminal", "");
    }

    private boolean isWithinAllowedPath(File selectedDirectory, File allowedPath) {
        // Check if the selected directory is within the allowed path
        try {
            String selectedCanonicalPath = selectedDirectory.getCanonicalPath();
            String allowedCanonicalPath = allowedPath.getCanonicalPath();
            return selectedCanonicalPath.startsWith(allowedCanonicalPath);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your requirements
            return false;
        }
    }

    private void displayFiles(File directory) {
        if (directory.isDirectory()) {
            Arrays.stream(directory.listFiles())
                    .forEach(file -> createWindow(file.getName(), file.getAbsolutePath()));
        }
    }

    public void kill() {
        exit();
    }

    public void setUsername(String username, boolean admin) {
        this.username.setText("Welcome " + username);
        this.username.setMinWidth(Region.USE_PREF_SIZE);
        user = username;
        this.admin = admin;
        openTerminalBtn.setVisible(admin);
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