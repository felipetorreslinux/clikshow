package com.clikshow.Validation;

public class CPF {
    public static String MaskCpf(String cpf){
        String cp = null;
        cp = cpf.charAt(0)+""+cpf.charAt(1)+""+cpf.charAt(2)+"."+cpf.charAt(3)+""+cpf.charAt(4)+""+cpf.charAt(5)+"."+cpf.charAt(6)+""+cpf.charAt(7)+""+cpf.charAt(8)+"-"+cpf.charAt(9)+""+cpf.charAt(10);
        return cp;
    };



}
