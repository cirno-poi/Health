package com.example.administrator.a001;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.a001.bean.ForgetResponseBean;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QAActivity extends AppCompatActivity {

    private String questionget = "";
    private String answer2 = "";
    private String username = "";

    private EditText edtQuestion;
    private EditText edtAnswer;
    private Button btnsure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);

        Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据
        String str = bundle.getString("str");//getString()返回指定key的值
        questionget = str;
        Log.d("233333", "questionget:------------------------> "+questionget);

        edtQuestion = (EditText)findViewById(R.id.edtQuestion2);
        edtAnswer = (EditText)findViewById(R.id.edtAnswer2);
        btnsure = (Button)findViewById(R.id.btn_sure) ;
        edtQuestion.setText(questionget);

        btnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer2 = edtAnswer.getText().toString();
                username = UserInfo.getUsername();

                if (TextUtils.isEmpty(answer2)) {
                    edtAnswer.setError(getResources().getString(R.string.input_username));
                    edtAnswer.requestFocus();
                } else {
                    sendSetNewRequest(username,answer2);
                }

            }
        });
    }

    private void sendSetNewRequest(final String username,final String answer2) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", username)
                            .add("answer",answer2)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/user/answermatch")
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
    private void getResponseStatusCode(final String res) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ForgetResponseBean forgetResponseBean = null;
                try {
                    JSONObject Obj = JSON.parseObject(res);
                    int status = Obj.getIntValue("statusCode");

                        if (status == 1) {
                            //跳转

                            Intent intent = new Intent(QAActivity.this, SetNewPasswordActivity.class);
                            startActivity(intent);
                        }else {
                            edtAnswer.setError(getResources().getString(R.string.answer_error));
                            edtAnswer.requestFocus();
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
