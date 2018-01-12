package com.example.administrator.a001;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.a001.bean.ForgetResponseBean;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetNewPasswordActivity extends AppCompatActivity {
    private String newpwd = "";
    private String renewpwd = "";
    private String username = "";

    private EditText EdtNewPassword;
    private EditText EdtReNewPassword;
    private Button Btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        EdtNewPassword = (EditText) findViewById(R.id.EdtNewPassword);
        EdtReNewPassword = (EditText) findViewById(R.id.EdtReNewPassword);
        Btn_ok = (Button) findViewById(R.id.btn_ok);

        Btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newpwd = EdtNewPassword.getText().toString();
                renewpwd = EdtReNewPassword.getText().toString();
                username = UserInfo.getUsername();

                if (TextUtils.isEmpty(newpwd)) {
                    EdtNewPassword.setError(getResources().getString(R.string.input_password));
                    EdtNewPassword.requestFocus();
                } else if (TextUtils.isEmpty(renewpwd)) {
                    EdtReNewPassword.setError(getResources().getString(R.string.input_password_again));
                    EdtReNewPassword.requestFocus();
                } else if (!newpwd.equals(renewpwd)) {
                    EdtReNewPassword.setError(getResources().getString(R.string.rePasswordWrong));
                    EdtReNewPassword.requestFocus();
                } else {
                    sendNewPasswordRequest(username, newpwd);
                }
            }
        });
    }

    private void sendNewPasswordRequest(final String username, final String newpwd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", username)
                            .add("password",newpwd)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/user/setpassword")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String data = response.body().string();
                        getResponseStatusCode(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getResponseStatusCode(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ForgetResponseBean forgetResponseBean = null;
                try {
                    JSONObject Obj = JSON.parseObject(data);
                    int status = Obj.getIntValue("statusCode");

                    if (status == 1) {
                        //跳转

                        Intent intent = new Intent(SetNewPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else {
                        EdtNewPassword.setError(getResources().getString(R.string.set_error));
                        EdtNewPassword.requestFocus();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
