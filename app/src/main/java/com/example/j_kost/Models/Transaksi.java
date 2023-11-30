package com.example.j_kost.Models;

public class Transaksi {

    public static String formatDec(int val) {
        return String.format("Rp. %,d", val).replace('.', ',');
    }
    private String bulan;
    private int harga;
    private String waktu;

    public Transaksi(String bulan, String waktu, int harga) {
        this.bulan = bulan;
        this.harga = harga;
        this.waktu = waktu;
    }

    public String getBulan() {
        return bulan;
    }

    public int getHarga() {
        return harga;
    }

    public String getWaktu(){
        return waktu;
    }
}
