package com.example.emirhan.iingilizce_ogren;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.emirhan.iingilizce_ogren.abstraction.game.GameMode;

public class ModeSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_select);
        setTitle("Oyun Modu Seçiniz");
    }


    public void tur_eng(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("gameMode", GameMode.Tur_Eng);
        startActivityForResult(intent, 0);
    }

    public void eng_tur(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("gameMode", GameMode.Eng_Tur);
        startActivityForResult(intent, 0);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }
}
