package com.pharmacymanage.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstoqueTransferenciaRequest {

    @NotNull(message = "Campo obrigatório.")
    private Long cnpjOrigem;

    @NotNull(message = "Campo obrigatório.")
    private Long cnpjDestino;

    @NotNull(message = "Campo obrigatório.")
    private Long nroRegistro;

    @NotNull(message = "Campo obrigatório.")
    private Integer quantidade;

}
