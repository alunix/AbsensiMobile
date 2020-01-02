package com.untirta.absensimobile.Adapter_Mahasiswa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.tapadoo.alerter.Alerter;
import com.untirta.absensimobile.Mahasiswa.AbsenMahasiswa;
import com.untirta.absensimobile.Model.MataKuliah;
import com.untirta.absensimobile.R;

import java.util.List;

public class AdapterBerandaMahasiswa extends RecyclerView.Adapter<AdapterBerandaMahasiswa.Holder> {

    Context context;
    List<MataKuliah> mataKuliahList;

    public AdapterBerandaMahasiswa(Context context, List<MataKuliah> mataKuliahList) {
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

        public Holder(@NonNull final View itemView) {
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
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    if (auth.getCurrentUser().isEmailVerified() == false){
                        Toast.makeText(context,"Verifikasi email kamu terlebih dahulu",Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(context, AbsenMahasiswa.class);
                        intent.putExtra("namaMK",mataKuliahList.get(posisi).getMk());
                        context.startActivity(intent);
                    }
                }
            });

        }
    }

}
