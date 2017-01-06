package com.catemaster.catemaster.activitys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.catemaster.catemaster.bean.CateInfo;
import com.catemaster.catemaster.bean.MainRecipe;
import com.catemaster.catemaster.constants.ConstantUtils;
import com.catemaster.catemaster.utils.CommonAdapter;
import com.catemaster.catemaster.utils.ViewHolder;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CateListActivity extends Activity {
    private List<MainRecipe> list;
    ListView cateListView;
    TextView titleTv;
    List<CateInfo.ResultBean.CateDetailInfo> cateDetailInfos = new ArrayList<>();
    CommonAdapter<CateInfo.ResultBean.CateDetailInfo> adapter;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catelist);
        titleTv = (TextView) findViewById(R.id.tv_title);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        int cid = intent.getIntExtra("cid", 1);
        titleTv.setText(title + "");
        queue = Volley.newRequestQueue(this);
        GetInternetData(cid);
        cateListView = (ListView) findViewById(R.id.cateList_lv);
        cateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CateListActivity.this,CateDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cateDetailInfo",cateDetailInfos.get(i));
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
    }

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
                return maps;
            }
        };
        queue.add(request);
        queue.start();
    }

    private void initAdapter() {
        adapter = new CommonAdapter<CateInfo.ResultBean.CateDetailInfo>(this,cateDetailInfos,R.layout.activity_cate_list_item) {
            @Override
            public void convert(ViewHolder helper, CateInfo.ResultBean.CateDetailInfo item) {
                helper.setText(R.id.cateNameTv,item.getTitle());
                helper.setImageByUrl(R.id.cate_img,item.getAlbums().get(0));
            }

        };
        cateListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void backClick(View view) {
        finish();
    }
}
