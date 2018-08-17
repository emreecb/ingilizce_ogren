package com.example.emirhan.iingilizce_ogren;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText emailText;
    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        emailText    = (EditText) findViewById(R.id.logEmailText);
        passwordText = (EditText) findViewById(R.id.logPasswordText);
    }

    public void login (View view){

        if(emailText.getText().toString().equals("") || passwordText.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Email veya Şifre Alanı Boş Bırakılamaz.", Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.putExtra("islem", "login");
            intent.putExtra("email", emailText.getText().toString());
            intent.putExtra("password", passwordText.getText().toString());
            startActivityForResult(intent, 0);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {

            if (resultCode == RESULT_OK)
                finish();
        }
    }
}
