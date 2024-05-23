package com.pharmacymanage.exceptions;

public class CnpjInvalidoException extends RuntimeException {

    private Long cnpj;

    public CnpjInvalidoException(Long cnpj) {
        this.cnpj = cnpj;
    }

    public String getMessage() {
        if (cnpj == null) {
            return "CNPJ não informado.";
        }
        return String.format("O CNPJ %s é inválido.", cnpj.toString());
    }
}