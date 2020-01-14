package com.untirta.absensimobile.Mahasiswa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.untirta.absensimobile.Adapter_Mahasiswa.AdapterBerandaMahasiswa;
import com.untirta.absensimobile.Adapter_Mahasiswa.AdapterHistoryMahasiswa;
import com.untirta.absensimobile.Model.History;
import com.untirta.absensimobile.Model.MataKuliah;
import com.untirta.absensimobile.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileMahasiswa extends Fragment {

    RecyclerView recyclerView;
    List<String> stringListUidMahasiswa, stringListMKDosen;
    AdapterBerandaMahasiswa adapterBerandaMahasiswa;
    List<MataKuliah> mataKuliahList;
    String nim, uid;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_mahasiswa, container, false);


        recyclerView = view.findViewById(R.id.dosen_recyclerberanda);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);


        matakuliahDosen();


        return view;
    }

    private void ambilUID() {
        stringListUidMahasiswa = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mahasiswa");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stringListUidMahasiswa.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    stringListUidMahasiswa.add(snapshot.getKey());
                    for (String uid : stringListUidMahasiswa) {
                        checkMKMhasiswa(uid);

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void matakuliahDosen() {
        stringListMKDosen = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Dosen").child("matakuliah");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stringListMKDosen.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    stringListMKDosen.add(snapshot.getKey());

                }

                ambilUID();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkMKMhasiswa(String id) {
        mataKuliahList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Mahasiswa").child(id).child("matakuliah");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mataKuliahList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MataKuliah kuliah = snapshot.getValue(MataKuliah.class);
                    for (String lis : stringListMKDosen){
                        if (kuliah.getMk().equals(lis)){
                            mataKuliahList.add(kuliah);

                        }
                    }

                }

                adapterBerandaMahasiswa = new AdapterBerandaMahasiswa(getContext(),mataKuliahList,nim);
                recyclerView.setAdapter(adapterBerandaMahasiswa);
                adapterBerandaMahasiswa.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
