package com.example.client.util.ui;

import com.example.client.MainLaunch;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertUtil {

    /* This displays an alert message to the user */
    public static void showErrorDialog(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText(message);
            alert.setContentText("Please check for firewall issues and check if the server is running.");
            alert.showAndWait();
        });

    }


    public static void showJfxDialog(String message) {
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setBody(new Label(message));
        layout.setMaxWidth(200);
        layout.setMaxHeight(5);
        layout.setPrefSize(200, 5);
        JFXAlert<Void> alert = new JFXAlert<>(MainLaunch.getPrimaryStage());
        alert.setOverlayClose(true);
        alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
        alert.setContent(layout);
        alert.initModality(Modality.NONE);
        alert.setTitle("提示");
        alert.showAndWait();
    }
}
