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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.untirta.absensimobile.Adapter_Mahasiswa.AdapterHistoryMahasiswa;
import com.untirta.absensimobile.Model.History;
import com.untirta.absensimobile.R;

import java.util.List;

public class HistoryMahasiswa extends Fragment {

    List<History> historyList;
    AdapterHistoryMahasiswa adapterHistoryMahasiswa;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_mahasiswa,container,false);

        recyclerView = view.findViewById(R.id.recyclerhistorymahasiswa);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        return view;
    }

    private void readData (){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Mahasiswa").child(auth.getCurrentUser().getUid()).child("absen");


    }
}
