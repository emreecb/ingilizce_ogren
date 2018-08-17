package com.example.emirhan.iingilizce_ogren;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        TextView textView = (TextView)findViewById(R.id.pdTakmaAdText);
        textView.setText(firebaseUser.getDisplayName());
    }

    public void profilGuncelle (View view) {

        EditText editText = (EditText)findViewById(R.id.takmaAdGirText);

        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
            .setDisplayName(editText.getText().toString())
            .build();

        firebaseUser.updateProfile(userProfileChangeRequest)

            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful())
                    Log.e("Güncelleme Hatası", task.getException().getMessage());
                finish();

                }
            }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                finish();

                }
            });

    }

    public void sifreGuncelle (View view) {

        EditText editText = (EditText)findViewById(R.id.yeniSifreGirText);

        firebaseUser.updatePassword(editText.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            if (task.isSuccessful())
                Toast.makeText(getApplicationContext(),"Şifre Değiştirildi",Toast.LENGTH_LONG ).show();
            finish();

            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            finish();

            }
        });

    }

    public void cikis(View view) {

        FirebaseAuth.getInstance().signOut();
        setResult(RESULT_OK);
        finish();
    }




}
