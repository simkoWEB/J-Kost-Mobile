package com.example.j_kost.Models;

public class Transaksi {
    private String namaTransaksi;
    private String tanggal;
    private int harga;
    private String idTransaksi;

    public Transaksi(String idTransaksi, String namaTransaksi, String tanggal, int harga) {
        this.idTransaksi = idTransaksi;
        this.namaTransaksi = namaTransaksi;
        this.tanggal = tanggal;
        this.harga = harga;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getNamaTransaksi() {
        return namaTransaksi;
    }

    public void setNamaTransaksi(String namaTransaksi) {
        this.namaTransaksi = namaTransaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }
    public static String formatDec(int val) {
        return String.format("%,d", val).replace('.', ',');
    }
}

