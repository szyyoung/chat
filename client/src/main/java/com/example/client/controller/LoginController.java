package com.example.client.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.client.MainLaunch;
import com.example.client.client.ImClient;
import com.example.client.util.Constant;
import com.example.client.util.HttpUtil;
import com.example.client.util.ServerResponse;
import com.example.client.util.ThreadPoolUtil;
import com.example.client.util.ui.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Slf4j
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
        if (null == pho || pho.trim().length() == 0) {
            AlertUtil.showJfxDialog("请输入账号");
            return;
        }
        if (pwd == null || pwd.length() == 0) {
            AlertUtil.showJfxDialog("请输入密码");
            return;
        }
        JSONObject res = HttpUtil.post(Constant.LOGIN_URL, null, buildLoginMap(pho, pwd));
        if (null == res) {
            AlertUtil.showJfxDialog("网络异常，请稍后重试");
            return;
        }
        ServerResponse serverResponse = JSONObject.parseObject(res.toJSONString(), ServerResponse.class);
        if (!serverResponse.isSuccess()) {
            AlertUtil.showJfxDialog(serverResponse.getMsg());
            return;
        }
        //登录请求
        log.info("用户{},登录成功！", pho);
        Constant.HOLDER.put(Constant.TOKEN, serverResponse.getData());
        //获取IM服务器地址，建立连接，发送授权包
        ThreadPoolUtil.MSG_POOL.execute(new Runnable() {
            @Override
            public void run() {
                ImClient.init();
            }
        });


    }


    public Map<String, Object> buildLoginMap(String account, String password) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("account", account);
        map.put("password", password);
        return map;
    }


}
