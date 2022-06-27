package com.malikazizali.e_library;

public class User {
    public String nama,email,alamat,no_telp;

    public User(){}

    public User(String email, String nama, String no_telp, String alamat){
        this.nama = nama;
        this.email = email;
        this.alamat = alamat;
        this.no_telp = no_telp;
    }
}
