package com.pharmacymanage.mapper;

import com.pharmacymanage.dto.EnderecoCepResponse;
import com.pharmacymanage.dto.EnderecoResponse;

public class EnderecoMapper {

    public static EnderecoResponse mapToEnderecoResponse(EnderecoCepResponse response) {
        EnderecoResponse enderecoResponse = new EnderecoResponse();

        Long cepLong = Long.parseLong(response.getCep().replaceAll("-", ""));
        enderecoResponse.setCep(cepLong);

        enderecoResponse.setLogradouro(response.getLogradouro());
        enderecoResponse.setBairro(response.getBairro());
        enderecoResponse.setCidade(response.getLocalidade());
        enderecoResponse.setEstado(response.getUf());
        enderecoResponse.setComplemento(response.getComplemento());

        return enderecoResponse;
    }
}

