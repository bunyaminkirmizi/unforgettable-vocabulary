package com.example.unforgottablevocabulary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText mEdit;
    private DataSRC md = new DataSRC(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//close screen orientation
        setContentView(R.layout.activity_main);
        mEdit = findViewById(R.id.editText);
        refreshList();
    }
    public void onVocabularyTestClicked(View view) {
        Intent intent = new Intent(this, FlashCardsActivity.class);
        startActivity(intent);
    }
    public void addNewWordButtonClick(View view) {
        String e = mEdit.getText().toString();
        if(e.equals("")){
            Toast.makeText(getApplicationContext(),"Kelime satırı boş olamaz",Toast.LENGTH_SHORT).show();
        }else{
            md = new DataSRC(this);
            md.open();

            Word w = md.getWordFromDBbySortedRankOrder(e);
            if(w == null){
                w = new Word(e);
                md.createWord(w);
                mEdit.setText("");
                Toast.makeText(getApplicationContext(),"Kelime '"+e+"' eklendi",Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getApplicationContext(),"Kelime '"+e+"' daha önce eklenmiş.",Toast.LENGTH_SHORT).show();
                mEdit.setText("");
            }
            refreshList();
            md.close();
        }
    }
    public void onDeleteButtonClick(final String value) {
        md.open();
        final Word word = md.getWordFromDBbySortedRankOrder(value);
        System.out.println(word);
        md.close();

        new AlertDialog.Builder(this)
                .setTitle("Deleting")
                .setMessage("Do you want to delete word '"+word.getWord()+"'?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        md.open();
                        md.deleteWord(word);
                        String toastText = "'" + word.getWord() + "' has been deleted";
                        Toast.makeText(getApplicationContext(),toastText, Toast.LENGTH_SHORT).show();
                        refreshList();
                        md.close();
                    }
                })
                .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
    private void refreshList(){
        final ListView wordsList;
        wordsList = findViewById(R.id.wordsList);
        final ArrayList<String> words = new ArrayList<>();
        final DataSRC md = new DataSRC(this);
        md.open();

        for (Word w : md.getWords()) {
            words.add(w.getWord());
        }

        final ArrayAdapter<String> wordsListAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                words
        );

        wordsList.setAdapter(wordsListAdapter);
        wordsList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, WordDescriptionActivity.class);
                        intent.putExtra("key", words.get(position));
                        startActivityForResult(intent,1);

                    }
                }
        );
        wordsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)  {
                onDeleteButtonClick(words.get(position));

                return true;
            }

        });
        md.close();
    }
}
