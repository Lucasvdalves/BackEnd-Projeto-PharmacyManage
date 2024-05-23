package com.pharmacymanage.service;

import com.pharmacymanage.dto.FarmaciaRequest;
import com.pharmacymanage.dto.FarmaciaResponse;
import com.pharmacymanage.exceptions.CnpjInvalidoException;
import com.pharmacymanage.exceptions.NenhumRegistroEncontradoException;
import com.pharmacymanage.exceptions.RegistroJaCadastradoException;
import com.pharmacymanage.exceptions.RegistroNaoEncontradoException;
import com.pharmacymanage.exceptions.RequisicaoInvalidaException;
import com.pharmacymanage.model.Farmacia;
import com.pharmacymanage.repository.FarmaciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FarmaciaServiceTest {

    @Mock
    private FarmaciaRepository farmaciaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FarmaciaService farmaciaService;

    @Test
    void deveRetornarFarmacia() {
        given(farmaciaRepository.findById(12345L)).willReturn(Optional.of(new Farmacia()));
        Farmacia farmacia = assertDoesNotThrow(() -> farmaciaService.consultarFarmacia(12345L));
        assertNotNull(farmacia);
    }

    @Test
    void deveRetornarListaDeFarmacias() {
        Pageable pageable = Pageable.ofSize(10);
        List<Farmacia> farmacias = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            farmacias.add(new Farmacia());
        }

        Page<Farmacia> page = new PageImpl<>(farmacias, Pageable.unpaged(), 10);

        given(farmaciaRepository.findAll(pageable)).willReturn(page);
        Page<FarmaciaResponse> farmaciaResponses = assertDoesNotThrow(() -> farmaciaService.consultarFarmacias(pageable));
        assertEquals(10L, farmaciaResponses.getTotalElements());
    }

    @Test
    void deveLancarExcecaoAoConsultarListaDeFarmacias() {
        Pageable pageable = Pageable.unpaged();
        given(farmaciaRepository.findAll(pageable)).willReturn(Page.empty());
        assertThrows(NenhumRegistroEncontradoException.class, () -> farmaciaService.consultarFarmacias(pageable));
    }

    @Test
    void deveLancarExcecaoAoConsultarFarmaciaComCnpjNulo() {
        assertThrows(CnpjInvalidoException.class, () -> farmaciaService.consultarFarmacia(null));
    }

    @Test
    void deveLancarExcecaoAoConsultarFarmaciaComCnpjInexistente() {
        given(farmaciaRepository.findById(12345L)).willReturn(Optional.empty());
        assertThrows(RegistroNaoEncontradoException.class, () -> farmaciaService.consultarFarmacia(12345L));
    }

    @Test
    void deveLancarExcecaoAoRegistrarFarmaciaQuandoRequestForNulo() {
        assertThrows(RequisicaoInvalidaException.class, () -> farmaciaService.registrarFarmacia(null));
    }

    @Test
    void deveLancarExcecaoQuandoFarmaciaJaExiste() {
        Long cnpj = 12345L;

        Farmacia farmacia = new Farmacia();
        farmacia.setCnpj(cnpj);
        FarmaciaRequest farmaciaRequest = new FarmaciaRequest();

        given(modelMapper.map(farmaciaRequest, Farmacia.class)).willReturn(farmacia);
        given(farmaciaRepository.existsById(cnpj)).willReturn(true);
        assertThrows(RegistroJaCadastradoException.class, () -> farmaciaService.registrarFarmacia(farmaciaRequest));
    }

    @Test
    void deveRegistrarFarmacia() {
        FarmaciaRequest farmaciaRequest = new FarmaciaRequest();
        Long cnpj = 12345L;
        farmaciaRequest.setCnpj(cnpj);
        Farmacia farmacia = new Farmacia();
        farmacia.setCnpj(cnpj);

        given(modelMapper.map(farmaciaRequest, Farmacia.class)).willReturn(farmacia);
        given(farmaciaRepository.existsById(cnpj)).willReturn(false);
        given(farmaciaRepository.save(any(Farmacia.class))).willReturn(farmacia);
        FarmaciaResponse farmaciaResponse = assertDoesNotThrow(() -> farmaciaService.registrarFarmacia(farmaciaRequest));
        assertEquals(cnpj, farmaciaResponse.getCnpj());
    }

}
