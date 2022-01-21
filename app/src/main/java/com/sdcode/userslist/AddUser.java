package com.sdcode.userslist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Date;

public class AddUser extends AppCompatActivity {

    TextInputLayout firstNameInputLayout, lastNameInputLayout, userNameInputLayout, emailInputLayout, phoneInputLayout, date_picker_InputLayout;
    TextInputEditText firstNameInputEditText, lastNameInputEditText, userNameInputEditText, emailInputEditText, phoneInputEditText, date_picker_InputEditText;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton, maleRadioButton, femaleRadioButton;
    MaterialCheckBox chk_cricket, chk_movies, chk_reading, chk_writing;
    Button btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
//        getSupportActionBar().setTitle("Users");
        initialization();


        Intent i = getIntent();
        final String action = i.getStringExtra("action");
        final String userId = i.getStringExtra("userId");


        if (action.equals("EditUser")) {
            btnAddUser.setText("Update");
            DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
            SQLiteDatabase readableDatabase = helper.getReadableDatabase();

            Cursor userDataCursor = readableDatabase.rawQuery("select * from AllUsers where _id = " + userId, null);

            Log.d("Edit User cursor", "User Data = " +  DatabaseUtils.dumpCursorToString(userDataCursor));

            if(userDataCursor.getCount() == 0){
                Messagee.message(getApplicationContext(),"User Not Found");
            }else{
                userDataCursor.moveToFirst();
                do{
                    String fName = userDataCursor.getString(1);
                    String lName = userDataCursor.getString(2);
                    String uname = userDataCursor.getString(3);
                    int genderId = userDataCursor.getInt(6);
                    String email = userDataCursor.getString(4);
                    String phone = userDataCursor.getString(5);
                    String bDate = userDataCursor.getString(7);


                    firstNameInputEditText.setText(fName);
                    lastNameInputEditText.setText(lName + " - " + genderId);
                    userNameInputEditText.setText(uname);
                    emailInputEditText.setText(email);
                    phoneInputEditText.setText(phone);
                    date_picker_InputEditText.setText(bDate);

                    if(genderId == 1){
                        maleRadioButton.setChecked(true);
                    }else{
                        femaleRadioButton.setChecked(true);
                    }

                    Cursor hobbiesData = readableDatabase.rawQuery("select * from hobbies where user_id LIKE " + userId, null);

                    hobbiesData.moveToPosition(-1);
                    while (hobbiesData.moveToNext()) {
                        if (hobbiesData.getString(1).equals("Cricket")) {
                            chk_cricket.setChecked(true);
                        }
                        if (hobbiesData.getString(1).equals("Movies")) {
                            chk_movies.setChecked(true);
                        }
                        if (hobbiesData.getString(1).equals("Reading")) {
                            chk_reading.setChecked(true);
                        }
                        if (hobbiesData.getString(1).equals("Writing")) {
                            chk_writing.setChecked(true);
                        }
                    }
                }while (userDataCursor.moveToNext());
            }
            readableDatabase.close();
        } else {
            btnAddUser.setText("Add User");
        }


        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Firstname = firstNameInputEditText.getText().toString().trim();
                String Lastname = lastNameInputEditText.getText().toString().trim();
                String Username = userNameInputEditText.getText().toString().trim();
                String Email = emailInputEditText.getText().toString().trim();
                String Mono = phoneInputEditText.getText().toString().trim();
                String bDate = date_picker_InputEditText.getText().toString();
                Integer gender_id = 1;

                if (Firstname.isEmpty() || Lastname.isEmpty() || Username.isEmpty() || Email.isEmpty() || Mono.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill all details!", Toast.LENGTH_SHORT).show();
                } /* else if (!maleRadioButton.isSelected() && !femaleRadioButton.isSelected()) {
                    Messagee.message(getApplicationContext(), "Please select Gender");
                } */ else {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    selectedRadioButton = findViewById(selectedId);

                    if (selectedId == R.id.radio_male) {
                        gender_id = 1;
                    } else {
                        gender_id = 2;
                    }


                    if (action.equals("AddUser")){
                        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                        SQLiteDatabase database = helper.getWritableDatabase();

                    /*
CREATE TABLE AllUsers(_id INTEGER PRIMARY KEY AUTOINCREMENT,FNAME VARCHAR(50),LNAME VARCHAR(50),UNAME VARCHAR(50),EMAIL VARCHAR(255),PHONE VARCHAR(255),GENDER_id INTEGER,BDATE DATETIME, FOREIGN KEY (GENDER_id) REFERENCES gender(_id))";
create table hobbies(user_id INTEGER, hobbie VARCHAR(50), FOREIGN KEY (user_id) REFERENCES AllUsers(_id))";
                     */
                        ContentValues values = new ContentValues();
                        values.put("FNAME", Firstname);
                        values.put("LNAME", Lastname);
                        values.put("UNAME", Username);
                        values.put("EMAIL", Email);
                        values.put("PHONE", Mono);
                        values.put("GENDER_id", gender_id);
                        values.put("BDATE", bDate);

                        long user_id = database.insert("AllUsers", null, values);

                        user_id = Integer.parseInt(String.valueOf(user_id));

                        ArrayList<String> hobbies = new ArrayList<>();

                        if (chk_cricket.isChecked()) {
                            hobbies.add("Cricket");
                        }
                        if (chk_movies.isChecked()) {
                            hobbies.add("Movies");
                        }
                        if (chk_reading.isChecked()) {
                            hobbies.add("Reading");
                        }
                        if (chk_writing.isChecked()) {
                            hobbies.add("Writing");
                        }

                        ContentValues valuesHobbies = new ContentValues();

                        for (String hobby : hobbies) {
                            valuesHobbies.put("user_id", user_id);
                            valuesHobbies.put("hobbie", hobby);
                            database.insert("hobbies", null, valuesHobbies);
                            valuesHobbies.clear();
                        }

                        database.close();

                        Toast.makeText(getApplicationContext(), "User created!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                        SQLiteDatabase database = helper.getWritableDatabase();

                    /*
CREATE TABLE AllUsers(_id INTEGER PRIMARY KEY AUTOINCREMENT,FNAME VARCHAR(50),LNAME VARCHAR(50),UNAME VARCHAR(50),EMAIL VARCHAR(255),PHONE VARCHAR(255),GENDER_id INTEGER,BDATE DATETIME, FOREIGN KEY (GENDER_id) REFERENCES gender(_id))";
create table hobbies(user_id INTEGER, hobbie VARCHAR(50), FOREIGN KEY (user_id) REFERENCES AllUsers(_id))";
                     */
                        ContentValues values = new ContentValues();
                        values.put("FNAME", Firstname);
                        values.put("LNAME", Lastname);
                        values.put("UNAME", Username);
                        values.put("EMAIL", Email);
                        values.put("PHONE", Mono);
                        values.put("GENDER_id", gender_id);
                        values.put("BDATE", bDate);

                        database.update("AllUsers", values, "_id=?", new String[]{userId});

                        ArrayList<String> hobbies = new ArrayList<>();

                        if (chk_cricket.isChecked()) {
                            hobbies.add("Cricket");
                        }
                        if (chk_movies.isChecked()) {
                            hobbies.add("Movies");
                        }
                        if (chk_reading.isChecked()) {
                            hobbies.add("Reading");
                        }
                        if (chk_writing.isChecked()) {
                            hobbies.add("Writing");
                        }

                        ContentValues valuesHobbies = new ContentValues();

                        database.delete("hobbies","user_id=?",new String[]{userId});
                        for (String hobby : hobbies) {
                            valuesHobbies.put("user_id", userId);
                            valuesHobbies.put("hobbie", hobby);
                            database.insert("hobbies", null, valuesHobbies);
                            valuesHobbies.clear();
                        }

                        database.close();

                        Toast.makeText(getApplicationContext(), "User Updated!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                }
            }
        });

    }

    private void initialization() {
        firstNameInputLayout = findViewById(R.id.firstNameInputLayout);
        lastNameInputLayout = findViewById(R.id.lastNameInputLayout);
        userNameInputLayout = findViewById(R.id.userNameInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        phoneInputLayout = findViewById(R.id.phoneInputLayout);
        date_picker_InputLayout = findViewById(R.id.date_picker_InputLayout);


        firstNameInputEditText = findViewById(R.id.firstNameInputEditText);
        lastNameInputEditText = findViewById(R.id.lastNameInputEditText);
        userNameInputEditText = findViewById(R.id.userNameInputEditText);
        emailInputEditText = findViewById(R.id.emailInputEditText);
        phoneInputEditText = findViewById(R.id.phoneInputEditText);
        date_picker_InputEditText = findViewById(R.id.date_picker_InputEditText);

        radioGroup = findViewById(R.id.radioGroup);
        maleRadioButton = findViewById(R.id.radio_male);
        femaleRadioButton = findViewById(R.id.radio_female);

        chk_cricket = findViewById(R.id.chk_cricket);
        chk_movies = findViewById(R.id.chk_movies);
        chk_reading = findViewById(R.id.chk_reading);
        chk_writing = findViewById(R.id.chk_writing);

        btnAddUser = findViewById(R.id.btn_add_user);
    }

}