package com.pharmacymanage.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CepNaoEncontradoException extends RuntimeException {

    private String cep;

    public CepNaoEncontradoException(String cep) {
        this.cep = cep;
    }

    @Override
    public String getMessage() {
        return String.format("CEP %s não encontrado", cep);
    }

    public String getCep() {
        return cep;
    }
}