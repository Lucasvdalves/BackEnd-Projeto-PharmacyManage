package com.pharmacymanage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EnderecoResponse {

    private Long cep;

    private String logradouro;

    private String bairro;

    private String cidade;

    private String estado;

    private String complemento;

}