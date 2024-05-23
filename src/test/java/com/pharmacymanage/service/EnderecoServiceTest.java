package com.pharmacymanage.service;

import com.pharmacymanage.client.CepClient;
import com.pharmacymanage.dto.EnderecoCepResponse;
import com.pharmacymanage.exceptions.CepNaoEncontradoException;
import com.pharmacymanage.exceptions.RequisicaoInvalidaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {

    @Mock
    private CepClient cepClient;

    @InjectMocks
    private EnderecoService enderecoService;

    @Test
    void deveConsultarEnderecoViaCep() {
        String cep = "12345";

        EnderecoCepResponse enderecoCepResponse = new EnderecoCepResponse();
        enderecoCepResponse.setCep(cep);

        given(cepClient.consultarCep(cep)).willReturn(enderecoCepResponse);
        assertDoesNotThrow(() -> enderecoService.consultarEnderecoViaCep(cep));
    }

    @Test
    void deveLancarExcecaoQuandoCepForNuloVazioOuBranco() {
        assertThrows(RequisicaoInvalidaException.class, () -> enderecoService.consultarEnderecoViaCep(null));
        assertThrows(RequisicaoInvalidaException.class, () -> enderecoService.consultarEnderecoViaCep(""));
        assertThrows(RequisicaoInvalidaException.class, () -> enderecoService.consultarEnderecoViaCep(" "));
    }

    @Test
    void deveLancarExcecaoQuandoCepNaoForEncontrado() {
        String cep = "12345";

        given(cepClient.consultarCep(cep)).willReturn(null);
        assertThrows(CepNaoEncontradoException.class, () -> enderecoService.consultarEnderecoViaCep("12345"));
    }
}