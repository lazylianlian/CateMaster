package com.catemaster.catemaster.activitys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.catemaster.catemaster.R;
import com.catemaster.catemaster.bean.MainRecipe;

import java.util.ArrayList;
import java.util.List;

public class CateListActivity extends Activity {
	private List<MainRecipe> list;
	ListView cateListView;
	TextView titleTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catelist);
		titleTv = (TextView) findViewById(R.id.tv_title);
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		titleTv.setText(title);
		
		cateListView = (ListView) findViewById(R.id.cateList_lv);
		
	}
	private void loadList(){
		list = new ArrayList<MainRecipe>();
		list.add(new MainRecipe(R.mipmap.cate_default, "��Ȼ����", "20����"));
		list.add(new MainRecipe(R.mipmap.cate_default, "����ţ��", "30����"));
		list.add(new MainRecipe(R.mipmap.cate_default, "��Ы��", "40����"));
		list.add(new MainRecipe(R.mipmap.cate_default, "��֭Ƥ��", "10����"));
		list.add(new MainRecipe(R.mipmap.cate_default, "��Ȼ����", "20����"));
		list.add(new MainRecipe(R.mipmap.cate_default, "����ţ��", "30����"));
		list.add(new MainRecipe(R.mipmap.cate_default, "��Ы��", "40����"));
		list.add(new MainRecipe(R.mipmap.cate_default, "��֭Ƥ��", "10����"));

	}
	public void backClick(View view) {
		finish();
	}
}
