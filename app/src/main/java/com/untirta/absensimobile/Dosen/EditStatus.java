package com.untirta.absensimobile.Dosen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.untirta.absensimobile.R;

import java.util.HashMap;
import java.util.Map;

public class EditStatus extends DialogFragment {


    String nim,status,time,nama,mk;
    Button back,input;
    TextView namamahasiswa,nimmahasiswa;
    TextInputEditText inputstatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editstatus,container,false);

        nim = getArguments().getString("nim");
        nama = getArguments().getString("nama");
        mk = getArguments().getString("mk");
        status = getArguments().getString("status");
        time = getArguments().getString("time");

        namamahasiswa = view.findViewById(R.id.editstatus_namamahasiswa);
        nimmahasiswa = view.findViewById(R.id.editstatus_nimmahasiswa);
        inputstatus = view.findViewById(R.id.editstatus_inputstatus);
        back = view.findViewById(R.id.editstatus_btnkembali);
        input = view.findViewById(R.id.editstatus_btnInput);

        namamahasiswa.setText(nama);
        nimmahasiswa.setText(nim);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputsts = inputstatus.getText().toString();

                if (inputsts.isEmpty()){
                    Toast.makeText(getContext(),"Masukan status", Toast.LENGTH_SHORT).show();
                } else {

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference reference = firebaseDatabase.getReference();
                    reference.child("Absensi").child(mk).child(nim).child(time).child("status").setValue(inputsts);
                    dismiss();

                }

            }
        });


        return view;
    }
}
