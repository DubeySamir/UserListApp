package com.sdcode.userslist.base;

import androidx.appcompat.app.AppCompatActivity;

import com.sdcode.userslist.database.DatabaseHelper;

public class BaseActivity extends AppCompatActivity {
    protected DatabaseHelper helper;

    protected void initData(){
        helper = new DatabaseHelper(getApplicationContext());
    }

    protected void initUi(){

    }

}
