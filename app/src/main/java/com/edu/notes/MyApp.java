package com.edu.notes;

import android.app.Application;
import android.util.Log;


import org.xutils.x;

import cn.bmob.v3.Bmob;

public class MyApp extends Application {
	public static String CITY;
	public static String APPID = "cd80464c73b908b305dad05fe7257d33";
	@Override
	public void onCreate() {
		super.onCreate();
	initXutlis();
		initBmob();
	}

	private void initBmob() {
		Bmob.initialize(this, APPID);
	}

	private void initXutlis() {
			x.Ext.init(this);
		}

}
