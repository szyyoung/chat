package com.example.client.controller;

import com.example.client.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private ImageView logoImg;

    @FXML
    public TextField passwordField;

    @FXML
    public TextField accountField;

    @FXML
    public Label forgetPwdLabel;

    @FXML
    public Label registeLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void login() {
        String pho = accountField.getText();
        String pwd = passwordField.getText();
        if (null == pho || pho.trim().length() != 11) {
            AlertUtil.showErrorDialog("请输入手机号");
        }
        if (pwd == null || pwd.length() == 0) {
            AlertUtil.showErrorDialog("请输入密码");
        }
        //todo
        //登录请求
        System.out.println("登录成功！！！");


    }
}
