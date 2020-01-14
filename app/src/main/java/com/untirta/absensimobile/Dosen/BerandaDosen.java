package com.untirta.absensimobile.Dosen;

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
import com.untirta.absensimobile.Model.MataKuliah;
import com.untirta.absensimobile.QRScaner.AbsenMahasiswa;
import com.untirta.absensimobile.R;

import java.util.ArrayList;
import java.util.List;

public class BerandaDosen extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    RecyclerView recyclerView;
    List<MataKuliah> mataKuliahList;
    List<String> stringListMKdosen;
    AdapterDosen adapterDosen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beranda_dosen,container,false);

        recyclerView = view.findViewById(R.id.recyclerberandadosen);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        checkMKdosen();


        return view;
    }

    private void checkMKdosen() {
        stringListMKdosen = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Dosen").child(firebaseAuth.getCurrentUser().getUid()).child("matakuliah");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stringListMKdosen.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    stringListMKdosen.add(snapshot.getKey());
                    bacaMK();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void bacaMK() {
        mataKuliahList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Matakuliah");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mataKuliahList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    MataKuliah mataKuliah = snapshot.getValue(MataKuliah.class);
                    for (String mk : stringListMKdosen){
                        if (mataKuliah.getMk().equals(mk)){
                            mataKuliahList.add(mataKuliah);
                        }
                    }
                }

                adapterDosen = new AdapterDosen(mataKuliahList,getContext());
                recyclerView.setAdapter(adapterDosen);
                adapterDosen.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class AdapterDosen extends RecyclerView.Adapter<AdapterDosen.Holder>{

        List<MataKuliah> list;
        Context context;

        public AdapterDosen(List<MataKuliah> mataKuliahList, Context context) {
            this.list = mataKuliahList;
            this.context = context;
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

            holder.sks.setText(list.get(position).getSks());
            holder.mk.setText(list.get(position).getMk());
            holder.dosen.setText("Dosen : "+list.get(position).getDosen());
            holder.waktu.setText(list.get(position).getWaktu());
            holder.tempat.setText("Ruang Kelas : "+list.get(position).getKelas());

        }

        @Override
        public int getItemCount() {
            return list.size();
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
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        if (auth.getCurrentUser().isEmailVerified() == false){
                            Toast.makeText(context,"Verifikasi email kamu terlebih dahulu",Toast.LENGTH_LONG).show();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("namamk",mataKuliahList.get(posisi).getMk());
                            DialogFragment fragment = new AbsenDosen();
                            fragment.setArguments(bundle);
                            fragment.show(manager,"dialogabsendosen");
                        }
                    }
                });

            }
        }
    }
}
