package com.untirta.absensimobile.Model;

public class History {

    private String nama,nim,time,status,mk;

    public History(String nama, String nim, String time, String status, String mk) {
        this.nama = nama;
        this.nim = nim;
        this.time = time;
        this.status = status;
        this.mk = mk;
    }

    public History() {
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMk() {
        return mk;
    }

    public void setMk(String mk) {
        this.mk = mk;
    }
}
