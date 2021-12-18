package com.example.unforgottablevocabulary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConnectSQL extends SQLiteOpenHelper {

    ConnectSQL(Context c) {
        super(c, "sqlitedb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createWordsTable =
                "CREATE TABLE IF NOT EXISTS words (\n" +
                "\twordID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\tword text UNIQUE NOT NULL,\n" +
                "\tpronunciation TEXT,\n" +
                "\trank INTEGER DEFAULT(0)\n" +
                ");\n";

        String createDefinitionsTable =
                "CREATE TABLE IF NOT EXISTS definitions(\n" +
                " \tdefID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                " \tdeftype TEXT,\n" +
                "\tdefinition TEXT not NULL,\n" +
                "\tdefimgurl TEXT,\n" +
                "\tdefexample TEXT,\n" +
                " \twordID INTEGER NOT NULL,\n" +
                " \tFOREIGN KEY(wordID) REFERENCES word(wordID)\n" +
                ");";

        String createUserTable =
                "CREATE TABLE IF NOT EXISTS user(\n" +
                " \tpoint INTEGER DEFAULT(0)\n" +
                ");";
        String insertUserDefaults = "INSERT INTO user(point)\n" +
                "VALUES(0);";

        db.execSQL(createWordsTable);
        db.execSQL(createDefinitionsTable);
        db.execSQL(createUserTable);
        db.execSQL(insertUserDefaults);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
