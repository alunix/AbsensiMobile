package com.untirta.absensimobile.Model;

public class Absensi {

    private String uid, nama, waktu, dosen,status;


    public Absensi(String uid, String nama, String waktu, String dosen, String status) {
        this.uid = uid;
        this.nama = nama;
        this.waktu = waktu;
        this.dosen = dosen;
        this.status = status;
    }

    public Absensi() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getDosen() {
        return dosen;
    }

    public void setDosen(String dosen) {
        this.dosen = dosen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
