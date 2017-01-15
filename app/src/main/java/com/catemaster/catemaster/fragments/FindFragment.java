package com.catemaster.catemaster.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.catemaster.catemaster.R;
import com.catemaster.catemaster.activitys.FindAddActivity;
import com.catemaster.catemaster.activitys.FindDetailActivity;
import com.catemaster.catemaster.bean.Post;
import com.catemaster.catemaster.utils.CommonAdapter;
import com.catemaster.catemaster.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FindFragment extends Fragment {
	List<Post> postList = new ArrayList<>();
	CommonAdapter<Post> adapter;
	ListView listView;
	Button find_addBtn;
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        Bmob.initialize(getActivity(),"844b411fb7129f92886dad13103fde9f");
		View view = inflater.inflate(R.layout.fragment_find, null);
		listView = (ListView) view.findViewById(R.id.findListView);
		find_addBtn = (Button) view.findViewById(R.id.find_addBtn);
        find_addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FindAddActivity.class);
                startActivity(intent);
            }
        });

        initData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Post post = postList.get(i);
                Intent intent = new Intent(getActivity(),FindDetailActivity.class);
                intent.putExtra("post",postList.get(i));
                startActivity(intent);
            }
        });
		return view;
	}

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        BmobQuery<Post> query = new BmobQuery<>("Post");
        query.order("-updatedAt");
        query.include("author.username");

        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if(e==null){
                    postList = list;
                    initAdapter();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "获取成功"+postList.size(), Toast.LENGTH_SHORT).show();
                }
            }
        });

	}

    private void initAdapter() {
        adapter = new CommonAdapter<Post>(getActivity(),postList,R.layout.fragment_find_list_item) {
            @Override
            public void convert(ViewHolder helper, Post item) {
                helper.setText(R.id.find_userName,item.getAuthor().getUsername());
                helper.setText(R.id.fd_title,item.getTitle());
                helper.setText(R.id.fd_content,item.getContent());
                if (item.getImage()!=null){
                    helper.setImageByUrl(R.id.fd_img,item.getImage().getFileUrl());
                }else{
                    helper.setImageResource(R.id.fd_img,R.mipmap.grid_hongbei);
                }
            }
        };
        listView.setAdapter(adapter);
    }
}
