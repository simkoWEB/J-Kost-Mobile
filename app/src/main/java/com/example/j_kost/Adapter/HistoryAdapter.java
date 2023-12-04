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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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


        if (getItemCount() == 0) {

        } else {
            // Jika ada transaksi, tampilkan data normal
            Transaksi data = listData.get(position);
            holder.judulHistory.setText(data.getNamaTransaksi());
            int harga = data.getHarga();
            holder.hargaBulanan.setText(Transaksi.formatDec(harga));
            holder.historyWaktu.setText(data.getTanggal());
        }



        String tanggal = listData.get(position).getTanggal();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(tanggal);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int month = calendar.get(Calendar.MONTH);
            String[] namaBulanSingkat = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

            holder.bulanCalendar.setText(namaBulanSingkat[month]);
            holder.tglCalendar.setText(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RincianPembayaran.class);
                // Pass data to the RincianPembayaran activity if needed
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView judulHistory, hargaBulanan, historyWaktu, bulanCalendar, tglCalendar;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            judulHistory = itemView.findViewById(R.id.judulhistory);
            hargaBulanan = itemView.findViewById(R.id.hargaBulanan);
            historyWaktu = itemView.findViewById(R.id.tanggalTransaksi);
            bulanCalendar = itemView.findViewById(R.id.bulanCalendar);
            tglCalendar = itemView.findViewById(R.id.tglCalendar);
        }
    }
}

