package com.example.emirhan.iingilizce_ogren;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emirhan.iingilizce_ogren.abstraction.data.FirebaseService;
import com.example.emirhan.iingilizce_ogren.abstraction.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textView;
    private int progressDurum= 0;
    private Handler handler = new Handler();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;

    private FirebaseService firebaseService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        firebaseService = new FirebaseService();

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        textView = (TextView)findViewById(R.id.textView9);

        Intent intent = getIntent();
        final String islem = intent.getStringExtra("islem");
        final String emailText = intent.getStringExtra("email");
        final String passwordText = intent.getStringExtra("password");

        progressDurum = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressDurum<100){
                    progressDurum +=1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressDurum);
                            textView.setText(
                                islem.equals("login") ?
                                "Giriş yapılıyor..." :
                                "Kayıt işlemi yapılıyor..."
                            );

                        }
                    });
                    try{
                        Thread.sleep(19);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        if (islem.equals("login")) {
            Task<AuthResult> asyncTask = firebaseService.loginUser(emailText,passwordText);

            asyncTask.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), ModeSelectActivity.class);
                        startActivity(intent);

                        setResult(RESULT_OK);
                        finish();
                    }
                }
            });

            asyncTask.addOnFailureListener(this, new OnFailureListener() {
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(
                        getApplicationContext(),
                        "Giriş başarısız oldu. Lütfen tekrar deneyiniz.",
                        Toast.LENGTH_LONG
                    ).show();

                    setResult(RESULT_CANCELED);
                    finish();
                }
            });

        } else {

            Task<AuthResult> asyncTask = firebaseService.registerUser(emailText, passwordText);

            asyncTask.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    int number = 1000 + new Random().nextInt(8999);
                    String newName = "Kullanici" + Integer.toString(number);

                    // @TODO firebaseService updateUser su anda sadece username guncelledigi icin
                    // diger alanlar o kadar onemli degil bu metoda yollanan UserModel'de
                    UserModel user = new UserModel("", newName, "");

                    Task updateTask = firebaseService.updateUser(user);

                    updateTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Log.e("Güncelleme Hatası", task.getException().getMessage());
                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(), ModeSelectActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

                    setResult(RESULT_OK);
                    finish();
                }
                }
            });

            asyncTask.addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Kayıt işlemi başarısız oldu. Lütfen bilgilerinizi kontrol edip tekrar deneyiniz." + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();

                    firebaseService.clearUser();
                    Log.d("Datasdsd", "Register failed: " + e.getLocalizedMessage());
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
        }

    }
}
