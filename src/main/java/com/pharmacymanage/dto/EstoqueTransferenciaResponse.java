package com.pharmacymanage.dto;

import java.util.List;

import com.pharmacymanage.model.Estoque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class EstoqueTransferenciaResponse {

    private Long nroRegistro;

    private Long cnpjOrigem;

    private Integer quantidadeOrigem;

    private Long cnpjDestino;

    private Integer quantidadeDestino;

    public EstoqueTransferenciaResponse(List<Estoque> estoques) {
        Estoque estoqueOrigem = estoques.get(0);
        Estoque estoqueDestino = estoques.get(1);
        this.nroRegistro = estoqueOrigem.getNroRegistro();
        this.cnpjOrigem = estoqueOrigem.getCnpj();
        this.quantidadeOrigem = estoqueOrigem.getQuantidade();
        this.cnpjDestino = estoqueDestino.getCnpj();
        this.quantidadeDestino = estoqueDestino.getQuantidade();
    }
}