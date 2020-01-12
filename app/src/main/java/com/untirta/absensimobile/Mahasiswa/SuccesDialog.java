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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.untirta.absensimobile.Model.InfoMahasiwa;
import com.untirta.absensimobile.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SuccesDialog extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    TextView desc;
    Button input,cancel;
    String namamk,nim;
    InfoMahasiwa infoMahasiwa;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Mahasiswa")
                        .child(firebaseAuth.getCurrentUser().getUid()).child("identitas");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        infoMahasiwa = dataSnapshot.getValue(InfoMahasiwa.class);

                        nim = infoMahasiwa.getNim();
                        baca();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        return view;
    }

    private void baca(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String format = DateFormat.getDateInstance().format(date);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Mahasiswa").child(firebaseAuth.getCurrentUser().getUid())
                .child("absensi").child(namamk).child(format);

        Map<Object,String> map = new HashMap<>();
        map.put("mk",namamk);
        map.put("nama",firebaseAuth.getCurrentUser().getDisplayName());
        map.put("nim",String.valueOf(nim));
        map.put("status","masuk");
        map.put("time",format);
        databaseReference.setValue(map);
        databaseReference.push();

        Intent intent = new Intent(getActivity(), DashboardMahasiswa.class);
        intent.putExtra("mk", namamk);
        intent.putExtra("time", format);
        startActivity(intent);
    }
}
