package com.clikshow.IUGU.Models;

public class CartaoCreditoModel {
    private int id;
    private String method_cartao;
    private String brand_cartao;
    private String holder_name_cartao;
    private String display_number_cartao;
    private String bin_cartao;
    private String month_cartao;
    private String year_cartao;
    private boolean teste_cartao;
    private boolean starts_cartao;
    private int created_at;

    public CartaoCreditoModel(int id, String method_cartao, String brand_cartao, String holder_name_cartao, String display_number_cartao, String bin_cartao, String month_cartao, String year_cartao, boolean teste_cartao, boolean starts_cartao, int created_at) {
        this.id = id;
        this.method_cartao = method_cartao;
        this.brand_cartao = brand_cartao;
        this.holder_name_cartao = holder_name_cartao;
        this.display_number_cartao = display_number_cartao;
        this.bin_cartao = bin_cartao;
        this.month_cartao = month_cartao;
        this.year_cartao = year_cartao;
        this.teste_cartao = teste_cartao;
        this.starts_cartao = starts_cartao;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod_cartao() {
        return method_cartao;
    }

    public void setMethod_cartao(String method_cartao) {
        this.method_cartao = method_cartao;
    }

    public String getBrand_cartao() {
        return brand_cartao;
    }

    public void setBrand_cartao(String brand_cartao) {
        this.brand_cartao = brand_cartao;
    }

    public String getHolder_name_cartao() {
        return holder_name_cartao;
    }

    public void setHolder_name_cartao(String holder_name_cartao) {
        this.holder_name_cartao = holder_name_cartao;
    }

    public String getDisplay_number_cartao() {
        return display_number_cartao;
    }

    public void setDisplay_number_cartao(String display_number_cartao) {
        this.display_number_cartao = display_number_cartao;
    }

    public String getBin_cartao() {
        return bin_cartao;
    }

    public void setBin_cartao(String bin_cartao) {
        this.bin_cartao = bin_cartao;
    }

    public String getMonth_cartao() {
        return month_cartao;
    }

    public void setMonth_cartao(String month_cartao) {
        this.month_cartao = month_cartao;
    }

    public String getYear_cartao() {
        return year_cartao;
    }

    public void setYear_cartao(String year_cartao) {
        this.year_cartao = year_cartao;
    }

    public boolean isTeste_cartao() {
        return teste_cartao;
    }

    public void setTeste_cartao(boolean teste_cartao) {
        this.teste_cartao = teste_cartao;
    }

    public boolean isStarts_cartao() {
        return starts_cartao;
    }

    public void setStarts_cartao(boolean starts_cartao) {
        this.starts_cartao = starts_cartao;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }
}

