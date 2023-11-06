package com.example.j_kost.Data;

public class UserData {
    private String idUser;
    private String namaLengkap;
    private String email;
    private String tgglLahir;
    private String jenisKelamin;
    private String alamat;
    private String noHp;
    private String fotoUser;

    // Constructor
    public UserData(String idUser, String namaLengkap, String email, String tgglLahir, String jenisKelamin, String alamat, String noHp, String fotoUser) {
        this.idUser = idUser;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.tgglLahir = tgglLahir;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.noHp = noHp;
        this.fotoUser = fotoUser;
    }

    // Getter dan Setter untuk setiap atribut
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTgglLahir() {
        return tgglLahir;
    }

    public void setTgglLahir(String tgglLahir) {
        this.tgglLahir = tgglLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getFotoUser() {
        return fotoUser;
    }

    public void setFotoUser(String fotoUser) {
        this.fotoUser = fotoUser;
    }
}

