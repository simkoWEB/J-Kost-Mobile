package com.example.j_kost.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.j_kost.DetailActivity.TransaksiActivity;
import com.example.j_kost.Models.Pembayaran;
import com.example.j_kost.Models.Transaksi;
import com.example.j_kost.R;

import java.util.List;

public class PembayaranAdapter extends RecyclerView.Adapter<PembayaranAdapter.HolderData> {
    List<Pembayaran> listData;
    LayoutInflater inflater;
    Context context;


    public PembayaranAdapter(Context context, List<Pembayaran> listData) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listData = listData;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_transaksi, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {

        Pembayaran data = listData.get(position);
        holder.id.setText(data.getIdTransaksi());
        holder.judul.setText(data.getJudul());
        int hargaFormat = data.getHarga();
        holder.harga.setText(Transaksi.formatDec(hargaFormat));
        holder.statusBayar.setText(data.getStatus());

        // Set CardView background color based on status
        if ("Belum Bayar".equals(data.getStatus())) {
            holder.cardStatus.setCardBackgroundColor(context.getResources().getColor(R.color.redDanger));
            holder.cardPembayaran.setClickable(true);
        } else if ("Proses".equals(data.getStatus())) {
            holder.cardStatus.setCardBackgroundColor(context.getResources().getColor(R.color.orangeProses));
            holder.cardPembayaran.setClickable(false);
        }

        if (!"Proses".equals(data.getStatus())) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pembayaran data = listData.get(position);
                    Intent i = new Intent(context, TransaksiActivity.class);
                    i.putExtra("idTransaksi", data.getIdTransaksi());
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView judul, harga, statusBayar, id;
        CardView cardStatus, cardPembayaran;
        public HolderData(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.idTransaksi);
            judul = itemView.findViewById(R.id.judulPembayaran);
            harga = itemView.findViewById(R.id.hargaTotal);
            statusBayar = itemView.findViewById(R.id.status);
            cardStatus = itemView.findViewById(R.id.cardStatus);
            cardPembayaran = itemView.findViewById(R.id.cardPembayaranFull);

        }
    }
}
