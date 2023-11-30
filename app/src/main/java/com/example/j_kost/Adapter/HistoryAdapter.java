package com.example.j_kost.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.j_kost.DetailActivity.RincianPembayaran;
import com.example.j_kost.Models.Transaksi;
import com.example.j_kost.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HolderData> {
    List<Transaksi> listData;
    LayoutInflater inflater;
    Context context;

    public HistoryAdapter(Context context, List<Transaksi> listData) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listData = listData;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_history_transaksi, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        Transaksi data = listData.get(position);

        holder.txtBulan.setText(listData.get(position).getBulan()); // Mengatur teks berdasarkan objek Transaksi
        int harga = listData.get(position).getHarga();
        holder.txtHarga.setText(Transaksi.formatDec(harga));
        holder.txtWaktu.setText(listData.get(position).getWaktu());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RincianPembayaran.class);
                // You might want to pass some data to the RincianPembayaran activity here
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView txtBulan;
        TextView txtHarga;
        TextView txtWaktu;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            txtBulan = itemView.findViewById(R.id.judulhistory);
            txtHarga = itemView.findViewById(R.id.hargaBulanan);
            txtWaktu = itemView.findViewById(R.id.historyWaktu);
        }
    }
}

