package com.pharmacymanage.service;

import com.pharmacymanage.client.CepClient;
import com.pharmacymanage.dto.EnderecoCepResponse;
import com.pharmacymanage.dto.EnderecoResponse;
import com.pharmacymanage.exceptions.CepNaoEncontradoException;
import com.pharmacymanage.exceptions.RequisicaoInvalidaException;
import com.pharmacymanage.mapper.EnderecoMapper;
import feign.FeignException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    private final CepClient cepClient;

    public EnderecoService(CepClient cepClient) {
        this.cepClient = cepClient;
    }

    public EnderecoResponse consultarEnderecoViaCep(String cep) {
        if (StringUtils.isBlank(cep)) {
            throw new RequisicaoInvalidaException("CEP inválido.");
        }
        try {
            EnderecoCepResponse response = cepClient.consultarCep(cep);
            if (response == null) {
                throw new CepNaoEncontradoException(cep);
            }
            return EnderecoMapper.mapToEnderecoResponse(response);
        } catch (FeignException.BadRequest e) {
            throw new CepNaoEncontradoException(cep);
        } catch (FeignException e) {
            throw new RuntimeException();
        }
    }
}
