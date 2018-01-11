package com.example.administrator.a001;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
//import android.app.ActionBar.Tab;
//import android.app.ActionBar.TabListener;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    Long startTime = 0L;

    private HealthFragment mHealthFragment;
    private SearchFragment mSearchFragment;
    private MyFragment myFragment;
    private TextView mTitle;

    /**
     * 页面跳转方法
     *
     * @param context 源页面context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
//        toolbar.setTitle("2333");

        mTitle = findViewById(R.id.title);

        BottomNavigationBar mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        mBottomNavigationBar.setBarBackgroundColor(R.color.white_grey);//set background color for navigation bar，设置底部导航栏颜色

        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_accessibility_black_48dp, R.string.my_health).setInActiveColorResource(R.color.font_grey))//icon需要修改
                .addItem(new BottomNavigationItem(R.drawable.ic_search_black_36dp, R.string.lucky).setInActiveColorResource(R.color.font_grey))//icon需要变化
                .addItem(new BottomNavigationItem(R.drawable.ic_person_black_36dp, R.string.my_account).setInActiveColorResource(R.color.font_grey))//依次添加item,分别icon和名称
                .setFirstSelectedPosition(0)//设置默认选择item
                .initialise();//初始化
        mBottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();

    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mHealthFragment = HealthFragment.newInstance("健康");
        mTitle.setText("健康");
        transaction.replace(R.id.tb, mHealthFragment);
        transaction.commit();
    }


    //退出应用
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - startTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                startTime = System.currentTimeMillis();
            } else {
                sendLogoutRequest(UserInfo.getUsername(), UserInfo.getToken());
                finish();
            }
        }
        return true;
    }

    /**
     * 发送登出请求
     *
     * @param username 用户名
     * @param password 密码
     */
    private void sendLogoutRequest(final String username, final String password) {
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
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/user/logou")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();

//                    String responseDate = JSON.toJSONString(response.body());
//                    Log.d("23333", "responseDate:------------ ." + JSON.toJSONString(registerResponseBean));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = this.getFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                mTitle.setText("健康");
                if (mHealthFragment == null) {
                    mHealthFragment = HealthFragment.newInstance("健康");
                }
                transaction.replace(R.id.tb, mHealthFragment);
                break;
            case 1:
                mTitle.setText("发现");
                if (mSearchFragment == null) {
                    mSearchFragment = SearchFragment.newInstance("发现");
                }
                transaction.replace(R.id.tb, mSearchFragment);
                break;
            case 2:
                mTitle.setText("我的");
                if (myFragment == null) {
                    myFragment = MyFragment.newInstance("我的");
                }
                transaction.replace(R.id.tb, myFragment);
                break;

            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
