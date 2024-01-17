package com.desktopfx.desktopfx;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTests {

    @Test
    boolean testConnection() throws SQLException {
        Connection connection = Database.getDBConnection();
        return true;
    }

    @Test
    boolean testSelect() {
        try (Connection connection = Database.getDBConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ? ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, "test");
                statement.setString(2, "123");

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        boolean admin = rs.getString("admin").equalsIgnoreCase("1");

                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return false;
    }

}
