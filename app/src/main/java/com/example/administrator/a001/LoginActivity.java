package com.example.administrator.a001;

/**
 * Created by Wei Jinhua on 2017/10/26.
 */

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.a001.bean.LoginResponseBean;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends Activity {


    public static final String TAG = "LoginActivity";
    public static final int INIT = 100;

    private Button btnLogin;
    private TextView tvLostPassword;
    private EditText edtUsername;
    private EditText edtPassword;
    //    CheckBox ckLogin = null;
    private TextView tvRegister;

    private int loginStatusCode = INIT;//登录返回值状态码
    private String statusMsg = "";//状态描述

    private String username = "";
    private String password = "";

//    private String result = "";

//    private LoginResponseBean loginResponseBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        sendLoginRequest("pass", "nothing");//为了通过网络请求

        tvLostPassword = findViewById(R.id.tvLostPassword);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
//        ckLogin = findViewById(R.id.ckLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // 忘记密码链接?
        tvLostPassword
                .setText(Html
                        .fromHtml("<a href=\"http://www.baidu.com\">忘记密码？</a>"));
        tvLostPassword.setMovementMethod(LinkMovementMethod.getInstance());

        // 注册页面跳转
        tvRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.actionStart(LoginActivity.this);
            }
        });

        // 获取组件对象
        btnLogin = (Button) findViewById(R.id.btnLogin);
        // 绑定监听器
        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("MyHealth", "Login Button Click");

                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    edtUsername.setError(getResources().getString(R.string.input_username));
                    edtUsername.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    edtPassword.setError(getResources().getString(R.string.input_password));
                    edtPassword.requestFocus();
                } else {
                    sendLoginRequest(username, password);//发送登录请求

////                    if(loginResponseBean != null)
////                    {
////                        loginStatusCode = loginResponseBean.getStatusCode();
////                    }
////                    loginStatusCode = loginResponseBean.getStatusCode();
//                    Log.d(TAG, "-----------loginStatusCodes: " + loginStatusCode);
//                    if (loginStatusCode == 1) {
//                        Toast.makeText(LoginActivity.this, "登录成功",
//                                Toast.LENGTH_SHORT).show();
//                        MainActivity.actionStart(LoginActivity.this);
//                        finish();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "error",
//                                Toast.LENGTH_SHORT).show();
//                    }
                }

            }
        });
    }

    /**
     * 发送登录请求
     *
     * @param username 用户名
     * @param password 密码
     */
    private void sendLoginRequest(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Log.d("23333", "sendRegisterRequest:------------ .");

                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", username)
                            .add("password", password)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/user/login")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
//                        result = response.body().string();
                        getResponse(response.body().string());
//                        statusMsg = getResponse(response.body().string()).getMsg();
                    }
//                    String responseDate = JSON.toJSONString(response.body());
//                    Log.d("23333", "responseDate:------------ ." + JSON.toJSONString(registerResponseBean));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 解析结果
     *
     * @param res 返回的json
     * @return
     */
    private void getResponse(final String res) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginResponseBean loginResponseBean = null;
                try {
                    JSONObject Obj = JSON.parseObject(res);
                    loginResponseBean = JSON.parseObject(Obj.toJSONString(), LoginResponseBean.class);

                    if (loginResponseBean.getStatusCode() == 1) {
                        Toast.makeText(LoginActivity.this, "登录成功",
                                Toast.LENGTH_SHORT).show();
                        MainActivity.actionStart(LoginActivity.this);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "error",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

}
