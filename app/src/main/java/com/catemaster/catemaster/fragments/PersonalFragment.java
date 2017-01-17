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
import com.catemaster.catemaster.bean.UserInfo;
import com.catemaster.catemaster.dao.DBManager;
import com.catemaster.catemaster.utils.CommonAdapter;
import com.catemaster.catemaster.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class PersonalFragment extends Fragment {
	ListView listView;
	List<CateCollectionInfo> cList = new ArrayList<>();
	CommonAdapter<CateCollectionInfo> adapter;
	DBManager manager;
	Button collectBtn;
	ImageView settingBtn;
	TextView person_name,person_word,toast_collec;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        Bmob.initialize(getActivity(),"844b411fb7129f92886dad13103fde9f");

        View view = inflater.inflate(R.layout.fragment_personal, null);
		listView = (ListView) view.findViewById(R.id.personListView);
		person_name = (TextView) view.findViewById(R.id.person_name);
		person_word = (TextView) view.findViewById(R.id.person_word);
        toast_collec = (TextView) view.findViewById(R.id.toast_collec);
        initUserInfo();
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
                        deleteCollectInfo(cList.get(i));
//						boolean result = manager.addCateCollecInfo(cList.get(i));
//						if (!result){
//							collectBtn.setBackgroundResource(R.mipmap.cate_list_like_normal);
//							initData();
//							Toast.makeText(getActivity(), "取消收藏", Toast.LENGTH_SHORT).show();
//						}
						Toast.makeText(getActivity(), cList.get(i).getId(), Toast.LENGTH_SHORT).show();
					}
				});

				Intent intent = new Intent(getActivity(),CateDetailActivity.class);
                intent.putExtra("id",cList.get(i).getId());
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("cateDetailInfo",cateDetailInfos.get(i));
//				intent.putExtra("bundle",bundle);
				startActivity(intent);

			}
		});
		return view;
	}

    /**
     * 取消收藏
     * @param cateCollectionInfo 所选的美食
     */
    private void deleteCollectInfo(CateCollectionInfo cateCollectionInfo) {
        cateCollectionInfo.delete(cateCollectionInfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    collectBtn.setBackgroundResource(R.mipmap.cate_list_like_normal);
                    initData();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initUserInfo();
        initData();
    }

    /**
     * 初始化用户信息
     */
    private void initUserInfo() {
        UserInfo userInfo = UserInfo.getCurrentUser();
        person_name.setText(userInfo.getUsername());
        if (userInfo.getUserWord()==null||userInfo.getUserWord().equals("")){
            person_word.setText("我的美食宣言");
        }else{
            person_word.setText(userInfo.getUserWord());
        }
    }


    private void initData() {
        cList = new ArrayList<>();
        BmobQuery<CateCollectionInfo> bmobQuery = new BmobQuery<>("CateCollectionInfo");
        bmobQuery.addWhereEqualTo("userInfo", UserInfo.getCurrentUser());
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);

        bmobQuery.findObjects(new FindListener<CateCollectionInfo>() {
            @Override
            public void done(List<CateCollectionInfo> list, BmobException e) {
                if (e==null){
                    if (list.size()==0){
                        toast_collec.setVisibility(View.VISIBLE);
                    }else{
                        cList = list;
                        adapter.setmDatas(cList);
                        toast_collec.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        initAdapter();
    }

    private void initAdapter() {
        adapter = new CommonAdapter<CateCollectionInfo>(getActivity(), cList, R.layout.activity_cate_list_item) {
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
