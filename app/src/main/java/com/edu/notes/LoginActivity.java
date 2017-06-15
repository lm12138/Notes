package com.edu.notes;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.Toast;

import com.edu.notes.R;
import com.edu.notes.adapter.TabViewPagerAdapter;
import com.edu.notes.fragment.FragmentLogin;
import com.edu.notes.fragment.FragmentRegist;

import cn.bmob.v3.BmobUser;

public class LoginActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//再次打开时获取本地已经登录用户的信息
		BmobUser bmobUser = BmobUser.getCurrentUser(this);
		if (bmobUser != null){
			Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			finish();
			startActivity(intent);
		}
		//透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		setupViewPager();
	}

	//设置tab下的viewpager
	private void setupViewPager() {

		final ViewPager login_viewpager = (ViewPager) findViewById(R.id.login_viewpager);
		setupViewPager(login_viewpager);
		TabLayout login_tabs = (TabLayout) findViewById(R.id.login_tabs);
		login_tabs.setupWithViewPager(login_viewpager);
		login_tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				final 	int f=tab.getPosition();
				login_viewpager.setCurrentItem(f);

			}
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}
			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});

	}
	private void setupViewPager(ViewPager viewPager) {
		TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager());
		adapter.addFrag(new FragmentLogin(), "登录");
		adapter.addFrag(new FragmentRegist(), "注册");
		viewPager.setAdapter(adapter);
	}
}
