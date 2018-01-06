package com.example.administrator.a001;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.os.Handler;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/10/26.
 */

public class SplashActivity extends Activity {
    /*Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg){

            switch (msg.what){
                case 1:
                    String jsessionid = (String) msg.obj;
                    int result = msg.arg1;

                    if (0 == result){
                        //跳转至LoginActivity
                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        };
                        timer.schedule(task, 1000*3);
                    }else if (1 == result){
                        // 记录JSESSIONID
                        SharedPreferences pref = getSharedPreferences("user",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Cookie", jsessionid);
                        editor.commit();

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask(){
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        };
                        timer.schedule(task, 1000*3);
                    }

                    break;
                case 2:
                    Toast.makeText(SplashActivity.this, "服务器错误，请重试",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            }
        };
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏模式
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 去除标题行
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(task,1000*3);
        /*// 自动登录
        SharedPreferences pref = getSharedPreferences("user", 0);
        String username = pref.getString("username", "");
        String password = pref.getString("password", "");
        
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            timer.schedule(task,1000*3);
        } else {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            timer.schedule(task,1000*3);
        }
        finish();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }
}
