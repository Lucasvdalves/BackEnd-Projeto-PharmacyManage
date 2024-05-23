package com.pharmacymanage.dto;

import com.pharmacymanage.model.enums.TipoMedicamentoEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class MedicamentoRequest {

    @NotNull(message = "Campo obrigatório")
    private Long nroRegistro;

    @NotBlank(message = "Campo obrigatório")
    private String nome;

    @NotBlank(message = "Campo obrigatório")
    private String laboratorio;

    @NotBlank(message = "Campo obrigatório")
    private String dosagem;

    @NotBlank(message = "Campo obrigatório")
    private String descricao;

    @NotNull(message = "Campo obrigatório")
    private BigDecimal preco;

    @NotNull(message = "Campo obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoMedicamentoEnum tipo;
}
