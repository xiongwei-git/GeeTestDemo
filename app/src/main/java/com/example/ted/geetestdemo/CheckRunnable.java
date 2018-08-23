package com.example.ted.geetestdemo;

import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckRunnable implements Runnable {
    private String phone;
    private String challenge;
    private Handler handler;

    public CheckRunnable(Handler handler, String phone, String challenge) {
        this.handler = handler;
        this.phone = phone;
        this.challenge = challenge;
    }

    @Override
    public void run() {
        Map<String, Object> map = new HashMap<>(8);

        map.put("phone", phone);
        map.put("challenge", challenge);
        String verifyParams = GHttpUtils.postHttpOfMap("http://www.geetest.com/demo/gt/verify", map);
        try {
            JSONObject jsonObject = new JSONObject(verifyParams);
            if ("success".equals(jsonObject.getString("status"))) {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            } else {
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
