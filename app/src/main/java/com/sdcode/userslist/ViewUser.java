package com.sdcode.userslist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.sdcode.userslist.classes.Messagee;
import com.sdcode.userslist.base.BaseActivity;

import java.util.ArrayList;

public class ViewUser extends BaseActivity {

    TextView userNameTV, fNameTV, lNameTV, genderTV, emailTV, phoneTV, bDateTV, hobbiesTV;
    ImageView avatarImg;
    MaterialButton deleteUser,editUser;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        initUi();
        initData();


        Intent i = getIntent();
        final String userId = i.getStringExtra("userId");
        userNameTV.setText(userId);

        deleteUser.setOnClickListener(view -> deleteUser(userId));

        editUser.setOnClickListener(view -> {
            Intent i1 = new Intent(getApplicationContext(), AddUser.class);
            i1.putExtra("action", "EditUser");
            i1.putExtra("userId", userId);
            startActivity(i1);
        });

        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursorUser = database.rawQuery(getString(R.string.SelectAllUsersData), null);
        @SuppressLint("Recycle") Cursor cursorHobbies = database.rawQuery(getString(R.string.SelectAllHobbiesData), null);

        if (cursorUser.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No data!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursorUser.moveToNext()) {
                int tempUserId = cursorUser.getInt(0);
                if (Integer.parseInt(userId) == tempUserId) {

                    String fName = cursorUser.getString(1);
                    String lName = cursorUser.getString(2);
                    String uname = cursorUser.getString(3);
                    int genderId = cursorUser.getInt(6);
                    String email = cursorUser.getString(4);
                    String phone = cursorUser.getString(5);
                    String bDate = cursorUser.getString(7);
                    hobbiesTV = findViewById(R.id.hobiesTV);


                    fNameTV.setText(fName);
                    lNameTV.setText(lName);
                    userNameTV.setText(uname);
                    bDateTV.setText(bDate);

                    if (genderId == 1) {
                        genderTV.setText(R.string.Male);
                        avatarImg.setImageDrawable(getResources().getDrawable(R.drawable.avatar_male));
                    } else {
                        genderTV.setText(R.string.Female);
                        avatarImg.setImageDrawable(getResources().getDrawable(R.drawable.avatar_female));
                    }

                    emailTV.setText(email);
                    phoneTV.setText(phone);

                    ArrayList<String> hobbies = new ArrayList<>();
                    if (cursorHobbies.getCount() == 0) {
                        Toast.makeText(getApplicationContext(), "No Hobbies!", Toast.LENGTH_SHORT).show();
                    } else {
                        while (cursorHobbies.moveToNext()) {
                            if (cursorHobbies.getInt(0) == tempUserId) {
                                hobbies.add(cursorHobbies.getString(1));
                            }
                        }
                        hobbiesTV.setText(hobbies.toString());

                    }
                }
            }
            cursorUser.close();
            database.close();
        }

    }

    private void deleteUser(String userId) {
        SQLiteDatabase db = helper.getWritableDatabase();

        int isDeleted =  db.delete("AllUsers","_id = "+ userId,null);

        if(isDeleted > 0){
            db.close();
            Messagee.message(getApplicationContext(),"User Deleted");
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }else{
            Messagee.message(getApplicationContext(),"Can't delete User");
        }
        db.close();
    }

    @Override
    protected void initUi(){
        super.initData();

        avatarImg = findViewById(R.id.avatarImg);
        userNameTV = findViewById(R.id.userNameTV);
        fNameTV = findViewById(R.id.fnameTV);
        lNameTV = findViewById(R.id.lnameTV);
        genderTV = findViewById(R.id.genderTV);
        emailTV = findViewById(R.id.emailTV);
        phoneTV = findViewById(R.id.phoneTV);
        bDateTV = findViewById(R.id.bDateTV);
        hobbiesTV = findViewById(R.id.hobiesTV);
        deleteUser = findViewById(R.id.deleteUser);
        editUser = findViewById(R.id.editUser);
    }

    @Override
    protected void initData(){
        super.initData();
    }

}