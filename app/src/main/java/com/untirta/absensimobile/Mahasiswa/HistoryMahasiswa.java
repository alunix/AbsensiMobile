package com.untirta.absensimobile.Mahasiswa;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.untirta.absensimobile.Adapter_Mahasiswa.AdapterBerandaMahasiswa;
import com.untirta.absensimobile.Adapter_Mahasiswa.AdapterHistoryMahasiswa;
import com.untirta.absensimobile.Model.History;
import com.untirta.absensimobile.Model.MataKuliah;
import com.untirta.absensimobile.QRScaner.AbsenMahasiswa;
import com.untirta.absensimobile.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryMahasiswa extends Fragment {

    List<MataKuliah> mataKuliahList;
    List<String> stringListMKMahasiswa;
    ListMk listMk;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_mahasiswa,container,false);

        recyclerView = view.findViewById(R.id.recyclerhistorymahasiswa);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        checkMataKuliah();

        return view;
    }


    private void checkMataKuliah() {
        stringListMKMahasiswa = new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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

                listMk = new ListMk(getContext(),mataKuliahList);
                recyclerView.setAdapter(listMk);
                listMk.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class ListMk extends RecyclerView.Adapter<ListMk.Holder>{

        Context context;
        List<MataKuliah> mataKuliahList;

        public ListMk(Context context, List<MataKuliah> mataKuliahList) {
            this.context = context;
            this.mataKuliahList = mataKuliahList;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.model_matakuliah,parent,false);
            Holder holder = new Holder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

            holder.sks.setText(mataKuliahList.get(position).getSks());
            holder.mk.setText(mataKuliahList.get(position).getMk());
            holder.dosen.setText("Dosen : "+mataKuliahList.get(position).getDosen());
            holder.waktu.setText(mataKuliahList.get(position).getWaktu());
            holder.tempat.setText("Ruang Kelas : "+mataKuliahList.get(position).getKelas());

        }

        @Override
        public int getItemCount() {
            return mataKuliahList.size();
        }

        public class Holder extends RecyclerView.ViewHolder{

            TextView sks,mk,dosen,waktu,tempat;

            public Holder(@NonNull View itemView) {
                super(itemView);

                sks = itemView.findViewById(R.id.model_sksmk_mahasiswa);
                mk = itemView.findViewById(R.id.model_namaMK_mahasiswa);
                dosen = itemView.findViewById(R.id.model_dosen_mahasiswa);
                waktu = itemView.findViewById(R.id.model_jammk_mahasiswa);
                tempat = itemView.findViewById(R.id.model_kelasmk_mahasiswa);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int posisi = getAdapterPosition();
                        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putString("namamk",mataKuliahList.get(posisi).getMk());
                        DialogFragment fragment = new LihatHistory();
                        fragment.setArguments(bundle);
                        fragment.show(manager,"lihathistory");
                    }
                });

            }
        }

    }

}
