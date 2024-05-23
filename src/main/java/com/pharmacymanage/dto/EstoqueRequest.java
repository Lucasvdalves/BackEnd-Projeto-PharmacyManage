package com.pharmacymanage.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstoqueRequest {

    @NotNull(message = "Campo obrigatório.")
    private Long cnpj;

    @NotNull(message = "Campo obrigatório.")
    private Long nroRegistro;

    @NotNull(message = "Campo obrigatório.")
    private Integer quantidade;
}
