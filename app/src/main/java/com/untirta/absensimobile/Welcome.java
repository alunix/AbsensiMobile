package com.untirta.absensimobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.untirta.absensimobile.Authentikasi.LoginDosen;
import com.untirta.absensimobile.Authentikasi.LoginMahasiswa;

public class Welcome extends AppCompatActivity {

    Button btnmahasiswa,btndosen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        btnmahasiswa = findViewById(R.id.masukmahasiswa);
        btndosen = findViewById(R.id.masukdosen);

        btndosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcome.this, LoginDosen.class));
                finish();
            }
        });

        btnmahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment  = new LoginMahasiswa();
                fragment.show(getSupportFragmentManager(),"LoginMahasiswa");
            }
        });


    }
}
