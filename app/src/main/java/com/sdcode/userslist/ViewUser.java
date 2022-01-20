package com.sdcode.userslist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewUser extends AppCompatActivity {

    TextView userNameTV, fNameTV, lNameTV, genderTV, emailTV, phoneTV, bDateTV, hobiesTV;
    ImageView avatarImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        avatarImg = findViewById(R.id.avatarImg);
        userNameTV = findViewById(R.id.userNameTV);
        fNameTV = findViewById(R.id.fnameTV);
        lNameTV = findViewById(R.id.lnameTV);
        genderTV = findViewById(R.id.genderTV);
        emailTV = findViewById(R.id.emailTV);
        phoneTV = findViewById(R.id.phoneTV);
        bDateTV = findViewById(R.id.bDateTV);
        hobiesTV = findViewById(R.id.hobiesTV);


        Intent i = getIntent();
        final String username = i.getStringExtra("username");
        userNameTV.setText("Uid " + username);

        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursorUser = database.rawQuery("SELECT * FROM AllUsers", null);
        Cursor cursorHobies = database.rawQuery("SELECT * FROM hobbies", null);
//CREATE TABLE AllUsers(_id INTEGER PRIMARY KEY AUTOINCREMENT,FNAME VARCHAR(50),LNAME VARCHAR(50),UNAME VARCHAR(50),EMAIL VARCHAR(255),PHONE VARCHAR(255),GENDER_id INTEGER,BDATE DATETIME, FOREIGN KEY (GENDER_id) REFERENCES gender(_id))";
//create table hobbies(user_id INTEGER, hobbie VARCHAR(50), FOREIGN KEY (user_id) REFERENCES AllUsers(_id))";
        if (cursorUser.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No data!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursorUser.moveToNext()) {
                int userId = cursorUser.getInt(0);
                if (Integer.parseInt(username) == userId) {

                    String fname = cursorUser.getString(1);
                    String lname = cursorUser.getString(2);
                    String uname = cursorUser.getString(3);
                    int genderId = cursorUser.getInt(6);
                    String email = cursorUser.getString(4);
                    String phone = cursorUser.getString(5);
                    String bDate = cursorUser.getString(7);
                    hobiesTV = findViewById(R.id.hobiesTV);

                    fNameTV.setText(fname);
                    lNameTV.setText(lname);
                    userNameTV.setText(uname);
                    bDateTV.setText("dt - " + bDate);

                    if (genderId == 1) {
                        genderTV.setText("Male");
                        avatarImg.setImageDrawable(getResources().getDrawable(R.drawable.avatar_male));
                    } else {
                        genderTV.setText("Female");
                        avatarImg.setImageDrawable(getResources().getDrawable(R.drawable.avatar_female));
                    }

                    emailTV.setText(email);
                    phoneTV.setText(phone);

                    ArrayList<String> hobbies = new ArrayList<>();
                    if (cursorHobies.getCount() == 0) {
                        Toast.makeText(getApplicationContext(), "No Hobbies!", Toast.LENGTH_SHORT).show();
                    } else {
                        while (cursorHobies.moveToNext()) {
                            if (cursorHobies.getInt(0) == userId) {
                                hobbies.add(cursorHobies.getString(1));
                            }
                        }
                        hobiesTV.setText(hobbies.toString());

                    }
                }
            }
            cursorUser.close();
        }

    }
}