package com.example.ted.geetestdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;


import com.geetest.deepknow.DPAPI;
import com.geetest.deepknow.bean.DPJudgementBean;
import com.geetest.sensebot.SEAPI;
import com.geetest.sensebot.listener.BaseSEListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SEAPI seapi;
    public static final String ID = "48a6ebac4ebc6642d68c217fca33eb4d";

    private Button start;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.editText = (EditText) findViewById(R.id.editText);
        this.start = (Button) findViewById(R.id.start);
        seapi = new SEAPI(this);
        test();
    }

    private void test() {
        DPAPI.getInstance(this).ignoreDPView(start, "MainActivity");
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPhone(editText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "请输入正确手机号", Toast.LENGTH_LONG).show();
                    return;
                }
                seapi.onVerify(new DPJudgementBean(ID, 1, new HashMap<String, Object>()), new BaseSEListener() {
                    /**
                     * SDK内部show loading dialog
                     */
                    @Override
                    public void onShowDialog() {
                        Log.i(TAG, "onShowDialog-->SDK show loading dialog！");
                    }

                    @Override
                    public void onError(String errorCode, String error) {
                        Log.i(TAG, "onError-->errorCode:" + errorCode + ", error: " + error);
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * 验证码Dialog关闭
                     * 1：webview的叉按钮关闭
                     * 2：点击屏幕外关闭
                     * 3：点击回退键关闭
                     *
                     * @param num
                     */
                    @Override
                    public void onCloseDialog(int num) {
                        Log.i(TAG, "onCloseDialog-->" + num);
                    }

                    /**
                     * show 验证码webview
                     */
                    @Override
                    public void onDialogReady() {
                        Log.i(TAG, "onDialogReady-->SDK show captcha webview dialog! ");
                    }

                    /**
                     * 验证成功
                     * @param challenge
                     */
                    @Override
                    public void onResult(String challenge) {
                        Log.i(TAG, "onResult: " + challenge);
                        final Map map = new HashMap(8);
                   
                        map.put("phone", editText.getText().toString());
                        map.put("challenge", challenge);
         
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                     String verifyParams = GHttpUtils.postHttpOfMap("http://www.geetest.com/demo/gt/verify", map);
                                try {
                                    JSONObject jsonObject = new JSONObject(verifyParams);
                                    if ("success".equals(jsonObject.getString("status"))) {
                                        Message message = new Message();
                                        message.what = 1;
                                        handler.sendMessage(message);
                                    }else {
                                        Message message = new Message();
                                        message.what = 2;
                                        handler.sendMessage(message);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
            }
        });
    }



    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        seapi.destroy();
    }

    private boolean checkPhone(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)){
            return false;
        }
        String regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(14[5-9])|(19[8,9])|)\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

}
