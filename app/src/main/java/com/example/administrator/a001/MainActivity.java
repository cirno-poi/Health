package com.example.administrator.a001;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
//import android.app.ActionBar.Tab;
//import android.app.ActionBar.TabListener;
import android.view.KeyEvent;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

public class MainActivity extends FragmentActivity implements BottomNavigationBar.OnTabSelectedListener {

    ViewPager pager = null;
    Long startTime = 0L;

    private HealthFragment mHealthFragment;
    private SearchFragment mSearchFragment;
    private MyFragment myFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationBar  mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

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
        transaction.replace(R.id.tb, mHealthFragment);
        transaction.commit();
    }




    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis() - startTime >2000){
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                startTime = System.currentTimeMillis();
            }else{
                finish();
            }
        }
        return true;
    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = this.getFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mHealthFragment == null) {
                    mHealthFragment = HealthFragment.newInstance("健康");
                }
                transaction.replace(R.id.tb, mHealthFragment);
                break;
            case 1:
                if (mSearchFragment == null) {
                    mSearchFragment = SearchFragment.newInstance("发现");
                }
                transaction.replace(R.id.tb, mSearchFragment);
                break;
            case 2:
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
