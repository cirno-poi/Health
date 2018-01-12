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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.a001.bean.ForgetResponseBean;
import com.example.administrator.a001.bean.RegisterResponseBean;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgetActivity extends AppCompatActivity {

    private String username = "";//用户名
    private String question = "";

    private EditText usernameEdt;
    private Button btnfind;//确认找回按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        btnfind = (Button) findViewById(R.id.btnForget);
        usernameEdt = (EditText) findViewById(R.id.forgetusername_edit);

        btnfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEdt.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    usernameEdt.setError(getResources().getString(R.string.input_username));
                    usernameEdt.requestFocus();
                } else {
                    UserInfo.setUsername(username);
                    sendForgetRequest(username);
                    Log.d("6666666666", "questionfinal:------------------------> "+question);

//                    Intent intent = new Intent(ForgetActivity.this, QAActivity.class);
//                    intent.putExtra("str", question);
//                    startActivity(intent);
                }

            }
        });
    }

    private void sendForgetRequest(final String username) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", username)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/user/findQuestion")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String data = response.body().string();
                        Log.d("233333", "data:------------------------> "+data);
                        getResponseStatusCode(data);
                    }
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
                ForgetResponseBean forgetResponseBean = null;
                try {
                    JSONObject Obj = JSON.parseObject(res);
                    forgetResponseBean = JSON.parseObject(Obj.toJSONString(), ForgetResponseBean.class);

                    if (forgetResponseBean != null) {
                        if (forgetResponseBean.getStatusCode() == 1) {
                            //跳转
                            question = forgetResponseBean.getQuestion();

                            Intent intent = new Intent(ForgetActivity.this, QAActivity.class);
                            intent.putExtra("str", question);
                            startActivity(intent);
                            Log.d("455555555555", "question:------------------------> "+question);
                        }else if (forgetResponseBean.getStatusCode() == -1){
                            usernameEdt.setError(getResources().getString(R.string.usernameout));
                            usernameEdt.requestFocus();
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
        Intent intent = new Intent(context, ForgetActivity.class);
        context.startActivity(intent);
    }
}
