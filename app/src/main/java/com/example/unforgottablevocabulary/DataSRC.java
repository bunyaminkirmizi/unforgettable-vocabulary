package com.example.unforgottablevocabulary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataSRC {
    private SQLiteDatabase db;
    private ConnectSQL cdb;

    DataSRC(Context c) {
        cdb = new ConnectSQL(c);
    }

    void open() {
        db = cdb.getWritableDatabase();

    }
    void close() {
        cdb.close();
    }

    public int getWordCount() {
        String query = "SELECT COUNT(*)\n" +
                "FROM words;";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int a = c.getInt(0);
        c.close();
        return a;
    }
    public void userSetPoint(int point) {
        String query = "UPDATE user\n" +
                "SET point =" + point + ";\n" +
                "\n";
        db.execSQL(query);
    }
    public Integer userGetPoint() {
        String query = "SELECT point\n" +
                "FROM user;";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (c.getString(0) == null | c.getString(0).equals("null")) {
            return null;
        }
        int a = c.getInt(0);
        c.close();
        return a;
    }
    public int getHardWordsCount() {
        String query = "SELECT COUNT(*)\n" +
                "FROM words\n" +
                "WHERE rank<-4;";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int a = c.getInt(0);
        c.close();
        return a;
    }
    public int getKnownWordsCount() {
        String query = "SELECT COUNT(*)\n" +
                "FROM words\n" +
                "WHERE rank>4;";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int a = c.getInt(0);
        c.close();
        return a;
    }

    void createWord(Word word) {
        ContentValues val = new ContentValues();
        val.put("word", word.getWord());
        val.put("pronunciation", word.getPronunciation());
        val.put("rank", word.getRank());

        String[] wordcolumns = {
                "wordID",
                "word",
                "pronunciation",
                "rank"};
        db.insert("words", null, val);

        String where = "word = '" + word.getWord() + "'";
        Cursor c = db.query("words", wordcolumns, where, null, null, null, null);
        c.moveToFirst();
        word.setWordID(c.getInt(0));
        c.close();
    }
    void updateRank(Word word) {
        db.execSQL("UPDATE words\n" +
                "SET \"rank\"=" + word.getRank() + '\n' +
                "WHERE word = \"" + word.getWord() + "\";");
    }

    boolean deleteWord(Word word) {
        return db.delete("words", "wordID" + "=" + word.getWordID(), null) > 0;

    }

    List<Word> getWords() {
        String[] wordcolumns = {
                "wordID",
                "word",
                "pronunciation",
                "rank"};

        String[] defcolumns = {
                "wordID",
                "deftype",
                "definition",
                "defimgurl",
                "defexample"
        };

        List<Word> wordslist = new ArrayList<>();

        Cursor c = db.query("words", wordcolumns, null, null, null, null, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            int id = c.getInt(0);
            String word = c.getString(1);
            String pronunciation = c.getString(2);
            int rank = c.getInt(3);

            String where = "wordID=".concat(String.valueOf(id));
            String query = "SELECT * FROM definitions WHERE " + where + ";";

            System.out.println(query);
            Cursor d = db.query("definitions", defcolumns, where, null, null, null, null);

            d.moveToFirst();
            while (!d.isAfterLast()) {
                System.out.println("testisworked");
                System.out.println("QUERY ID: " + d.getInt(0));
                String type = d.getString(1);
                String definition = d.getString(2);
                String defimgurl = d.getString(3);
                String defexample = d.getString(4);
                d.moveToNext();
            }
            d.close();
            c.moveToNext();
            wordslist.add(new Word(word, pronunciation, rank, id));

        }
        c.close();
        Collections.sort(wordslist, new Comparator<Word>() {
            @Override
            public int compare(Word a1, Word a2) {
                return a1.getRank() - a2.getRank();
            }
        });

        return wordslist;
    }
    Word getWordFromDBbySortedRankOrder(String word) {
        String[] wordcolumns = {
                "wordID",
                "word",
                "pronunciation",
                "rank"};

        String[] defcolumns = {
                "defID",
                "deftype",
                "definition",
                "defimgurl",
                "defexample"
        };

        Word wordslist = null;

        Cursor c = db.query("words", wordcolumns, "word = '" + word + "'", null, null, null, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            int id = c.getInt(0);
            String pronunciation = c.getString(2);
            int rank = c.getInt(3);

            String where = "wordID=".concat(String.valueOf(id));
            String query = "SELECT * FROM definitions WHERE " + where + ";";

            System.out.println(query);
            Cursor d = db.query("definitions", defcolumns, where, null, null, null, null);

            d.moveToFirst();
            while (!d.isAfterLast()) {
                System.out.println("testisworked");
                System.out.println("QUERY ID: " + d.getInt(0));
                d.moveToNext();
            }
            d.close();
            c.moveToNext();
            wordslist = new Word(word, pronunciation, rank, id);

        }
        c.close();
        return wordslist;
    }
}
