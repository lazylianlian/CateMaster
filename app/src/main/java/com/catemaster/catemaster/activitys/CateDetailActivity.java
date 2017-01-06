package com.catemaster.catemaster.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.catemaster.catemaster.R;
import com.catemaster.catemaster.bean.CateInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 美食详情
 * @author 文捷
 */
public class CateDetailActivity extends AppCompatActivity {
    TextView titleTv,tv_tag,tv_intro;
    ImageView img;
    CateInfo.ResultBean.CateDetailInfo detailInfo;
    ImageLoader imgLoader;
    DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_detail);
        initView();
        Intent intent = getIntent();
        detailInfo = (CateInfo.ResultBean.CateDetailInfo) intent.getBundleExtra("bundle").getSerializable("cateDetailInfo");
        imgLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.cate_default)
                .showImageOnFail(R.mipmap.cate_default)
                .build();

    }

    private void initView() {
        titleTv = (TextView) findViewById(R.id.tv_title);
        tv_tag = (TextView) findViewById(R.id.detail_tag);
        tv_intro = (TextView) findViewById(R.id.detail_intro);
        img = (ImageView) findViewById(R.id.detail_img);
        titleTv.setText(detailInfo.getTitle());
        tv_tag.setText(detailInfo.getTags());
        tv_intro.setText(detailInfo.getImtro());
        if (detailInfo.getAlbums().size()==0){

        }else{
            String imgUrl = detailInfo.getAlbums().get(0);
            imgLoader.displayImage(imgUrl,img,options);
        }

    }

    public void backClick(View view){
        finish();
    }
}
