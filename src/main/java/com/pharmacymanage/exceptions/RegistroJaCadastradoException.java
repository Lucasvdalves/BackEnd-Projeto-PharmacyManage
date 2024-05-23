package com.pharmacymanage.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor

public class RegistroJaCadastradoException extends RuntimeException {

    private String nome;

    private  String identificador;

    public RegistroJaCadastradoException(String nome, Integer identificador) {
        this(nome, identificador.toString());
    }

    public RegistroJaCadastradoException(String nome, Long identificador) {
        this(nome, identificador.toString());
    }

    public String getMessage() {

        if (nome == null || identificador == null)
            return null;

        if (nome == "Medicamento")

            return String.format("Medicamento de registro %s já existe", identificador);

        if (nome == "Farmacia")
            return String.format("Farmácia de CNPJ %s já existe", identificador);

        return "Contate o suporte.";
    }
}
