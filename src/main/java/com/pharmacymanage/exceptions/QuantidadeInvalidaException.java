package com.pharmacymanage.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

public class QuantidadeInvalidaException extends RuntimeException {

    private String nome;

    private String quantidade;

    public QuantidadeInvalidaException(String nome, Integer quantidade) {
        this(nome, quantidade.toString());
    }

    public String getMessage() {

        if (quantidade == null)
            return null;

        if (nome == "Quantidade")

            return String.format("Quantidade %s deve ser maior que 0", quantidade);

        if (nome == "Estoque")

            return String.format("Estoque %s é maior que estoque atual", quantidade);

        return String.format("Quantidade %s é inválida.", quantidade);
    }


}