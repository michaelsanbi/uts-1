package com.consolerouting;

public class Credit {

    private String idCredit;
    private String namaCredit;
    private int jumlahCredit;

    public Credit(String idCredit, String namaCredit, int jumlahCredit) {
        this.idCredit = idCredit;
        this.namaCredit = namaCredit;
        this.jumlahCredit = jumlahCredit;
    }

    public Credit() {
    }

    public String getIdCredit() {
        return idCredit;
    }

    public void setIdCredit(String idCredit) {
        this.idCredit = idCredit;
    }

    public String getNamaCredit() {
        return namaCredit;
    }

    public void setNamaCredit(String namaCredit) {
        this.namaCredit = namaCredit;
    }

    public int getJumlahCredit() {
        return jumlahCredit;
    }

    public void setJumlahCredit(int jumlahCredit) {
        this.jumlahCredit = jumlahCredit;
    }

}
