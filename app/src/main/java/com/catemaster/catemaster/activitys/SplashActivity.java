package com.catemaster.catemaster.activitys;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.catemaster.catemaster.R;

public class SplashActivity extends Activity {
	Timer timer;
	private SharedPreferences sp;
	String string;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//�洢
		sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		string = sp.getString("edit", "first");
		
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if (string.equals("first")) {
					Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
					startActivity(intent);
				}else {
					Intent intent = new Intent(SplashActivity.this,MainActivity.class);
					startActivity(intent);
				}
				finish();
				
			}
		}, 2000);
	}
}
