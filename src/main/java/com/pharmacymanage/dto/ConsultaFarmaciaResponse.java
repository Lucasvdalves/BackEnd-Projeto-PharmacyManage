package com.pharmacymanage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaFarmaciaResponse {

    private List<FarmaciaResponse> farmacias;

}
