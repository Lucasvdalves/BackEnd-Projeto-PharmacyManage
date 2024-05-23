package com.pharmacymanage.service;

import com.pharmacymanage.dto.TelefoneRequest;
import com.pharmacymanage.dto.TelefoneResponse;
import com.pharmacymanage.exceptions.CnpjInvalidoException;
import com.pharmacymanage.exceptions.RegistroNaoEncontradoException;
import com.pharmacymanage.exceptions.RequisicaoInvalidaException;
import com.pharmacymanage.model.Farmacia;
import com.pharmacymanage.model.Telefone;
import com.pharmacymanage.repository.TelefoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TelefoneServiceTest {

    @Mock
    private TelefoneRepository telefoneRepository;

    @Mock
    private FarmaciaService farmaciaService;

    @InjectMocks
    private TelefoneService telefoneService;

    @Test
    void registrarTelefoneDeveLancarExcecaoQuandoCnpjForNulo() {
        assertThrows(CnpjInvalidoException.class, () -> telefoneService.registrarTelefone(null, new TelefoneRequest()));
    }

    @Test
    void registrarTelefoneDeveLancarExcecaoQuandoCnpjNaoExiste() {
        given(farmaciaService.consultarFarmacia(12345L)).willReturn(null);
        assertThrows(CnpjInvalidoException.class, () -> telefoneService.registrarTelefone(12345L, new TelefoneRequest()));
    }

    @Test
    void registrarTelefoneDeveLancarExcecaoQuandoRequestForNulo() {
        assertThrows(RequisicaoInvalidaException.class, () -> telefoneService.registrarTelefone(12345L, null));
    }

    @Test
    void deveCadastrarTelefone() {
        String codigoPais = "55";
        String codigoDDD = "48";
        String numeroTelefone = "996487159";

        Telefone telefone = new Telefone();
        Farmacia farmacia = new Farmacia();

        telefone.setCodigoPais(codigoPais);
        telefone.setCodigoDDD(codigoDDD);
        telefone.setNumeroTelefone(numeroTelefone);
        telefone.setFarmacia(farmacia);

        TelefoneRequest telefoneRequest = new TelefoneRequest();
        telefoneRequest.setCodigoPais(codigoPais);
        telefoneRequest.setCodigoDDD(codigoDDD);
        telefoneRequest.setNumeroTelefone(numeroTelefone);

        given(farmaciaService.consultarFarmacia(12345L)).willReturn(farmacia);
        given(telefoneRepository.save(any(Telefone.class))).willReturn(telefone);

        TelefoneResponse telefoneResponse = assertDoesNotThrow(() -> telefoneService.registrarTelefone(12345L, telefoneRequest));

        assertEquals(codigoPais, telefoneResponse.getCodigoPais());
        assertEquals(codigoDDD, telefoneResponse.getCodigoDDD());
        assertEquals(numeroTelefone, telefoneResponse.getNumeroTelefone());
    }

    @Test
    void deveRemoverTelefone() {
        Long id = 1L;
        Long cnpj = 12345L;

        Telefone telefone = new Telefone();
        Farmacia farmacia = new Farmacia();

        telefone.setFarmacia(farmacia);

        given(telefoneRepository.findById(id)).willReturn(Optional.of(telefone));
        given(farmaciaService.consultarFarmacia(cnpj)).willReturn(farmacia);
        assertDoesNotThrow(() -> telefoneService.removeTelefonePorId(id, cnpj));
    }

    @Test
    void removeTelefoneDeveLancarExcecaoQuandoIdForNulo() {
        assertThrows(RequisicaoInvalidaException.class, () -> telefoneService.removeTelefonePorId(null, 12345L));
    }

    @Test
    void removeTelefoneDeveLancarExcecaoQuandoIdNaoEncontrado() {
        assertThrows(RegistroNaoEncontradoException.class, () -> telefoneService.removeTelefonePorId(1L, 12345L));
    }

    @Test
    void removeTelefoneDeveLancarExcecaoQuandoCnpjNaoEncontrado() {
        given(telefoneRepository.findById(1L)).willReturn(Optional.of(new Telefone()));
        given(farmaciaService.consultarFarmacia(12345L)).willReturn(null);
        assertThrows(RegistroNaoEncontradoException.class, () -> telefoneService.removeTelefonePorId(1L, 12345L));
    }

    @Test
    void removeTelefoneDeveLancarExcecaoQuandoCnpjForNulo() {
        assertThrows(CnpjInvalidoException.class, () -> telefoneService.removeTelefonePorId(1L, null));
    }
}