package com.pharmacymanage.exceptions;

public class RequisicaoInvalidaException extends RuntimeException {

    private String mensagem;

    public RequisicaoInvalidaException(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMessage() {
        return mensagem;
    }
}
