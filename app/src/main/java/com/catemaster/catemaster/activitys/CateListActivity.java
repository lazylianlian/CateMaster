package com.catemaster.catemaster.activitys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.catemaster.catemaster.R;
import com.catemaster.catemaster.bean.CateCollectionInfo;
import com.catemaster.catemaster.bean.CateInfo;
import com.catemaster.catemaster.constants.ConstantUtils;
import com.catemaster.catemaster.dao.DBManager;
import com.catemaster.catemaster.utils.CommonAdapter;
import com.catemaster.catemaster.utils.ViewHolder;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;

public class CateListActivity extends Activity {
    ListView cateListView;
    TextView titleTv;
    List<CateInfo.ResultBean.CateDetailInfo> cateDetailInfos = new ArrayList<>();
    CommonAdapter<CateInfo.ResultBean.CateDetailInfo> adapter;
    RequestQueue queue;
    DBManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catelist);
        queue = Volley.newRequestQueue(this);

        titleTv = (TextView) findViewById(R.id.tv_title);
        cateListView = (ListView) findViewById(R.id.cateList_lv);
        manager = DBManager.getInstance(CateListActivity.this);

        Intent intent = getIntent();
        if (intent.getStringExtra("title")!=null){
            String title = intent.getStringExtra("title");
            int cid = intent.getIntExtra("cid", 1);
            titleTv.setText(title + "");
            GetInternetData(cid);
        }else{
            String title = intent.getStringExtra("searchT");
            titleTv.setText(title + "");

            CateInfo cateInfo = (CateInfo)intent.getSerializableExtra("searchCateInfo");
            cateDetailInfos = cateInfo.getResult().getData();
            initAdapter();
        }

        cateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


                Intent intent = new Intent(CateListActivity.this,CateDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cateDetailInfo",cateDetailInfos.get(i));
                intent.putExtra("bundle",bundle);
                startActivity(intent);

            }
        });
    }
    /*
     *  获取网络数据
     */
    private void GetInternetData(final int cid) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ConstantUtils.JUHE_TAG_SEARCH_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                Log.i("-----",s);
                CateInfo cateInfo = gson.fromJson(s, CateInfo.class);
                cateDetailInfos = cateInfo.getResult().getData();
                initAdapter();
                Toast.makeText(CateListActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String err = volleyError.getCause().toString();
                Toast.makeText(CateListActivity.this, err, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> maps = new HashMap<String,String>();
                maps.put("key",ConstantUtils.JUHE_CATE_KEY);
                maps.put("cid",cid+"");
                maps.put("pn","1");

                return maps;
            }
        };
        queue.add(request);
        queue.start();
    }

    private void initAdapter() {
        adapter = new CommonAdapter<CateInfo.ResultBean.CateDetailInfo>(this,cateDetailInfos,R.layout.activity_cate_list_item) {
            @Override
            public void convert(ViewHolder helper, final CateInfo.ResultBean.CateDetailInfo item) {
                helper.setText(R.id.cateNameTv, item.getTitle());
                helper.setImageByUrl(R.id.cate_img, item.getAlbums().get(0));
                if(manager.isCollected(item.getId())){
                    helper.setBtnImageResource(R.id.collectBtn,R.mipmap.cate_list_like_normal);
                }else{
                    helper.setBtnImageResource(R.id.collectBtn,R.mipmap.cate_list_like_click);
                }
                final Button collectBtn = helper.getView(R.id.collectBtn);
                collectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CateInfo.ResultBean.CateDetailInfo cInfo = item;
                        CateCollectionInfo cateCollectionInfo = new CateCollectionInfo(cInfo.getId(), cInfo.getTitle(), cInfo.getAlbums().get(0), 1);
                        boolean result = manager.addCateCollecInfo(cateCollectionInfo);
                        if (result){
                            collectBtn.setBackgroundResource(R.mipmap.cate_list_like_click);
                            Toast.makeText(CateListActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        }else{
                            collectBtn.setBackgroundResource(R.mipmap.cate_list_like_normal);
                            Toast.makeText(CateListActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(CateListActivity.this, cInfo.getId(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        cateListView.setAdapter(adapter);

        cateListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(),true,true));
    }

    public void backClick(View view) {
        finish();
    }
}
