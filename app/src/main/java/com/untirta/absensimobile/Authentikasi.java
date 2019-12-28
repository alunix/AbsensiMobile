package com.untirta.absensimobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class Authentikasi extends AppCompatActivity {

    TextInputEditText number,password;
    Button btnmasuk;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentikasi);

        number = findViewById(R.id.inputNim);
        password = findViewById(R.id.inputPassword);
        btnmasuk = findViewById(R.id.btnmasuk);
        progressBar = findViewById(R.id.loadingauth);

        btnmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomer = number.getText().toString();
                String pass = password.getText().toString();

                if (nomer.isEmpty() || pass.isEmpty()){
                    pesan("Lengkapi Indentitas Kamu");
                } else if (checkUser(nomer)){

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    pesan("Sedang mencocokan identitas kamu");
                    loginUser(nomer,pass);
                }
            }
        });
    }

    private void loginUser(String nomer, String pass) {
        firebaseAuth.signInWithEmailAndPassword(nomer,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    pesan("Selamat datang");
                    startActivity(new Intent(Authentikasi.this,Dashboard.class));
                    finish();
                }
            }
        });
    }

    private boolean checkUser(String s) {
        firebaseAuth.fetchSignInMethodsForEmail(s).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean check = task.getResult().getSignInMethods().isEmpty();

                if (check){
                    pesan("Identitas kamu tidak ditemukan");
                } else {

                }
            }
        });
        return true;
    }

    private void pesan(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
}
