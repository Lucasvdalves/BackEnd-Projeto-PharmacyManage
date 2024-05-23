package com.pharmacymanage.dto;

import com.pharmacymanage.model.Telefone;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class TelefoneResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String codigoPais;

    private String codigoDDD;

    private String numeroTelefone;

    public TelefoneResponse(Telefone telefone) {
        this.id = telefone.getId();
        this.codigoPais = telefone.getCodigoPais();
        this.codigoDDD = telefone.getCodigoDDD();
        this.numeroTelefone = telefone.getNumeroTelefone();
    }
}
