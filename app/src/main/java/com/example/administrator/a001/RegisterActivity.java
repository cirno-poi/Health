package com.example.administrator.a001;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 注册页面
 */
public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;//注册按钮
    private String username = "";//用户名
    private String password = "";//密码
    private String rePassword = "";//确认密码

    private EditText usernameEdt;
    private EditText passwordEdt;
    private EditText passwordAgainEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        usernameEdt = findViewById(R.id.registerEdtUsername);
        passwordEdt = findViewById(R.id.registerEdtPassword);
        passwordAgainEdt = findViewById(R.id.edtPasswordAgain);


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
                }
//                Log.d("23333", "onClick: on----------------");
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
                    String responseDate = response.body().string();
//                    String responseDate = JSON.toJSONString(response.body());
                    Log.d("23333", "responseDate:------------ ." + responseDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

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
