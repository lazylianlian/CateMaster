package com.catemaster.catemaster.activitys;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.catemaster.catemaster.R;
import com.catemaster.catemaster.bean.CateCollectionInfo;
import com.catemaster.catemaster.bean.CateInfo;
import com.catemaster.catemaster.dao.DBManager;
import com.catemaster.catemaster.utils.CommonAdapter;
import com.catemaster.catemaster.utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 美食详情
 *
 * @author 文捷
 */
public class CateDetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView titleTv, tv_tag, tv_intro, tv_ingre, tv_burden;
    ImageView img;
    TextView share_qq, share_wechat, share_wechatcomm, share_qzone, share_sinaweibo, share_tecentweibo, share_twitter, share_facebook;
    Button collectBtn, shareBtn;
    View view;//popWindow的布局V
    PopupWindow popupWindow;
    DBManager manager;//收藏美食数据库操作类对象
    CateInfo.ResultBean.CateDetailInfo detailInfo;//美食详情实体类
    ImageLoader imgLoader;
    DisplayImageOptions options;

    ListView listView;
    CommonAdapter<CateInfo.ResultBean.CateDetailInfo.CateStepsInfo> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_detail);
        ShareSDK.initSDK(this);
        Intent intent = getIntent();
        detailInfo = (CateInfo.ResultBean.CateDetailInfo) intent.getBundleExtra("bundle").getSerializable("cateDetailInfo");
        imgLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.cate_default)
                .showImageOnFail(R.mipmap.cate_default)
                .build();
        initView();
    }
    /*
     *   V初始化
     */
    private void initView() {
        titleTv = (TextView) findViewById(R.id.tv_title);
        tv_tag = (TextView) findViewById(R.id.detail_tag);
        tv_intro = (TextView) findViewById(R.id.detail_intro);
        tv_ingre = (TextView) findViewById(R.id.detail_ingredients);
        tv_burden = (TextView) findViewById(R.id.detail_burden);
        img = (ImageView) findViewById(R.id.detail_img);
        listView = (ListView) findViewById(R.id.detail_stepListView);
        listView.setFocusable(false);
        shareBtn = (Button) findViewById(R.id.shareBtn);
        collectBtn = (Button) findViewById(R.id.detail_collect);
        titleTv.setText(detailInfo.getTitle());
        tv_tag.setText(detailInfo.getTags());
        tv_intro.setText(detailInfo.getImtro());
        tv_ingre.setText(detailInfo.getIngredients());
        tv_burden.setText(detailInfo.getBurden());
        if (detailInfo.getAlbums().size() == 0) {

        } else {
            String imgUrl = detailInfo.getAlbums().get(0);
            imgLoader.displayImage(imgUrl, img, options);
        }

        manager = DBManager.getInstance(CateDetailActivity.this);
        if (manager.isCollected(detailInfo.getId())) {
            collectBtn.setBackgroundResource(R.mipmap.cate_list_like_normal);
        } else {
            collectBtn.setBackgroundResource(R.mipmap.cate_list_like_click);
        }
        view = getLayoutInflater().inflate(R.layout.activity_cate_detail_popwindow, null);
        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initPopView();
        initListener();
        initAdapter();
    }
    /*
     *   初始化popWindow中的分享控件
     */
    private void initPopView() {
        share_qq = (TextView) view.findViewById(R.id.qq);
        share_qzone = (TextView) view.findViewById(R.id.qzone);
        share_sinaweibo = (TextView) view.findViewById(R.id.sinaweibo);
        share_facebook = (TextView) view.findViewById(R.id.facebook);
        share_tecentweibo = (TextView) view.findViewById(R.id.tecentweibo);
        share_twitter = (TextView) view.findViewById(R.id.twitter);
        share_wechat = (TextView) view.findViewById(R.id.wechat);
        share_wechatcomm = (TextView) view.findViewById(R.id.wechatmoments);
        share_wechatcomm.setOnClickListener(this);
        share_wechat.setOnClickListener(this);
        share_twitter.setOnClickListener(this);
        share_tecentweibo.setOnClickListener(this);
        share_facebook.setOnClickListener(this);
        share_sinaweibo.setOnClickListener(this);
        share_qzone.setOnClickListener(this);
        share_qq.setOnClickListener(this);

    }
    /*
     *   初始化步骤列表适配器
     */
    private void initAdapter() {
        adapter = new CommonAdapter<CateInfo.ResultBean.CateDetailInfo.CateStepsInfo>(CateDetailActivity.this, detailInfo.getSteps(), R.layout.activity_cate_detail_expand_item) {
            @Override
            public void convert(ViewHolder helper, CateInfo.ResultBean.CateDetailInfo.CateStepsInfo item) {
                helper.setText(R.id.step_text, item.getStep());
                helper.setImageByUrl(R.id.step_img, item.getImg());
            }
        };
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
    }
    /*
     *   添加popWindow中的分享控件事件监听
     */
    private void initListener() {
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopWindow();
            }
        });
        collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CateInfo.ResultBean.CateDetailInfo cInfo = detailInfo;
                CateCollectionInfo cateCollectionInfo = new CateCollectionInfo(cInfo.getId(), cInfo.getTitle(), cInfo.getAlbums().get(0), 1);
                boolean result = manager.addCateCollecInfo(cateCollectionInfo);
                if (result) {
                    collectBtn.setBackgroundResource(R.mipmap.cate_list_like_click);
                } else {
                    collectBtn.setBackgroundResource(R.mipmap.cate_list_like_normal);
                }
            }
        });
    }
    /*
     *   popWindow显示隐藏
     */
    private void showPopWindow() {
        //第三方分享
        if (!popupWindow.isShowing()) {
            //在底部显示
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            popupWindow.setAnimationStyle(R.style.myPopWindowAnim);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);
            backgroundAlpha(0.5f);
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (popupWindow.isShowing()) {
                        backgroundAlpha(1);
                        popupWindow.dismiss();
                    }
                    return false;
                }
            });
        } else {
            backgroundAlpha(1);
            popupWindow.dismiss();
        }
    }
    /*
     *   设置窗体背景颜色变化
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = bgAlpha;
        getWindow().setAttributes(layoutParams);
    }
    /*
     *   代码计算ListView的高度
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        CommonAdapter<CateInfo.ResultBean.CateDetailInfo.CateStepsInfo> listAdapter = (CommonAdapter<CateInfo.ResultBean.CateDetailInfo.CateStepsInfo>) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + 300 + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public void backClick(View view) {
        finish();
    }
    /*
     *   点击事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qq://qq
                Toast.makeText(this, "dianji", Toast.LENGTH_SHORT).show();
                Platform qq = ShareSDK.getPlatform(this, QQ.NAME);
                QQ.ShareParams qqq = new QQ.ShareParams();
                qqq.setText("美食研究生——"+detailInfo.getTitle());
                qqq.setImageUrl(detailInfo.getAlbums().get(0));
                qq.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Log.d("wyb", "onComplete");
                        backgroundAlpha(1);
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Log.d("wyb", "onError" + throwable.toString());
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Log.d("wyb", "onCancel");
                    }
                });
                qq.share(qqq);
                break;
            case R.id.qzone://qq空间
                QZone.ShareParams qzsp = new QZone.ShareParams();
                qzsp.setText(detailInfo.getTitle());
                qzsp.setImageUrl(detailInfo.getAlbums().get(0));
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Toast.makeText(CateDetailActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                        backgroundAlpha(1);
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Toast.makeText(CateDetailActivity.this, "分享失败", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Toast.makeText(CateDetailActivity.this, "分享取消", Toast.LENGTH_SHORT).show();
                        backgroundAlpha(1);
                        popupWindow.dismiss();
                    }
                });
                qzone.share(qzsp);
                break;
            case R.id.tecentweibo://腾讯微博
                Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
                Platform tecentweibo = ShareSDK.getPlatform(this, TencentWeibo.NAME);
                TencentWeibo.ShareParams tecentweibosp = new TencentWeibo.ShareParams();
                tecentweibosp.setText("美食研究生——"+detailInfo.getTitle());
                tecentweibosp.setImageUrl(detailInfo.getAlbums().get(0));
                tecentweibo.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Log.d("wyb", "onComplete");
                        backgroundAlpha(1);
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Log.d("wyb", "onError" + throwable.toString());
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Log.d("wyb", "onCancel");
                    }
                });
                tecentweibo.share(tecentweibosp);
                break;
            case R.id.sinaweibo://新浪微博
                Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
                Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
                SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
                sp.setText("美食研究生——"+detailInfo.getTitle());
                //sp.setImageUrl(detailInfo.getAlbums().get(0));
                weibo.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Log.d("wyb", "onComplete");
                        backgroundAlpha(1);
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Log.d("wyb", "onError" + throwable.toString());
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Log.d("wyb", "onCancel");
                    }
                });
                weibo.share(sp);

//                OnekeyShare share = new OnekeyShare();
//                share.disableSSOWhenAuthorize();
//                share.setText("text");
//                // text是分享文本，所有平台都需要这个字段
//                share.setTitle("title");
//                // url仅在微信（包括好友和朋友圈）中使用
//                share.setUrl("http://sweetystory.com/");
//                share.setTitleUrl("http://sweetystory.com/");
//                share.setImageUrl("http://sweetystory.com/Public/ttwebsite/theme1/style/img/special-1.jpg");
//                share.setCallback(new PlatformActionListener() {
//                    @Override
//                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//
//                    }
//
//                    @Override
//                    public void onError(Platform platform, int i, Throwable throwable) {
//                        Log.d("TAG", throwable.toString());
//                    }
//
//                    @Override
//                    public void onCancel(Platform platform, int i) {
//
//                    }
//                });
//                share.show(mContext);

                break;
            case R.id.wechat://微信
                Platform wechat = ShareSDK.getPlatform(this, Wechat.NAME);
                Wechat.ShareParams wesp = new Wechat.ShareParams();
                wesp.setShareType(Platform.SHARE_IMAGE);
                wesp.setText("美食研究生——"+detailInfo.getTitle());
                wesp.setImageUrl(detailInfo.getAlbums().get(0));
                wechat.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Log.d("wyb", "onComplete");
                        backgroundAlpha(1);
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Log.d("wyb", "onError" + throwable.toString());
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Log.d("wyb", "onCancel");
                    }
                });
                wechat.share(wesp);
                break;
            case R.id.wechatmoments://朋友圈
                Platform wecomchat = ShareSDK.getPlatform(this, WechatMoments.NAME);
                WechatMoments.ShareParams wecomsp = new WechatMoments.ShareParams();
                wecomsp.setShareType(Platform.SHARE_IMAGE);
                wecomsp.setText("美食研究生测试文本——"+detailInfo.getTitle());
                wecomsp.setImageUrl(detailInfo.getAlbums().get(0));
                wecomchat.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Log.d("wyb", "onComplete");
                        backgroundAlpha(1);
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Log.d("wyb", "onError" + throwable.toString());
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Log.d("wyb", "onCancel");
                    }
                });
                wecomchat.share(wecomsp);
                break;
            case R.id.twitter://twitter
                Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
                Platform twitter = ShareSDK.getPlatform(this, Twitter.NAME);
                Twitter.ShareParams twittersp = new Twitter.ShareParams();
                twittersp.setText("美食研究生——"+detailInfo.getTitle());
                twittersp.setImageUrl(detailInfo.getAlbums().get(0));
                twitter.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Log.d("wyb", "onComplete");
                        backgroundAlpha(1);
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Log.d("wyb", "onError" + throwable.toString());
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Log.d("wyb", "onCancel");
                    }
                });
                twitter.share(twittersp);

                break;
            case R.id.facebook://facebook
                Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
                Platform facebook = ShareSDK.getPlatform(this, Facebook.NAME);
                Facebook.ShareParams facebooksp = new Facebook.ShareParams();
                facebooksp.setText("美食研究生——"+detailInfo.getTitle());
                facebooksp.setImageUrl(detailInfo.getAlbums().get(0));
                facebook.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Log.d("wyb", "onComplete");
                        backgroundAlpha(1);
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Log.d("wyb", "onError" + throwable.toString());
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Log.d("wyb", "onCancel");
                    }
                });
                facebook.share(facebooksp);
                break;
            default:
                break;
        }
    }

}
