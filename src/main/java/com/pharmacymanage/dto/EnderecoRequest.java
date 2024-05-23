package com.pharmacymanage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EnderecoRequest {

    @NotNull(message = "Campo obrigatório.")
    private Long cep;

    @NotBlank(message = "Campo obrigatório.")
    private String logradouro;

    @NotBlank(message = "Campo obrigatório.")
    private String numero;

    @NotBlank(message = "Campo obrigatório.")
    private String bairro;

    @NotBlank(message = "Campo obrigatório.")
    private String cidade;

    @NotBlank(message = "Campo obrigatório.")
    private String estado;

    private String complemento;

    @NotNull(message = "Campo obrigatório.")
    private Double latitude;

    @NotNull(message = "Campo obrigatório.")
    private Double longitude;
}
