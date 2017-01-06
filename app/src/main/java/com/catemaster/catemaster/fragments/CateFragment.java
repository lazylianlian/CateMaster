package com.catemaster.catemaster.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.catemaster.catemaster.R;
import com.catemaster.catemaster.activitys.CateListActivity;
import com.catemaster.catemaster.adapter.CateGridAdapter;

public class CateFragment extends Fragment{
	private GridView gridView;
	private int[] pics = {R.mipmap.grid_jiachangcai, R.mipmap.grid_kuaishou, R.mipmap.grid_chuangyi, R.mipmap.grid_sucai, R.mipmap.grid_liangcai, R.mipmap.grid_hongbei, R.mipmap.grid_mianshi, R.mipmap.grid_tang};
	private String[] tags = {"家常菜","快手菜","创意菜","凉菜","素菜","烘焙","面食","汤"};
	private CateGridAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_cate, null);
		
		gridView = (GridView) view.findViewById(R.id.cateGridView);
		adapter = new CateGridAdapter(getActivity(), pics, tags);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selected = tags[position];
				Toast.makeText(getActivity(), selected, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getActivity(), CateListActivity.class);
				intent.putExtra("title", selected);
				intent.putExtra("cid",position+1);
				startActivity(intent);
			}

		});
		return view;
	}
}
