package com.catemaster.catemaster.activitys;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.catemaster.catemaster.R;
import com.catemaster.catemaster.bean.Post;
import com.catemaster.catemaster.bean.UserInfo;
import com.catemaster.catemaster.utils.ImagePathUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class FindAddActivity extends AppCompatActivity {
    EditText pTitle,pContent;
    TextView pImageText;
    ImageView pImage;
    Button postBtn;
    Post post = new Post();
    BmobFile imageFile = new BmobFile();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_add);
        initView();
    }

    private void initView() {
        pTitle = (EditText) findViewById(R.id.post_title);
        pContent = (EditText) findViewById(R.id.post_content);
        pImageText = (TextView) findViewById(R.id.post_img_text);
        pImage = (ImageView) findViewById(R.id.post_img);
        postBtn = (Button) findViewById(R.id.postBtn);
        pImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("crop", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 2);

            }
        });
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAddPost();
            }
        });
    }

    /**
     * 获取手机相册图片
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            pImageText.setVisibility(View.GONE);
            Uri uri = data.getData();

            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bmp = BitmapFactory
                        .decodeStream(cr.openInputStream(uri));
                pImage.setImageBitmap(bmp);
                String path = ImagePathUtils.getRealPathFromUri(FindAddActivity.this,uri);
                doPostImageFile(path);

                Log.i("图片路径：：：",path);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void doPostImageFile(String path) {
        postBtn.setClickable(false);
        imageFile = new BmobFile(new File(path));
        imageFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    String url = imageFile.getFileUrl();
                    postBtn.setClickable(true);

                    //post.setImage(imageFile);
                }
            }

        });
    }

    /**
     * 发表帖子
     */
    private void doAddPost() {

        post.setTitle(pTitle.getText().toString().trim());
        post.setContent(pContent.getText().toString().trim());
        post.setAuthor(UserInfo.getCurrentUser());
        post.setImage(imageFile);
        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                Toast.makeText(FindAddActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void backClick(View view) {
        finish();
    }
}
