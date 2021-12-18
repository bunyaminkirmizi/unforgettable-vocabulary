package com.example.unforgottablevocabulary;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

public class WordDescriptionActivity extends AppCompatActivity {

    private Word word;
    final DataSRC md = new DataSRC(this);
    private WebView youglish;
    private Button youglish_button;
    private Button tureng_button;
    private Button cambridge_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_description);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        md.open();
        word = md.getWordFromDBbySortedRankOrder(value);
        md.close();
        TextView rank = findViewById(R.id.rank_text);
        rank.setText(String.valueOf(word.getRank()));
        if(word.getRank()<0){
            rank.setTextColor(Color.RED);
        }
        youglish_button = findViewById(R.id.youglish_button);
        tureng_button = findViewById(R.id.tureng_button);
        cambridge_button = findViewById(R.id.cambrige_button);
        youglish = findViewById(R.id.webview_youglish);
        WebSettings webSettings = youglish.getSettings();
        webSettings.setJavaScriptEnabled(true);
        youglish.setWebViewClient(new Callback());
        youglish.loadUrl("https://dictionary.cambridge.org/dictionary/english/"+ word.getWord());
        setButtonColors(false,false,true);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void onYouglishButtonClick(View view) {
        youglish.loadUrl("https://youglish.com/pronounce/"+word.getWord()+"/english?");
        setButtonColors(true,false,false);
    }
    public void setButtonColors(Boolean x,Boolean y,Boolean z){
        if(x){
            youglish_button.setBackgroundResource(R.drawable.btn_rounded_video_pressed);

        }else{
            youglish_button.setBackgroundResource(R.drawable.btn_rounded_video);
        }
        if(y){
            tureng_button.setBackgroundResource(R.drawable.btn_rounded_description_pressed);

        }else{
            tureng_button.setBackgroundResource(R.drawable.btn_rounded_description);
        }
        if(z){
            cambridge_button.setBackgroundResource(R.drawable.btn_rounded_description_pressed);

        }else{
            cambridge_button.setBackgroundResource(R.drawable.btn_rounded_description);
        }
    }

    public void onCambrigeButtonClick(View view) {
        youglish.loadUrl("https://dictionary.cambridge.org/dictionary/english/"+ word.getWord());
        setButtonColors(false,false,true);

    }

    public void onTurengButtonClick(View view) {
        youglish.loadUrl("https://tureng.com/en/turkish-english/"+ word.getWord());
        setButtonColors(false,true,false);

    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view , KeyEvent event){
            return false;
        }
    }
}
