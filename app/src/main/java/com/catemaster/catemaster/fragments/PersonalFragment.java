package com.catemaster.catemaster.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.catemaster.catemaster.R;
import com.catemaster.catemaster.activitys.CateDetailActivity;
import com.catemaster.catemaster.activitys.CateListActivity;
import com.catemaster.catemaster.activitys.PersonalSetActivity;
import com.catemaster.catemaster.bean.CateCollectionInfo;
import com.catemaster.catemaster.bean.CateInfo;
import com.catemaster.catemaster.bean.UserInfo;
import com.catemaster.catemaster.dao.DBManager;
import com.catemaster.catemaster.utils.CommonAdapter;
import com.catemaster.catemaster.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PersonalFragment extends Fragment {
	ListView listView;
	List<CateCollectionInfo> list = new ArrayList<>();
	CommonAdapter<CateCollectionInfo> adapter;
	DBManager manager;
	Button collectBtn;
	ImageView settingBtn;
	TextView person_name,person_word;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_personal, null);
		listView = (ListView) view.findViewById(R.id.personListView);
		UserInfo userInfo = UserInfo.getCurrentUser();

		manager = DBManager.getInstance(getActivity());
		settingBtn = (ImageView) view.findViewById(R.id.personal_setting);
		settingBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(),PersonalSetActivity.class);
				startActivity(intent);
			}
		});
		initData();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
				collectBtn = (Button) view.findViewById(R.id.collectBtn);
				collectBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						boolean result = manager.addCateCollecInfo(list.get(i));
						if (!result){
							collectBtn.setBackgroundResource(R.mipmap.cate_list_like_normal);
							initData();
							Toast.makeText(getActivity(), "取消收藏", Toast.LENGTH_SHORT).show();
						}
						Toast.makeText(getActivity(), list.get(i).getId(), Toast.LENGTH_SHORT).show();
					}
				});

//				Intent intent = new Intent(CateListActivity.this,CateDetailActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("cateDetailInfo",cateDetailInfos.get(i));
//				intent.putExtra("bundle",bundle);
//				startActivity(intent);

			}
		});
		return view;
	}


	private void initData() {
		list = manager.findCateCollecInfo();
		adapter = new CommonAdapter<CateCollectionInfo>(getActivity(),list,R.layout.activity_cate_list_item) {
			@Override
			public void convert(ViewHolder helper, CateCollectionInfo item) {
				helper.setText(R.id.cateNameTv, item.getTitle());
				helper.setImageByUrl(R.id.cate_img, item.getAlbums());
				helper.setBtnImageResource(R.id.collectBtn,R.mipmap.cate_list_like_click);
			}
		};
		listView.setAdapter(adapter);
	}

}
