package com.untirta.absensimobile.Dosen;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.untirta.absensimobile.Adapter_Mahasiswa.AdapterHistoryMahasiswa;
import com.untirta.absensimobile.Model.History;
import com.untirta.absensimobile.Model.InfoMahasiwa;
import com.untirta.absensimobile.R;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AbsenDosen extends DialogFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }


    RecyclerView recyclerView;
    List<InfoMahasiwa> infoMahasiwaList;
    String namamk, uidm;
    List<String> absennim, uidmahasiswa;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.absensi_dosen, container, false);

        Bundle srg = getArguments();
        namamk = srg.getString("namamk");

        recyclerView = view.findViewById(R.id.recyclerhistorydosen);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        absenNim();
        return view;
    }


    private void absenNim() {
        absennim = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Absensi").child(namamk);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                absennim.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    absennim.add(snapshot.getKey());

                }

                ambilUIDMahasiswa();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void ambilUIDMahasiswa() {
        uidmahasiswa = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Mahasiswa");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uidmahasiswa.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    uidmahasiswa.add(snapshot.getKey());
                    for (String id : uidmahasiswa) {
                        uidm = id;
                    }
                    bacaIdentitasMahasiwa(uidm);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void bacaIdentitasMahasiwa(String uidm) {
        infoMahasiwaList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Mahasiswa")
                .child(String.valueOf(uidm)).child("identitas");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                InfoMahasiwa infoMahasiwa = dataSnapshot.getValue(InfoMahasiwa.class);
                infoMahasiwaList.add(infoMahasiwa);

                AdapterHistoryDosen adapterHistoryDosen = new AdapterHistoryDosen(getContext(), infoMahasiwaList);
                recyclerView.setAdapter(adapterHistoryDosen);
                adapterHistoryDosen.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class AdapterHistoryDosen extends RecyclerView.Adapter<AdapterHistoryDosen.Holder> {

        Context context;
        List<InfoMahasiwa> infoMahasiwaList;

        public AdapterHistoryDosen(Context context, List<InfoMahasiwa> infoMahasiwaList) {
            this.context = context;
            this.infoMahasiwaList = infoMahasiwaList;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.model_infomahasiswa, parent, false);
            Holder holder = new Holder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

            holder.nama.setText(infoMahasiwaList.get(position).getNama());
            holder.nim.setText(infoMahasiwaList.get(position).getNim());
            holder.angkatan.setText("Angkatan : " + infoMahasiwaList.get(position).getAngkatan());

        }

        @Override
        public int getItemCount() {
            return infoMahasiwaList.size();
        }

        public class Holder extends RecyclerView.ViewHolder {

            TextView nama, nim, angkatan;

            public Holder(@NonNull View itemView) {
                super(itemView);

                nama = itemView.findViewById(R.id.model_info_namamahasiswa);
                nim = itemView.findViewById(R.id.model_info_nimmahasiswa);
                angkatan = itemView.findViewById(R.id.model_info_angkatanmahasiswa);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int posisi = getAdapterPosition();
                        Bundle arg = new Bundle();
                        arg.putString("namamahasiswa",infoMahasiwaList.get(posisi).getNama());
                        arg.putString("nimmahasiswa",infoMahasiwaList.get(posisi).getNim());
                        arg.putString("namamk",namamk);
                        DialogFragment fragment = new HistoryAbsenDosen();
                        fragment.setArguments(arg);
                        fragment.show(getFragmentManager(),"listabsendosen");
                    }
                });

            }
        }

    }
}
