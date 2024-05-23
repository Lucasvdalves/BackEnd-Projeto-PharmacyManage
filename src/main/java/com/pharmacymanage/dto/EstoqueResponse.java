package com.pharmacymanage.dto;

import com.pharmacymanage.repository.MedicamentoRepository;
import com.pharmacymanage.service.MedicamentoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.pharmacymanage.model.Estoque;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class EstoqueResponse {

    private Long nroRegistro;

    private String nome;

    private Integer quantidade;

    private LocalDateTime dataAtualizacao;

    public EstoqueResponse(Estoque estoque, String nome) {
        this.nroRegistro = estoque.getNroRegistro();
        this.nome = nome;
        this.quantidade = estoque.getQuantidade();
        this.dataAtualizacao = estoque.getDataAtualizacao();
    }
}