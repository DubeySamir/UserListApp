package com.sdcode.userslist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;
    private static final String myDb = "MyDb";
    private static final int version = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, myDb, null, version);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Create table gender
        String createTableGender = "CREATE TABLE gender (_id INTEGER PRIMARY KEY AUTOINCREMENT,gender varchar(20) NOT NULL)";

        //Insert values in gender table
        String insertMale = "insert into gender values(1, 'male')";
        String insertFeMale = "insert into gender values(2, 'female')";
        String insertOther = "insert into gender values(3, 'other')";

        //Create AllUsers and hobbies tables
        String createTableAllUsers = "CREATE TABLE AllUsers(_id INTEGER PRIMARY KEY AUTOINCREMENT,FNAME VARCHAR(50),LNAME VARCHAR(50),UNAME VARCHAR(50),EMAIL VARCHAR(255),PHONE VARCHAR(255),GENDER_id INTEGER,BDATE DATETIME, FOREIGN KEY (GENDER_id) REFERENCES gender(_id))";
        String createTableHobbies = "create table hobbies(user_id INTEGER, hobbie VARCHAR(50), FOREIGN KEY (user_id) REFERENCES AllUsers(_id))";

        db.execSQL(createTableGender);
        db.execSQL(insertMale);
        db.execSQL(insertFeMale);
        db.execSQL(insertOther);
        db.execSQL(createTableAllUsers);
        db.execSQL(createTableHobbies);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public ArrayList<RVUser> getAllUsersData() {

        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<RVUser> objectModelClassList = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from AllUsers", null);

        if (cursor.getCount() != 0) {
            cursor.moveToPosition(-1);

            while (cursor.moveToNext()) {
                Integer userId = cursor.getInt(0);
                Integer genderId = cursor.getInt(6);
                String userName = cursor.getString(3);
                String userEmail = cursor.getString(4);

                objectModelClassList.add(new RVUser(userId,genderId, userName, userEmail));
            }
            return objectModelClassList;
        } else {
            Messagee.message(context, "No values in database");
            return null;
        }
    }
}
