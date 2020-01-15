package com.untirta.absensimobile.Authentikasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.untirta.absensimobile.Mahasiswa.DashboardMahasiswa;
import com.untirta.absensimobile.Model.InfoMahasiwa;
import com.untirta.absensimobile.R;

import java.util.ArrayList;
import java.util.List;

public class LoginMahasiswa extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    TextInputEditText mail, pass;
    Button btnmasuk;
    ProgressBar progressBar;
    InfoMahasiwa infoMahasiwa;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    List<String> listcheckMahasiswa;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mahasiswa_login, container, false);


        mail = view.findViewById(R.id.inputemailmahasiswa);
        pass = view.findViewById(R.id.inputPasswordmahasiswa);
        btnmasuk = view.findViewById(R.id.btnmasukmahasiswa);
        progressBar = view.findViewById(R.id.loadingauthmahasiswa);

        btnmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoMahasiwa = new InfoMahasiwa();
                infoMahasiwa.setEmail(mail.getText().toString());
                infoMahasiwa.setPassword(pass.getText().toString());
                if (infoMahasiwa.getEmail().isEmpty() || infoMahasiwa.getPassword().isEmpty()) {
                    pesan("Lengkapi Indentitas Anda");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(infoMahasiwa.getEmail()).matches()){
                    pesan("Alamat email anda tidak valid !");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    pesan("Sedang mencari identitas anda");
                    firebaseAuth.signInWithEmailAndPassword(infoMahasiwa.getEmail(),infoMahasiwa.getPassword())
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
        listcheckMahasiswa = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Mahasiswa");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listcheckMahasiswa.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    listcheckMahasiswa.add(snapshot.getKey());
                    for (String id : listcheckMahasiswa){
                        if (!firebaseAuth.getCurrentUser().getUid().equals(id)){
                            //pesan("Kamu Bukan Mahasiswa");
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Mahasiswa")
                .child(id).child("identitas");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                infoMahasiwa = dataSnapshot.getValue(InfoMahasiwa.class);

                UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(infoMahasiwa.getNama())
                        .build();
                firebaseAuth.getCurrentUser().updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pesan("Selamat Datang, " + infoMahasiwa.getNama());
                        Intent intent = new Intent(getActivity(), DashboardMahasiswa.class);
                        intent.putExtra("username", infoMahasiwa.getNama());
                        intent.putExtra("nim", infoMahasiwa.getNim());
                        intent.putExtra("password", infoMahasiwa.getPassword());
                        intent.putExtra("email", infoMahasiwa.getEmail());
                        intent.putExtra("jurusan", infoMahasiwa.getJurusan());
                        intent.putExtra("angkatan", infoMahasiwa.getAngkatan());
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
