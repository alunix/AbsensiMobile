package com.untirta.absensimobile.Dosen;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.untirta.absensimobile.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryAbsenDosen extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    RecyclerView recyclerView;
    TextView textView;
    String namamahasiswa, nimmahasiswa, namamk;
    List<History> historyList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_absen_dosen,container,false);


        namamahasiswa = getArguments().getString("namamahasiswa");
        nimmahasiswa = getArguments().getString("nimmahasiswa");
        namamk = getArguments().getString("namamk");

        textView = view.findViewById(R.id.history_absen_namamahasiswa);
        textView.setText(String.valueOf(namamahasiswa));

        recyclerView = view.findViewById(R.id.history_absen_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        bacadata();

        return view;
    }

    private void bacadata() {
        historyList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Absensi").child(namamk).child(nimmahasiswa);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    History history = snapshot.getValue(History.class);
                    historyList.add(history);
                }

                AdapterHistory adapterHistoryMahasiswa = new AdapterHistory(getContext(),historyList);
                recyclerView.setAdapter(adapterHistoryMahasiswa);
                adapterHistoryMahasiswa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.Holder> {

        Context context;
        List<History> histories;

        public AdapterHistory(Context context, List<History> histories) {
            this.context = context;
            this.histories = histories;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.model_historyabsen,parent,false);
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

        public class Holder extends RecyclerView.ViewHolder{

            TextView nama,nim,status,time;
            ImageView edit;

            public Holder(@NonNull View itemView) {
                super(itemView);

                edit = itemView.findViewById(R.id.model_history_edit);
                time = itemView.findViewById(R.id.model_history_tanggal);
                nama = itemView.findViewById(R.id.model_history_namamahasiswa);
                nim = itemView.findViewById(R.id.model_history_nimmahasiswa);
                status = itemView.findViewById(R.id.model_history_keteranganmasuk);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int posisi = getAdapterPosition();
                        Bundle arg = new Bundle();
                        arg.putString("nim", histories.get(posisi).getNim());
                        arg.putString("time", histories.get(posisi).getTime());
                        arg.putString("status",histories.get(posisi).getStatus());
                        arg.putString("nama",histories.get(posisi).getNama());
                        arg.putString("mk",namamk);
                        DialogFragment fragment = new EditStatus();
                        fragment.setArguments(arg);
                        fragment.show(getFragmentManager(),"openedit");

                    }
                });

            }
        }

    }
}
