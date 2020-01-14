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
    List<History> historyest;
    String namamk, NIM, TGL;
    List<String> absennim, absentgl;

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
                    for (String id : absennim) {
                        NIM = id;
                        bacatanggal();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void bacatanggal() {
        absentgl = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Absensi").child(namamk).child(NIM);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                absentgl.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    absentgl.add(snapshot.getKey());
                    for (String id : absentgl) {
                        TGL = id;
                        bacaAbsen();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void bacaAbsen() {
        historyest = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Absensi")
                .child(namamk).child(String.valueOf(NIM)).child(String.valueOf(TGL));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyest.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   History history = snapshot.getValue(History.class);
                   historyest.add(history);
                    for (int i = 0; i < historyest.size(); i++) {
                        System.out.println(historyest.get(i).getNim());
                    }
                }


               // AdapterHistoryDosen adapterHistoryDosen = new AdapterHistoryDosen(getContext(), historyest);
                //recyclerView.setAdapter(adapterHistoryDosen);
               // adapterHistoryDosen.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public class AdapterHistoryDosen extends RecyclerView.Adapter<AdapterHistoryDosen.Holder> {

        Context context;
        List<History> histories;

        public AdapterHistoryDosen(Context context, List<History> histories) {
            this.context = context;
            this.histories = histories;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.model_historyabsen, parent, false);
            Holder holder = new Holder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

            holder.nama.setText(histories.get(position).getNama());
            holder.nim.setText(histories.get(position).getNim());
            holder.status.setText(histories.get(position).getStatus());
            holder.time.setText(histories.get(position).getTime());

        }

        @Override
        public int getItemCount() {
            return histories.size();
        }

        public class Holder extends RecyclerView.ViewHolder {

            TextView nama, nim, status, mk, time;

            public Holder(@NonNull View itemView) {
                super(itemView);

                time = itemView.findViewById(R.id.model_history_tanggal);
                nama = itemView.findViewById(R.id.model_history_namamahasiswa);
                nim = itemView.findViewById(R.id.model_history_nimmahasiswa);
                status = itemView.findViewById(R.id.model_history_keteranganmasuk);

            }
        }

    }
}
