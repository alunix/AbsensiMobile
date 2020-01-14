package com.untirta.absensimobile.Model;

public class InfoDosen {

    private String uid,nama,email,pass;

    public InfoDosen(String uid, String nama, String email, String pass) {
        this.uid = uid;
        this.nama = nama;
        this.email = email;
        this.pass = pass;
    }

    public InfoDosen() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
