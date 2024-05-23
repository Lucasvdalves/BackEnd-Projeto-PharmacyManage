package com.pharmacymanage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class TelefoneRequest {

    @NotBlank(message = "É necessário informar o código do país.")
    private String codigoPais;

    @NotBlank(message = "É necessário informar o DDD.")
    private String codigoDDD;

    @NotBlank(message = "É necessário informar o número de telefone.")
    private String numeroTelefone;
}
