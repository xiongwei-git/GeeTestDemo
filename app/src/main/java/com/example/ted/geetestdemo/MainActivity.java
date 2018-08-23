package com.example.ted.geetestdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geetest.deepknow.DPAPI;
import com.geetest.deepknow.bean.DPJudgementBean;
import com.geetest.deepknow.listener.DPJudgementListener;
import com.geetest.sensebot.SEAPI;
import com.geetest.sensebot.listener.BaseSEListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName() + "+++++++++++";
    private SEAPI seapi;
    //public static final String ID = "48a6ebac4ebc6642d68c217fca33eb4d";
    public static final String LOGIN_ID = "5472b5e80367ff83e757d7ac8e9930fa";
    public static final String CODE_ID = "a11f378950feb249a8e04ac86f4fc4be";

    private Button loginBtn;
    private Button loginNoSenseBtn;
    private Button verifyCodeBtn;
    private Button verifyCodeNoSenseBtn;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.editText = findViewById(R.id.editText);
        this.loginBtn = findViewById(R.id.login_btn);
        this.verifyCodeBtn = findViewById(R.id.verify_code_btn);
        this.loginNoSenseBtn = findViewById(R.id.login_no_sense_btn);
        this.verifyCodeNoSenseBtn = findViewById(R.id.verify_code_no_sense_btn);
        seapi = new SEAPI(this);
        bindGeeView();
    }

    public void onClickLogin(View view) {
        if (!checkPhone(editText.getText().toString())) {
            Toast.makeText(MainActivity.this, "请输入正确手机号", Toast.LENGTH_LONG).show();
            return;
        }
        seapi.onVerify(getLoginBean(), baseSEListener);
    }

    public void onClickVerifyCode(View view) {
        if (!checkPhone(editText.getText().toString())) {
            Toast.makeText(MainActivity.this, "请输入正确手机号", Toast.LENGTH_LONG).show();
            return;
        }
        seapi.onVerify(getVerifyCodeBean(), baseSEListener);
    }

    public void onClickLoginNoSense(View view) {
        if (!checkPhone(editText.getText().toString())) {
            Toast.makeText(MainActivity.this, "请输入正确手机号", Toast.LENGTH_LONG).show();
            return;
        }
        DPAPI.getInstance(this).emitSenseData(getLoginBean(), judgementListener);
    }

    public void onClickVerifyCodeNoSense(View view) {
        if (!checkPhone(editText.getText().toString())) {
            Toast.makeText(MainActivity.this, "请输入正确手机号", Toast.LENGTH_LONG).show();
            return;
        }
        DPAPI.getInstance(this).emitSenseData(getVerifyCodeBean(), judgementListener);
    }

    private DPJudgementBean getLoginBean() {
        Map<String, Object> attr = new HashMap<>();
        attr.put("idType", 1);
        attr.put("idValue", editText.getText().toString());
        return new DPJudgementBean(LOGIN_ID, 2, attr);
    }

    private DPJudgementBean getVerifyCodeBean() {
        Map<String, Object> attr = new HashMap<>();
        attr.put("idType", 1);
        attr.put("idValue", editText.getText().toString());
        return new DPJudgementBean(CODE_ID, 3, attr);
    }

    private BaseSEListener baseSEListener = new BaseSEListener() {
        @Override
        public void onShowDialog() {
            Log.i(TAG, "onShowDialog-->SDK show loading dialog！");
            showLog("onShowDialog-->SDK show loading dialog！");
        }

        @Override
        public void onError(String errorCode, String error) {
            Log.i(TAG, "onError-->errorCode:" + errorCode + ", error: " + error);
            showLog("onError-->errorCode:" + errorCode + ", error: " + error);
        }

        /**
         * 验证码Dialog关闭
         * 1：webview的叉按钮关闭
         * 2：点击屏幕外关闭
         * 3：点击回退键关闭
         *
         * @param num int
         */
        @Override
        public void onCloseDialog(int num) {
            Log.i(TAG, "onCloseDialog-->" + num);
            showLog("onCloseDialog-->" + num);
        }

        /**
         * show 验证码webview
         */
        @Override
        public void onDialogReady() {
            Log.i(TAG, "onDialogReady-->SDK show captcha webview dialog! ");
            showLog("onDialogReady-->SDK show captcha webview dialog! ");
        }

        /**
         * 验证成功
         * @param challenge
         */
        @Override
        public void onResult(String challenge) {
            Log.i(TAG, "onResult: " + challenge);
//            Toast.makeText(MainActivity.this, "获取到的challenge是：" + challenge, Toast.LENGTH_SHORT).show();
//                        CheckRunnable runnable = new CheckRunnable(handler, editText.getText().toString(), challenge);
//                        new Thread(runnable).start();
            showLog("获取到的challenge是：" + challenge);
        }
    };

    private DPJudgementListener judgementListener = new DPJudgementListener() {
        @Override
        public void onError(String s, String s1) {
            //Toast.makeText(getApplicationContext(), "无感模式发生错误：" + s + "----" + s1, Toast.LENGTH_LONG).show();
            showLog("无感模式发生错误：" + s + "----" + s1);
        }

        @Override
        public void onDeepKnowResult(JSONObject jsonObject) {
            //Toast.makeText(getApplicationContext(), "无感模式结果：" + jsonObject.toString(), Toast.LENGTH_LONG).show();
            showLog("无感模式结果：" + jsonObject.toString());
        }
    };

//    private void bindCheck() {
//        DPAPI.getInstance(getApplicationContext()).ignoreDPView(checkBtn, "MainActivity");
//        checkBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DPJudgementBean judgementBean = new DPJudgementBean(ID, 1, new HashMap<String, Object>());
//                DPAPI.getInstance(getApplicationContext()).emitSenseData(judgementBean, judgementListener);
//            }
//        });
//
//    }

//    private DPJudgementListener judgementListener = new DPJudgementListener() {
//
//        @Override
//        public void onError(String errorCode, String error) {
//            //整个流程中的错误回调,errorCode为错误码,error为具体的错误原因
//            Log.d(TAG, errorCode + "--------" + error);
//        }
//
//        @Override
//        public void onDeepKnowResult(JSONObject result) {
//            //deepknow的回调
//            Log.d(TAG, "onDeepKnowResult" + "--------" + result.toString());
//        }
//    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        seapi.destroy();
    }

    private void bindGeeView() {
        DPAPI.getInstance(this).ignoreDPView(loginBtn, "MainActivity");
        DPAPI.getInstance(this).ignoreDPView(verifyCodeBtn, "MainActivity");
        DPAPI.getInstance(this).ignoreDPView(loginNoSenseBtn, "MainActivity");
        DPAPI.getInstance(this).ignoreDPView(verifyCodeNoSenseBtn, "MainActivity");
    }

    private boolean checkPhone(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        }
        String regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(14[5-9])|(19[8,9])|)\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    private void showLog(String log) {
        TextView logTxt = findViewById(R.id.log_out_txt);
        logTxt.append("\n");
        logTxt.append(log);
        logTxt.append("\n");
    }

//    private final Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message message) {
//            switch (message.what) {
//                case 1:
//                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2:
//                    Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//            return false;
//        }
//    });
}
