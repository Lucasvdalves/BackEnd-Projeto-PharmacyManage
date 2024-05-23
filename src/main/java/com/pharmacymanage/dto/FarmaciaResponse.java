package com.pharmacymanage.dto;

import com.pharmacymanage.model.Endereco;
import com.pharmacymanage.model.Farmacia;

import com.pharmacymanage.model.Telefone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class FarmaciaResponse {

    private Long cnpj;

    private String razaoSocial;

    private String nomeFantasia;

    private String email;

    private List<String> telefones;

    private Endereco endereco;

    public FarmaciaResponse(Farmacia farmacia) {
        this.cnpj = farmacia.getCnpj();
        this.razaoSocial = farmacia.getRazaoSocial();
        this.nomeFantasia = farmacia.getNomeFantasia();
        this.email = farmacia.getEmail();
        this.endereco = farmacia.getEndereco();

        if (farmacia.getTelefone()!=null) {
            this.telefones = farmacia.getTelefone().stream()
                    .map(telefone -> formatarTelefone(telefone))
                    .toList();
        } else {
            this.telefones = new ArrayList<>();
        }
    }

    private String formatarTelefone(Telefone telefone) {
        return String.format("%s (%s) %s-%s",
                telefone.getCodigoPais(),
                telefone.getCodigoDDD(),
                telefone.getNumeroTelefone().substring(0, 4),
                telefone.getNumeroTelefone().substring(5)
        );
    }
}