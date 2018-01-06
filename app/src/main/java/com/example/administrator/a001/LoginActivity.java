package com.example.administrator.a001;

/**
 * Created by Administrator on 2017/10/26.
 */
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity{

    Button btnLogin;
    TextView tvLostPassword = null;
    EditText edtUsername = null;
    EditText edtPassword = null;
    CheckBox ckLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvLostPassword = findViewById(R.id.tvLostPassword);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        ckLogin = findViewById(R.id.ckLogin);

        // 忘记密码链接?
        tvLostPassword
                .setText(Html
                        .fromHtml("<a href=\"http://www.baidu.com\">忘记密码？</a>"));
        tvLostPassword.setMovementMethod(LinkMovementMethod.getInstance());

        // 获取组件对象
        btnLogin = (Button) findViewById(R.id.btnLogin);
        // 绑定监听器
        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("MyHealth", "Login Button Click");

                if (TextUtils.isEmpty(edtUsername.getText().toString())) {
                    edtUsername.setError("请输入用户名");
                    edtUsername.requestFocus();
                } else if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    edtPassword.setError("请输入密码");
                    edtPassword.requestFocus();
                } else {
                    // 记录用户名或密码
                    if (ckLogin.isChecked()) {
                        // 0: mode_private
                        SharedPreferences pref = getSharedPreferences("user",
                                Context.MODE_PRIVATE);
                        Editor editor = pref.edit(); // 编辑器
                        editor.putString("username", edtUsername.getText()
                                .toString());
                        editor.putString("password", edtPassword.getText()
                                .toString());
                        editor.commit(); // 一定要提交

                    } else {
                        // 清空以前的登录信息
                        SharedPreferences pref = getSharedPreferences("user",
                                Context.MODE_PRIVATE);
                        Editor editor = pref.edit();
                        editor.remove("username");
                        editor.remove("password");
                        editor.commit();
                    }

                    // 显式意图
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    // 隐式意图
                    // Intent intent = new Intent();
                    // intent.setAction("aa");
                    // intent.addCategory("bb");
                    // startActivity(intent);

                    // 关闭LoginActivity
                    finish();
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
