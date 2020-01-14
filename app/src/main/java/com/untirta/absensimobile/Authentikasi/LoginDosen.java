package com.untirta.absensimobile.Authentikasi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.untirta.absensimobile.Dosen.DashboardDosen;
import com.untirta.absensimobile.Mahasiswa.DashboardMahasiswa;
import com.untirta.absensimobile.Model.InfoDosen;
import com.untirta.absensimobile.Model.InfoMahasiwa;
import com.untirta.absensimobile.R;

import java.util.ArrayList;
import java.util.List;

public class LoginDosen extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    TextInputEditText mail, pass;
    Button btnmasuk;
    ProgressBar progressBar;
    InfoDosen infoDosen;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    List<String> uidDosen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dosen_login,container,false);

        mail = view.findViewById(R.id.inputemaildosen);
        pass = view.findViewById(R.id.inputPassworddosen);
        btnmasuk = view.findViewById(R.id.btnmasukdosen);
        progressBar = view.findViewById(R.id.loadingauthdosen);

        btnmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDosen = new InfoDosen();
                infoDosen.setEmail(mail.getText().toString());
                infoDosen.setPass(pass.getText().toString());
                if (infoDosen.getEmail().isEmpty() || infoDosen.getPass().isEmpty()) {
                    pesan("Lengkapi Indentitas Anda");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(infoDosen.getEmail()).matches()){
                    pesan("Alamat email anda tidak valid !");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    pesan("Sedang mencari identitas anda");
                    firebaseAuth.signInWithEmailAndPassword(infoDosen.getEmail(),infoDosen.getPass())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        if (firebaseAuth.getCurrentUser().isEmailVerified() == false){
                                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        progressBar.setVisibility(View.GONE);
                                                        pesan("Kamu harus memverifikasi alamat email terlebih dahulu");
                                                    }
                                                }
                                            });
                                        } else {
                                            checkDosen();
                                        }
                                    } else {
                                        pesan("kamu bukan dosen");
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            pesan("Password anda salah");
                        }
                    });
                }
            }
        });


        return view;
    }


    private void checkDosen(){
        uidDosen = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Dosen");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uidDosen.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    uidDosen.add(snapshot.getKey());
                    for (String id : uidDosen){
                        if (!firebaseAuth.getCurrentUser().getUid().equals(id)){
                            pesan("Kamu Bukan Dosen");
                            progressBar.setVisibility(View.GONE);
                        } else {
                            readDatabase(id);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readDatabase(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Dosen")
                .child(id).child("identitas");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                infoDosen = dataSnapshot.getValue(InfoDosen.class);

                UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(infoDosen.getNama())
                        .build();
                firebaseAuth.getCurrentUser().updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pesan("Selamat Datang, " + infoDosen.getNama());
                        Intent intent = new Intent(getActivity(), DashboardDosen.class);
                        intent.putExtra("nama", infoDosen.getNama());
                        intent.putExtra("password", infoDosen.getPass());
                        intent.putExtra("email", infoDosen.getEmail());
                        intent.putExtra("uid", firebaseAuth.getCurrentUser().getUid());
                        startActivity(intent);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (databaseError.equals(true)) {
                    progressBar.setVisibility(View.GONE);
                    pesan("Periksa Koneksi Internet Anda");
                }
            }
        });
    }


    private void pesan(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }
}
