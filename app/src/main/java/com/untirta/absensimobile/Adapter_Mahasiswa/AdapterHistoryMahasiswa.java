package com.untirta.absensimobile.Adapter_Mahasiswa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.untirta.absensimobile.Model.History;
import com.untirta.absensimobile.R;

import java.util.List;

public class AdapterHistoryMahasiswa extends RecyclerView.Adapter<AdapterHistoryMahasiswa.Holder> {

    Context context;
    List<History> histories;

    public AdapterHistoryMahasiswa(Context context, List<History> histories) {
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
        holder.imageView.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        TextView nama,nim,status,mk,time;
        ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.model_history_tanggal);
            nama = itemView.findViewById(R.id.model_history_namamahasiswa);
            nim = itemView.findViewById(R.id.model_history_nimmahasiswa);
            status = itemView.findViewById(R.id.model_history_keteranganmasuk);
            imageView = itemView.findViewById(R.id.model_history_edit);

        }
    }

}
