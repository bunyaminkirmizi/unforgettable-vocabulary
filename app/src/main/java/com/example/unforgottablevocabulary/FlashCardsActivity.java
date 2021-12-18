package com.example.unforgottablevocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.Queue;

public class FlashCardsActivity extends AppCompatActivity {

    final DataSRC md = new DataSRC(this);
    private Queue<Word> words = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_cards);

        md.open();
        words = new LinkedList<>(md.getWords());
        md.close();
        refreshOrEndOfActivity("Word list is empty");
    }

    public void yesButtonClick(View view) {

        getNextWord().rankUp();
        md.open();
        md.updateRank(getNextWord());
        md.close();
        removeFromQuiz();

        refreshOrEndOfActivity("End of the list");
    }

    private void refreshOrEndOfActivity(String endOfActivityMessage) {
        try {
            TextView t = findViewById(R.id.word);
            t.setText(getNextWord().getWord());
            t = findViewById(R.id.cdt);
            t.setText(getRemaining());
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), endOfActivityMessage, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void noButtonClick(View view) {
        Intent intent = new Intent(FlashCardsActivity.this, WordDescriptionActivity.class);
        intent.putExtra("key", getNextWord().getWord());
        startActivityForResult(intent,1);
        getNextWord().rankDown();
        md.open();
        md.updateRank(getNextWord());
        md.close();
        sendToEnd();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshOrEndOfActivity("End of the list");
    }

    public void removeFromQuiz() {
        words.remove();
    }

    public void sendToEnd() {
        words.add(words.remove());
    }

    public String getRemaining() {
        int size = words.size();
        return Integer.toString(size);
    }

    private boolean isTheQuizOver() {
        return words.isEmpty();
    }

    Word getNextWord() {
        if (isTheQuizOver()) {
            return null;
        }
        return words.peek();
    }
}
