package com.example.j_kost.Models;

public class Pembayaran {
    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    private String idTransaksi;
    private String judul;
    private String status;
    private int harga;

    public Pembayaran(String idTransaksi, String judul, String status, int harga) {
        this.idTransaksi = idTransaksi;
        this.judul = judul;
        this.status = status;
        this.harga = harga;
    }
}
