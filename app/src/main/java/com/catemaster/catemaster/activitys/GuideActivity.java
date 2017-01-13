package com.catemaster.catemaster.activitys;

import java.util.ArrayList;
import java.util.List;
import com.catemaster.catemaster.fragments.Fragment1;
import com.catemaster.catemaster.fragments.Fragment2;
import com.catemaster.catemaster.fragments.Fragment3;
import com.catemaster.catemaster.fragments.Fragment4;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.catemaster.catemaster.R;
import com.catemaster.catemaster.adapter.MyPagerAdapter;

public class GuideActivity extends FragmentActivity implements OnClickListener {
	ImageView i1, i2, i3, i4;

	ViewPager vp;
	List<Fragment> fragments;
	Fragment1 Fragment1;
	Fragment2 Fragment2;
	Fragment3 Fragment3;
	Fragment4 Fragment4;
	MyPagerAdapter adapter;
	List<ImageView> imageViews;
	FragmentManager manager;
	private SharedPreferences sp;
	Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		manager = getSupportFragmentManager();
		// �洢
		sp = getSharedPreferences("user", Context.MODE_PRIVATE);

		// ��ȡEditor����
		Editor editor = sp.edit();
		editor.putString("edit", "1");
		editor.commit();

		initViewPager();
		resetColor(0);
	}

	private void initViewPager() {
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GuideActivity.this,LoginAndRegistActivity.class);
				startActivity(intent);
				finish();
			}
		});
		imageViews = new ArrayList<ImageView>();
		i1 = (ImageView) findViewById(R.id.imageView1);
		i2 = (ImageView) findViewById(R.id.imageView2);
		i3 = (ImageView) findViewById(R.id.imageView3);
		i4 = (ImageView) findViewById(R.id.imageView4);
		i1.setOnClickListener(this);
		i2.setOnClickListener(this);
		i3.setOnClickListener(this);
		i4.setOnClickListener(this);
		imageViews.add(i1);
		imageViews.add(i2);
		imageViews.add(i3);
		imageViews.add(i4);

		vp = (ViewPager) findViewById(R.id.myPager);
		fragments = new ArrayList<Fragment>();
		Fragment1 = new Fragment1();
		Fragment2 = new Fragment2();
		Fragment3 = new Fragment3();
		Fragment4 = new Fragment4();

		fragments.add(Fragment1);
		fragments.add(Fragment2);
		fragments.add(Fragment3);
		fragments.add(Fragment4);
		adapter = new MyPagerAdapter(manager, fragments);
		vp.setAdapter(adapter);
		vp.setCurrentItem(0);
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

				resetColor(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void resetColor(int index) {
		if (index == imageViews.size() - 1) {
			btn.setVisibility(View.VISIBLE);
		} else {
			btn.setVisibility(View.GONE);
		}
		for (int i = 0; i < imageViews.size(); i++) {
			if (index == i) {
				imageViews.get(i).setBackgroundResource(R.drawable.a4);
			} else {
				imageViews.get(i).setBackgroundResource(R.drawable.a5);
			}
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imageView1:
			vp.setCurrentItem(0);
			resetColor(0);
			break;
		case R.id.imageView2:
			vp.setCurrentItem(1);
			resetColor(1);
			break;
		case R.id.imageView3:
			vp.setCurrentItem(2);
			resetColor(2);
			break;
		case R.id.imageView4:
			vp.setCurrentItem(3);
			resetColor(3);
			break;

		default:
			break;
		}
	}

}
