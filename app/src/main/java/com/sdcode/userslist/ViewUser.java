package com.sdcode.userslist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ViewUser extends AppCompatActivity {

    TextView userNameTV, fNameTV, lNameTV, genderTV, emailTV, phoneTV, bDateTV, hobiesTV;
    ImageView avatarImg;
    MaterialButton deleteUser,editUser;

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
        deleteUser = findViewById(R.id.deleteUser);
        editUser = findViewById(R.id.editUser);




        Intent i = getIntent();
        final String userId = i.getStringExtra("userId");
        userNameTV.setText(userId);


        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                int isDeleted =  db.delete("AllUsers","_id = "+ userId,null);

                if(isDeleted > 0){
                    db.close();
                    Messagee.message(getApplicationContext(),"User Deleted");
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else{
                    Messagee.message(getApplicationContext(),"Can't delete User");
                }
            }
        });


        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddUser.class);
                i.putExtra("action", "EditUser");
                i.putExtra("userId", userId);
                startActivity(i);
            }
        });


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
                int tempUserId = cursorUser.getInt(0);
                if (Integer.parseInt(userId) == tempUserId) {

                    String fName = cursorUser.getString(1);
                    String lName = cursorUser.getString(2);
                    String uname = cursorUser.getString(3);
                    int genderId = cursorUser.getInt(6);
                    String email = cursorUser.getString(4);
                    String phone = cursorUser.getString(5);
                    String bDate = cursorUser.getString(7);
                    hobiesTV = findViewById(R.id.hobiesTV);

                    Log.d("View User cursor", "User Data = " +  DatabaseUtils.dumpCursorToString(cursorUser));

                    fNameTV.setText(fName);
                    lNameTV.setText(lName);
                    userNameTV.setText(uname);
                    bDateTV.setText(bDate);

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
                            if (cursorHobies.getInt(0) == tempUserId) {
                                hobbies.add(cursorHobies.getString(1));
                            }
                        }
                        hobiesTV.setText(hobbies.toString());

                    }
                }
            }
            cursorUser.close();
            database.close();
        }

    }
}