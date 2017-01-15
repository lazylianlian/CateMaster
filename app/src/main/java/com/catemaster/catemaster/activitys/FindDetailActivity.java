package com.catemaster.catemaster.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.catemaster.catemaster.R;
import com.catemaster.catemaster.bean.Comment;
import com.catemaster.catemaster.bean.Post;
import com.catemaster.catemaster.bean.UserInfo;
import com.catemaster.catemaster.utils.CommonAdapter;
import com.catemaster.catemaster.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class FindDetailActivity extends AppCompatActivity {
    TextView fd_title,fd_author,fd_content, doComment;
    EditText et_coment;
    Post post;
    ListView listView;
    List<Comment> commentList;
    CommonAdapter<Comment> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_detail);
        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");
        initView();
        getCommentList();


    }

    private void initView() {
        fd_title = (TextView) findViewById(R.id.fd_title);
        fd_author = (TextView) findViewById(R.id.fd_userName);
        fd_content = (TextView) findViewById(R.id.fd_content);
        doComment = (TextView) findViewById(R.id.doComent);
        et_coment = (EditText) findViewById(R.id.fd_coment);
        listView = (ListView) findViewById(R.id.comentList);
        commentList = new ArrayList<>();
        fd_title.setText(post.getTitle());
        fd_content.setText(post.getContent());
        fd_author.setText(post.getAuthor().getUsername());
        doComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.setPost(post);
                comment.setUser(UserInfo.getCurrentUser());
                comment.setContent(et_coment.getText().toString());
                comment.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null){
                            Toast.makeText(FindDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                            getCommentList();
                            adapter.setmDatas(commentList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    private void getCommentList() {
        BmobQuery<Comment> query = new BmobQuery<>("Comment");
        query.addWhereEqualTo("post",new BmobPointer(post));
        query.include("user,post.author");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e==null&&list!=null){
                    commentList = list;
                    initAdapter();
                    Toast.makeText(FindDetailActivity.this, "评论列表"+list.size(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(FindDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void initAdapter() {
        adapter = new CommonAdapter<Comment>(this,commentList,R.layout.activity_find_detail_comm) {
            @Override
            public void convert(ViewHolder helper, Comment item) {
                helper.setText(R.id.com_content,item.getContent());
                helper.setText(R.id.com_author,item.getUser().getUsername());
            }
        };
        listView.setAdapter(adapter);
    }
    public void backClick(View view) {
        finish();
    }
}
