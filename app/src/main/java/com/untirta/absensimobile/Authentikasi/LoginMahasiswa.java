package com.untirta.absensimobile.Authentikasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.untirta.absensimobile.Model.InfoMahasiwa;
import com.untirta.absensimobile.R;

public class LoginMahasiswa extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    TextInputEditText number, pass;
    Button btnmasuk;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mahasiswa_login, container, false);

        number = view.findViewById(R.id.inputNim);
        pass = view.findViewById(R.id.inputPassword);
        btnmasuk = view.findViewById(R.id.btnmasuk);
        progressBar = view.findViewById(R.id.loadingauth);

        btnmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nim = number.getText().toString();
                final String password = pass.getText().toString();
                if (nim.isEmpty() || password.isEmpty()) {
                    pesan("Lengkapi Indentitas Kamu");
                } else {
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Mahasiswa");
                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                InfoMahasiwa infoMahasiwa = snapshot.getValue(InfoMahasiwa.class);
                                if (!nim.equals(String.valueOf(infoMahasiwa.getNim()))) {
                                    pesan("email salah");
                                } else if (!password.equals(String.valueOf(infoMahasiwa.getPassword()))) {
                                    pesan("passkamu salah");
                                } else {
                                    pesan("kamu berhasil");
                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


        return view;
    }


    private void pesan(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }
}
