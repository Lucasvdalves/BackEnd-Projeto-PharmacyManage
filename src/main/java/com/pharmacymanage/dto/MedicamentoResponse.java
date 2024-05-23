package com.pharmacymanage.dto;

import com.pharmacymanage.model.Medicamento;
import com.pharmacymanage.model.enums.TipoMedicamentoEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class MedicamentoResponse {

    private Long nroRegistro;

    private String nome;

    private String laboratorio;

    private String dosagem;

    private String descricao;

    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    private TipoMedicamentoEnum tipo;

    public MedicamentoResponse(Medicamento medicamento) {
        this.nroRegistro = medicamento.getNroRegistro();
        this.nome = medicamento.getNome();
        this.laboratorio = medicamento.getLaboratorio();
        this.dosagem = medicamento.getDosagem();
        this.descricao = medicamento.getDescricao();
        this.preco = medicamento.getPreco();
        this.tipo = medicamento.getTipo();
    }

}