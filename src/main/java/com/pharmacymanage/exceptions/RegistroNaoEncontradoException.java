package com.pharmacymanage.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

public class RegistroNaoEncontradoException extends RuntimeException {

    private String nome;
    private String identificador;

    public RegistroNaoEncontradoException(String nome, Long identificador) {

        this(nome, identificador.toString());
    }

    public RegistroNaoEncontradoException(String nome, Integer identificador) {

        this(nome, identificador.toString());
    }

    public String getMessage() {

        if (nome == null || identificador == null)
            return null;

        if (nome == "Farmacia")
            return String.format("Farmácia com CNPJ %s não existe", identificador);

        if (nome == "Medicamento")
            return String.format("Medicamento registro %s não existe", identificador);

        if (nome == "Estoque")
            return String.format("Estoque %s não existe", identificador);

        return "Contate o suporte.";
    }
}
