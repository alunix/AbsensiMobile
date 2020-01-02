package com.untirta.absensimobile.Model;

public class MataKuliah {

    private String dosen,kelas,mk,sks,waktu;

    public MataKuliah(String dosen, String kelas, String mk, String sks, String waktu) {
        this.dosen = dosen;
        this.kelas = kelas;
        this.mk = mk;
        this.sks = sks;
        this.waktu = waktu;
    }

    public MataKuliah() {
    }

    public String getDosen() {
        return dosen;
    }

    public void setDosen(String dosen) {
        this.dosen = dosen;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getMk() {
        return mk;
    }

    public void setMk(String mk) {
        this.mk = mk;
    }

    public String getSks() {
        return sks;
    }

    public void setSks(String sks) {
        this.sks = sks;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
