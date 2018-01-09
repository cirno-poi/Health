package com.example.administrator.a001;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.a001.bean.RegisterResponseBean;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 注册页面
 */
public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "23333";

    private Button btnRegister;//注册按钮
    private String username = "";//用户名
    private String password = "";//密码
    private String rePassword = "";//确认密码

    private EditText usernameEdt;
    private EditText passwordEdt;
    private EditText passwordAgainEdt;

//    private int registerStatus = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        usernameEdt = (EditText) findViewById(R.id.registerEdtUsername);
        passwordEdt = (EditText) findViewById(R.id.registerEdtPassword);
        passwordAgainEdt = (EditText) findViewById(R.id.edtPasswordAgain);


        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                username = usernameEdt.getText().toString();
                password = passwordEdt.getText().toString();
                rePassword = passwordAgainEdt.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    usernameEdt.setError(getResources().getString(R.string.input_username));
                    usernameEdt.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    passwordEdt.setError(getResources().getString(R.string.input_password));
                    passwordEdt.requestFocus();
                } else if (TextUtils.isEmpty(rePassword)) {
                    passwordAgainEdt.setError(getResources().getString(R.string.input_password_again));
                    passwordAgainEdt.requestFocus();
                } else if (!password.equals(rePassword)) {
                    passwordAgainEdt.setError(getResources().getString(R.string.rePasswordWrong));
                    passwordAgainEdt.requestFocus();
                } else {
                    sendRegisterRequest(username, password);
//                    Log.d(TAG, "-----------registerStatus: " + registerStatus);
                }

            }
        });
    }

    /**
     * 发送注册请求
     *
     * @param username 用户名
     * @param password 密码
     */
    private void sendRegisterRequest(final String username, final String password) {
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
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/user/register")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        getResponseStatusCode(response.body().string());
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
     * @param res 返回的json串
     * @return
     */
    private void getResponseStatusCode(final String res) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RegisterResponseBean registerResponseBean = null;
                try {
                    JSONObject Obj = JSON.parseObject(res);
                    registerResponseBean = JSON.parseObject(Obj.toJSONString(), RegisterResponseBean.class);

                    if (registerResponseBean != null) {
                        switch (registerResponseBean.getStatusCode()) {
                            //失败
                            case -1:
                                Toast.makeText(RegisterActivity.this, "该用户名已被使用",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 0:
                                Toast.makeText(RegisterActivity.this, "未知错误",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            //成功
                            case 1:
                                Toast.makeText(RegisterActivity.this, "注册成功，请登录",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            default:
                                Toast.makeText(RegisterActivity.this, "未知错误",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    /**
     * 页面跳转方法
     *
     * @param context 源页面context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
}
