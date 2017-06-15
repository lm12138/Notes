package com.edu.notes;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.edu.notes.fragment.HomeFragment;
import com.edu.notes.fragment.InfoFragment;
import com.edu.notes.fragment.MeFragment;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{
    private ArrayList<Fragment> fragments;
    TextView textView;
    TextView tv_dt;
    TextView tv_wather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        获取定位


         textView = (TextView)findViewById(R.id.home_head);
         tv_dt=(TextView)findViewById(R.id.tv_fabiao);
        tv_wather=(TextView)findViewById(R.id.tv_weather);
        tv_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), CreateActivity.class));  // ActivityDemo是需要启动的Activity类
            }
        });
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);


        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);//设置导航栏属性
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);//设置导航栏背景模式 BACKGROUND_STYLE_DEFAULT，BACKGROUND_STYLE_STATIC，BACKGROUND_STYLE_RIPPLE
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home, "首  页").setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.mipmap.dynamic, "动  态").setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.mipmap.my, "我").setActiveColorResource(R.color.grey))
                .initialise();

        fragments = getFragments();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);       //bottomNavigationBar设置监听选项点击事件setTabSelectedListener。
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.layFrame, HomeFragment.newInstance());
        transaction.commit();
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance());
        fragments.add(InfoFragment.newInstance());
        fragments.add(MeFragment.newInstance());
        return fragments;
    }


    @Override
    public void onTabSelected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                     if (0==position){
                         tv_dt.setVisibility(View.GONE);
                         textView.setText("今日天气");
                         tv_wather.setVisibility(View.VISIBLE);
                     }
                    else if (1==position){
                         tv_dt.setVisibility(View.VISIBLE);
                         textView.setText("我的笔记");
                         tv_wather.setVisibility(View.GONE);
                    }
                    else {
                         tv_wather.setVisibility(View.GONE);
                         tv_dt.setVisibility(View.GONE);
                         textView.setText("我");
                     }
                if (fragment.isAdded()) {
                    ft.replace(R.id.layFrame, fragment);
                } else {
                    ft.add(R.id.layFrame, fragment);
                }
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

}
