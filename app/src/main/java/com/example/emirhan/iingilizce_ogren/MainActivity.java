package com.example.emirhan.iingilizce_ogren;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.emirhan.iingilizce_ogren.abstraction.core.Chain;
import com.example.emirhan.iingilizce_ogren.abstraction.core.Ref;
import com.example.emirhan.iingilizce_ogren.abstraction.data.FirebaseService;
import com.example.emirhan.iingilizce_ogren.abstraction.game.GameEngine;
import com.example.emirhan.iingilizce_ogren.abstraction.game.GameMode;
import com.example.emirhan.iingilizce_ogren.abstraction.models.ScoreModel;
import com.example.emirhan.iingilizce_ogren.abstraction.models.UserModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.acilir_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.profil_duzenle){
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivityForResult(intent, 0);
        }

        if(item.getItemId() == R.id.hakkinda){
            Intent intent2 = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(intent2);
        }
        if(item.getItemId() == R.id.paylas){

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Ingilizce Öğren App: https://www.google.com");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            setResult(RESULT_CANCELED);
            finish();
        }

        UserModel currentUser = firebaseService.getAuthUser();

        if (currentUser != null)
            setTitle("Merhaba " + currentUser.username + "!" );
    }

    FirebaseService firebaseService;
    GameEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseService = new FirebaseService();

        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));


        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        init_gui();

        UserModel currentUser = firebaseService.getAuthUser();

        if (currentUser != null)
            setTitle("Merhaba " + currentUser.username + "!" );

        // oyun motorunu calistir ve ilk oyuna basla
        init_game();
    }

    TextView textKelimeSoru;
    TextView textDogruKelime;
    TextView textYanlis;
    TextView textYanlisSayi;
    TextView textDogru;
    TextView textDogruSayi;
    TextView textIstatistik;

    Button buttonYeniKelime;
    Button buttonCevabiGoster;

    EditText editKullaniciCevabi;

    private void init_gui() {

        textKelimeSoru   = (TextView)findViewById(R.id.kelimeSoruView);
        textDogruKelime  = (TextView)findViewById(R.id.dogruKelimeView);
        textYanlisSayi   = (TextView)findViewById(R.id.yanlisSayiView);
        textYanlis       = (TextView)findViewById(R.id.yanlisView);
        textDogruSayi    = (TextView)findViewById(R.id.dogruSayiView);
        textDogru        = (TextView)findViewById(R.id.dogruView);
        textIstatistik   = (TextView)findViewById(R.id.istatistikView);

        buttonYeniKelime   = (Button)findViewById(R.id.buttonYeniKelime);
        buttonCevabiGoster = (Button)findViewById(R.id.buttonCevabiGoster);

        editKullaniciCevabi = (EditText)findViewById(R.id.kullaniciCevabiText);
    }

    private void init_game() {

        Intent intent = getIntent();
        GameMode oyunModu = (GameMode)intent.getSerializableExtra("gameMode");

        engine = new GameEngine(oyunModu);
        next_game();
    }

    private void next_game() {

        hideStatisticsSection();
        clearUserInput();

        clearQuestion();
        disableCheckAnswer();

        Chain result = engine.NextGame();

        result.thenApply(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        enableCheckAnswer();
                        setWord(engine.GetCurrentWord());
                    }
                });
            }
        });
    }

    private void check_answer() {

        final String input  = editKullaniciCevabi.getText().toString();

        final Ref<String> outAnswer = new Ref<>("");
        final Ref<Boolean> outCorrect = new Ref<>(true);
        final Ref<ScoreModel> newScore = new Ref<>(new ScoreModel());

        buttonCevabiGoster.setEnabled(false);

        Chain result = engine.CheckAnswerUpdateScore(input, outCorrect, outAnswer, newScore);

        result.thenApply(new Runnable() {
            @Override
            public void run() {

                final String correctAnswer = outAnswer.get();

                final int color;

                if (outCorrect.get())
                    color = Color.GREEN;

                else
                    color = Color.RED;

                runUIMain(new Runnable() {
                    @Override
                    public void run() {

                        setAnswer(color, correctAnswer);
                        setAndShowStatistics(newScore.get());
                    }
                });
            }
        });

    }

    private void runUIMain(Runnable run) {

        Handler main = new Handler(getApplicationContext().getMainLooper());
        main.post(run);
    }

//---------------------------------------
// Arayuzden gelen kullanici bildirimleri (buton tiklama vs)
//---------------------------------------

    public void ShowAnswerOnClick(View view) {

        hideKeyboard();
        check_answer();
    }

    public void NextQuestionOnClick(View view) {

        showKeyboard();
        next_game();
    }

//--------------------------------
// Arayuze mudahale yapan metodlar
//--------------------------------

    private void hideKeyboard() {

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(
                (null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showKeyboard() {

        editKullaniciCevabi.requestFocus();

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.showSoftInput(
                editKullaniciCevabi,
                InputMethodManager.SHOW_IMPLICIT);
    }

    private void disableCheckAnswer() {

        buttonCevabiGoster.setEnabled(false);
    }

    private void enableCheckAnswer() {

        buttonCevabiGoster.setEnabled(true);
    }

    private void hideStatisticsSection() {

        textDogruKelime.setVisibility(View.INVISIBLE);
        textIstatistik.setVisibility(View.INVISIBLE);
        textYanlisSayi.setVisibility(View.INVISIBLE);
        textDogruSayi.setVisibility(View.INVISIBLE);
        textYanlis.setVisibility(View.INVISIBLE);
        textDogru.setVisibility(View.INVISIBLE);
        buttonYeniKelime.setVisibility(View.INVISIBLE);
        buttonCevabiGoster.setEnabled(true);
    }

    private void clearUserInput() {

        editKullaniciCevabi.setText("");
    }

    private void clearQuestion() {

        textKelimeSoru.setText("");
    }

    private void setWord(String word) {

        char c = Character.toUpperCase(word.charAt(0));
        word = c + word.substring(1);
        textKelimeSoru.setText(word);
    }

    private void setAnswer(int color, String word) {

        textDogruKelime.setTextColor(color);

        word = Character.toUpperCase(word.charAt(0)) + word.substring(1);
        textDogruKelime.setText(word);

    }

    private void setAndShowStatistics(ScoreModel statistics) {

        textDogruKelime.setVisibility(View.VISIBLE);
        textIstatistik.setVisibility(View.VISIBLE);
        textYanlisSayi.setVisibility(View.VISIBLE);
        textDogruSayi.setVisibility(View.VISIBLE);
        textYanlis.setVisibility(View.VISIBLE);
        textDogru.setVisibility(View.VISIBLE);
        buttonYeniKelime.setVisibility(View.VISIBLE);

        int dogruSayisi  = statistics.correctCount;
        int yanlisSayisi = statistics.incorrectCount;
        textDogruSayi.setText(Integer.toString(dogruSayisi));
        textYanlisSayi.setText(Integer.toString(yanlisSayisi));
    }
}
