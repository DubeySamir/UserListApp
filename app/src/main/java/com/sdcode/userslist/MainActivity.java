package com.sdcode.userslist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    UserRVAdapter userRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(getApplicationContext());

        userRVAdapter = new UserRVAdapter(databaseHelper.getAllUsersData(),getApplicationContext());

        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        usersRecyclerView.setAdapter(userRVAdapter);

        userRVAdapter.setOnItemClickListener(new UserRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Integer userID = userRVAdapter.getUserId(position);
//                Messagee.message(getApplicationContext(),Integer.toString(userID));

                Intent i = new Intent(getApplicationContext(), ViewUser.class);
                i.putExtra("userId", Integer.toString(userID));
                startActivity(i);
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu ,menu);
        return true;
    }

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