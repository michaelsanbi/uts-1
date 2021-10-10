package com.consolerouting;

public class Checkout {

    private String nomorRekening;
    private String checkoutToken;

    public Checkout() {
    }

    public Checkout(String nomorRekening, String checkoutToken) {
        this.nomorRekening = nomorRekening;
        this.checkoutToken = checkoutToken;
    }

    public String getNomorRekening() {
        return nomorRekening;
    }

    public void setNomorRekening(String nomorRekening) {
        this.nomorRekening = nomorRekening;
    }

    public String getCheckoutToken() {
        return checkoutToken;
    }

    public void setCheckoutToken(String checkoutToken) {
        this.checkoutToken = checkoutToken;
    }

}
