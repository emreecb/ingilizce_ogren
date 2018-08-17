package com.example.emirhan.iingilizce_ogren;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.emirhan.iingilizce_ogren.abstraction.data.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    Button buttonDevam;
    Button buttonAnonim;
    Button buttonKayit;
    Button buttonOturum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ConstraintLayout view = (ConstraintLayout)findViewById(R.id.view);

        buttonAnonim = findViewById(R.id.buttonAnonim);
        buttonDevam  = findViewById(R.id.buttonDevam);
        buttonOturum = findViewById(R.id.buttonOturum);
        buttonKayit  = findViewById(R.id.buttonKaydol);

        Random random = new Random();
        int number = random.nextInt(7) + 1;

        switch (number){
            case 1:
                view.setBackgroundResource(R.drawable.london1);
                break;
            case 2:
                view.setBackgroundResource(R.drawable.london2);
                break;
            case 3:
                view.setBackgroundResource(R.drawable.london3);
                break;
            case 4:
                view.setBackgroundResource(R.drawable.london4);
                break;
            case 5:
                view.setBackgroundResource(R.drawable.london5);
                break;
            case 6:
                view.setBackgroundResource(R.drawable.london6);
                break;
            case 7:
                view.setBackgroundResource(R.drawable.london7);
                break;
            default:
                view.setBackgroundResource(R.drawable.london1);
                Toast.makeText(getApplicationContext(),"Hata", Toast.LENGTH_LONG).show();
        }

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {

                    //buttonAnonim.setVisibility(View.VISIBLE);
                    buttonKayit.setVisibility(View.VISIBLE);
                    buttonOturum.setVisibility(View.VISIBLE);
                    buttonDevam.setVisibility(View.INVISIBLE);

                } else {

                    //buttonAnonim.setVisibility(View.INVISIBLE);
                    buttonKayit.setVisibility(View.INVISIBLE);
                    buttonOturum.setVisibility(View.INVISIBLE);
                    buttonDevam.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void anonim(View view) {

        FirebaseService service = new FirebaseService();
        service.loginAnonymous();

        Intent intent = new Intent(getApplicationContext(), ModeSelectActivity.class);
        startActivity(intent);
    }
    public void kayit (View view)
    {
        Intent intent = new Intent (getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void giris (View view)
    {
        Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void devam(View view) {

        Intent intent = new Intent (getApplicationContext(), ModeSelectActivity.class);
        startActivity(intent);
    }
}
