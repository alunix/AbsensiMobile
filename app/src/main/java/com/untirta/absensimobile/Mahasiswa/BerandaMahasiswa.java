package com.untirta.absensimobile.Mahasiswa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;
import com.untirta.absensimobile.Adapter_Mahasiswa.AdapterBerandaMahasiswa;
import com.untirta.absensimobile.Model.InfoMahasiwa;
import com.untirta.absensimobile.Model.MataKuliah;
import com.untirta.absensimobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BerandaMahasiswa extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    RecyclerView recyclerView;
    List<MataKuliah> mataKuliahList;
    List<String> stringListMKMahasiswa;
    AdapterBerandaMahasiswa berandaMahasiswa;
    String nim;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beranda_mahasiswa,container,false);


        recyclerView = view.findViewById(R.id.recyclerberandamahasiswa);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        checkMataKuliah();

        checkEmailVerify();

        return view;
    }

    private void checkMataKuliah() {
        stringListMKMahasiswa = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Mahasiswa").child(firebaseAuth.getCurrentUser().getUid()).child("matakuliah");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stringListMKMahasiswa.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    stringListMKMahasiswa.add(snapshot.getKey());
                }

                bacaMataKuliahMahasiswa();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void bacaMataKuliahMahasiswa() {
        mataKuliahList = new ArrayList<>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Matakuliah");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mataKuliahList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    MataKuliah kuliah = snapshot.getValue(MataKuliah.class);
                    for (String mk : stringListMKMahasiswa){
                        if (kuliah.getMk().equals(mk)){
                            mataKuliahList.add(kuliah);
                        }
                    }

                }

                berandaMahasiswa = new AdapterBerandaMahasiswa(getContext(),mataKuliahList,nim);
                recyclerView.setAdapter(berandaMahasiswa);
                berandaMahasiswa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkEmailVerify() {
        if (firebaseAuth.getCurrentUser().isEmailVerified() == false){
            Alerter.create(getActivity())
                    .setTitle("Verifikasi E-mail").setTitleAppearance(R.style.CostumTitleAlert)
                    .setText("Periksa e-mail masuk kamu untuk mendapatkan link verifikasi").setTextAppearance(R.style.CostumTextAlert)
                    .enableSwipeToDismiss().setProgressColorRes(R.color.colormenu).enableProgress(true)
                    .setBackgroundColorRes(R.color.colorWhite).setDuration(8000)
                    .setIcon(getResources().getDrawable(R.drawable.icon_alert)).setIconColorFilter(getResources().getColor(R.color.colorText))
                    .showIcon(true)
                    .show();
        } else {
            Alerter.hide();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        String pertamakali = "SOME_KEY";
        Boolean first = getActivity().getPreferences(Context.MODE_PRIVATE).getBoolean(pertamakali,true);
        if (first){
            checkEmailVerify();
            getActivity().getPreferences(Context.MODE_PRIVATE).edit().putBoolean(pertamakali,false).apply();
        }
    }
}
