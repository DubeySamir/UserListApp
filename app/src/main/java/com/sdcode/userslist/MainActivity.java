package com.sdcode.userslist;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.sdcode.userslist.adapters.UserRVAdapter;
import com.sdcode.userslist.base.BaseClass;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends BaseClass {

    UserRVAdapter userRVAdapter;
    RecyclerView usersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
        initData();

        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersRecyclerView.setAdapter(userRVAdapter);

        userRVAdapter.setOnItemClickListener(position -> {
            Integer userID = userRVAdapter.getUserId(position);

            Intent i = new Intent(getApplicationContext(), ViewUser.class);
            i.putExtra("userId", Integer.toString(userID));
            startActivity(i);
        });
    }

    @Override
    protected void initUi(){
        super.initData();

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
    }

    @Override
    protected void initData(){
        super.initData();

        userRVAdapter = new UserRVAdapter(helper.getAllUsersData(),getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu ,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_user:
                Intent i = new Intent(getApplicationContext(), AddUser.class);
                i.putExtra("action", "AddUser");
                i.putExtra("userId", "NoId");
                startActivity(i);
                return true;
            case R.id.menu_logout:
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}