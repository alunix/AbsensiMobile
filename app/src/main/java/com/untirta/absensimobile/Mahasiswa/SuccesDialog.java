package com.untirta.absensimobile.Mahasiswa;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.untirta.absensimobile.R;

public class SuccesDialog extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    TextView desc;
    Button input,cancel;
    String namamk;
    DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.succesdialog,container,false);

        Bundle srg = getArguments();
        namamk = srg.getString("namamk");


        desc = view.findViewById(R.id.berhasilabsen);
        input = view.findViewById(R.id.succesmahasiswa_btninput);
        cancel = view.findViewById(R.id.succesmahasiswa_btncancel);

        desc.setText("click input absen untuk memvalidasi absen" + " "+ namamk);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),DashboardMahasiswa.class));
            }
        });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }
}
