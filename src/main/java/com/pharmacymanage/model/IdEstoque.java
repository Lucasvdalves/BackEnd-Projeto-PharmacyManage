package com.pharmacymanage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdEstoque implements Serializable {

    private Long cnpj;

    private Long nroRegistro;

}
