package com.untirta.absensimobile.Mahasiswa;

import android.os.Bundle;
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
import com.untirta.absensimobile.R;

import java.util.ArrayList;
import java.util.List;

public class LihatHistory extends DialogFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    AdapterHistoryMahasiswa historyMahasiswa;
    List<History> histories;
    RecyclerView recyclerView;
    String namamk;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lihathistory,container,false);

        Bundle srg = getArguments();
        namamk = srg.getString("namamk");

        textView = view.findViewById(R.id.lihathistory_namamk);
        textView.setText(namamk);
        recyclerView = view.findViewById(R.id.lihathistory_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        bacadata();


        return view;
    }

    private void bacadata() {

        histories = new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Mahasiswa").child(firebaseAuth.getCurrentUser().getUid()).child("absensi")
                .child(namamk);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                histories.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    History history = snapshot.getValue(History.class);
                    histories.add(history);
                }

                historyMahasiswa = new AdapterHistoryMahasiswa(getContext(),histories);
                recyclerView.setAdapter(historyMahasiswa);
                historyMahasiswa.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
