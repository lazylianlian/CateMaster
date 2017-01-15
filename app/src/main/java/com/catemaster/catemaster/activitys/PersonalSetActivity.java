package com.catemaster.catemaster.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.catemaster.catemaster.R;
import com.catemaster.catemaster.bean.UserInfo;

public class PersonalSetActivity extends AppCompatActivity {
    EditText set_name,set_email,set_word;
    Button setBtn,clearUserBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_set);
        initView();
    }

    private void initView() {
        set_name = (EditText) findViewById(R.id.set_name);
        set_email = (EditText) findViewById(R.id.set_email);
        set_word = (EditText) findViewById(R.id.set_word);
        setBtn = (Button) findViewById(R.id.setBtn);
        clearUserBtn = (Button) findViewById(R.id.clearCurrentBtn);
        UserInfo userInfo = UserInfo.getCurrentUser();
        set_name.setText(userInfo.getUsername());
        set_word.setText(userInfo.getUserWord());
        set_email.setText(userInfo.getEmail());

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        clearUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void backClick(View view) {
        finish();
    }

}
