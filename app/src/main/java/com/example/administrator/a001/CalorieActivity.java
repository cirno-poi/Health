package com.example.administrator.a001;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CalorieActivity extends AppCompatActivity {

    private EditText mifanEdt;
    private EditText yumiEdt;
    private EditText mantouEdt;
    private EditText yanmaiEdt;
    private EditText mianbaoEdt;
    private EditText hongshuEdt;
    private EditText xiaomizhouEdt;
    private EditText malingshuEdt;
    private EditText baoziEdt;
    private EditText miantiaoEdt;
    private EditText jidanEdt;
    private EditText jirouEdt;
    private EditText zhurouEdt;
    private EditText yurouEdt;
    private EditText niurouEdt;
    private EditText yangrouEdt;

    private TextView totalCalorieTv;
    private Button sumBtn;

    private String mifan = "";
    private String yumi = "";
    private String mantou = "";
    private String yanmai = "";
    private String mianbao = "";
    private String hongshu = "";
    private String xiaomizhou = "";
    private String malingshu = "";
    private String baozi = "";
    private String miantiao = "";
    private String jidan = "";
    private String jirou = "";
    private String zhurou = "";
    private String yurou = "";
    private String niurou = "";
    private String yangrou = "";


    /**
     * 页面跳转方法
     *
     * @param context 源页面context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CalorieActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);

        Toolbar toolbar = findViewById(R.id.calorie_toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        totalCalorieTv = findViewById(R.id.sum_tv);
        sumBtn = findViewById(R.id.sum_btn);

        mifanEdt = findViewById(R.id.rice_tv);
        yumiEdt = findViewById(R.id.yumi_tv);
        mantouEdt = findViewById(R.id.mantou_tv);
        yanmaiEdt = findViewById(R.id.yanmai_tv);
        mianbaoEdt = findViewById(R.id.mianbao_tv);
        hongshuEdt = findViewById(R.id.hongshu_tv);
        xiaomizhouEdt = findViewById(R.id.xiaomizhou_tv);
        malingshuEdt = findViewById(R.id.malingshu_tv);
        baoziEdt = findViewById(R.id.baozi_tv);
        miantiaoEdt = findViewById(R.id.miantiao_tv);
        jidanEdt = findViewById(R.id.jidan_tv);
        jirouEdt = findViewById(R.id.jirou_tv);
        zhurouEdt = findViewById(R.id.zhurou_tv);
        yurouEdt = findViewById(R.id.yurou_tv);
        niurouEdt = findViewById(R.id.niurou_tv);
        yangrouEdt = findViewById(R.id.yangrou_tv);



        sumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mifan = mifanEdt.getText().toString();
                yumi = yumiEdt.getText().toString();
                mantou = mantouEdt.getText().toString();
                yanmai = yanmaiEdt.getText().toString();
                mianbao = mianbaoEdt.getText().toString();
                hongshu = hongshuEdt.getText().toString();
                xiaomizhou = xiaomizhouEdt.getText().toString();
                malingshu = malingshuEdt.getText().toString();
                baozi = baoziEdt.getText().toString();
                miantiao = miantiaoEdt.getText().toString();
                jidan = jidanEdt.getText().toString();
                jirou = jirouEdt.getText().toString();
                zhurou = zhurouEdt.getText().toString();
                yurou = yurouEdt.getText().toString();
                niurou = niurouEdt.getText().toString();
                yangrou = yangrouEdt.getText().toString();
                sendCalorieRequest();
            }
        });

    }


    /**
     * 设置健康信息请求
     */
    private void sendCalorieRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Log.d("23333", "sendRegisterRequest:------------ .");

                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("米饭", mifan)
                            .add("玉米", yumi)
                            .add("馒头", mantou)
                            .add("燕麦片", yanmai)
                            .add("面包", mianbao)
                            .add("红薯", hongshu)
                            .add("小米粥", xiaomizhou)
                            .add("马铃薯", malingshu)
                            .add("包子", baozi)
                            .add("面条", miantiao)
                            .add("鸡蛋", jidan)
                            .add("鸡肉", jirou)
                            .add("猪肉", zhurou)
                            .add("鱼肉", yurou)
                            .add("牛肉", niurou)
                            .add("羊肉", yangrou)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/behavior/sumCalorie")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String data = response.body().string();
                        Log.d("2333", "----------------response: " + data);
                        getSumResponse(data);
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
     * 解析修改后的结果
     *
     * @param res 返回值json
     */
    private void getSumResponse(final String res) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("2333", "-----------------getResponse: " + res);
                try {
                    JSONObject Obj = JSON.parseObject(res);
                    int status = Obj.getIntValue("statusCode");
//                    Log.d(TAG, "------------------status: " + status);
                    if (status == 1) {
                        String sum = Obj.getString("totalCalorie");
                        totalCalorieTv.setText(sum+"卡路里");
//                        Log.d(TAG, "---------healthInfoResponseBean: " + basicInfo.toJSONString());
                    } else {
                        Toast.makeText(CalorieActivity.this, "计算失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
