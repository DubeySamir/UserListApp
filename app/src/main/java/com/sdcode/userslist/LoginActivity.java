package com.sdcode.userslist;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.sdcode.userslist.base.BaseActivity;

public class LoginActivity extends BaseActivity {
MaterialButton btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();
        initData();
    }

    @Override
    protected void initUi(){
        super.initData();

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
    }

    @Override
    protected void initData(){
        super.initData();
    }
}