package com.example.client.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class AlertUtil {

    /* This displays an alert message to the user */
    public static void showErrorDialog(String message) {
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText(message);
            alert.setContentText("Please check for firewall issues and check if the server is running.");
            alert.showAndWait();
        });

    }
}
