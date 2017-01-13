package com.catemaster.catemaster.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.catemaster.catemaster.R;
import com.catemaster.catemaster.bean.Post;
import com.catemaster.catemaster.bean.UserInfo;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class FindAddActivity extends AppCompatActivity {
    EditText pTitle,pContent;
    TextView pImage;
    Button postBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_add);
        initView();
    }

    private void initView() {
        pTitle = (EditText) findViewById(R.id.post_title);
        pContent = (EditText) findViewById(R.id.post_content);
        pImage = (TextView) findViewById(R.id.post_img);
        postBtn = (Button) findViewById(R.id.postBtn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAddPost();
            }
        });
    }

    private void doAddPost() {
        Post post = new Post();
        post.setTitle(pTitle.getText().toString().trim());
        post.setContent(pContent.getText().toString().trim());
        post.setAuthor(UserInfo.getCurrentUser());
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
