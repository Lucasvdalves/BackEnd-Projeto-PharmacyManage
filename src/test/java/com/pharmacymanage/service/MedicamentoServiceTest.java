package com.pharmacymanage.service;

import com.pharmacymanage.dto.MedicamentoRequest;
import com.pharmacymanage.dto.MedicamentoResponse;
import com.pharmacymanage.exceptions.NenhumRegistroEncontradoException;
import com.pharmacymanage.exceptions.RegistroJaCadastradoException;
import com.pharmacymanage.exceptions.RequisicaoInvalidaException;
import com.pharmacymanage.model.Medicamento;
import com.pharmacymanage.repository.MedicamentoRepository;
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
class MedicamentoServiceTest {

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MedicamentoService medicamentoService;

    @Test
    void deveConsultarMedicamento() {
        given(medicamentoRepository.findById(12345L)).willReturn(Optional.of(new Medicamento()));
        Medicamento medicamento = assertDoesNotThrow(() -> medicamentoService.consultarMedicamento(12345L));
        assertNotNull(medicamento);
    }

    @Test
    void deveLancarExcecaoQuandoNumRegistroForNulo() {
        assertThrows(RequisicaoInvalidaException.class, () -> medicamentoService.consultarMedicamento(null));

    }

    @Test
    void deveConsultarListaMedicamentos() {
        Pageable pageable = Pageable.ofSize(10);
        List<Medicamento> medicamentos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            medicamentos.add(new Medicamento());
        }

        Page<Medicamento> page = new PageImpl<>(medicamentos, Pageable.unpaged(), 10);

        given(medicamentoRepository.findAll(pageable)).willReturn(page);
        Page<MedicamentoResponse> medicamentoResponses = assertDoesNotThrow(() -> medicamentoService.consultarMedicamentos(pageable));
        assertEquals(10L, medicamentoResponses.getTotalElements());
    }

    @Test
    void deveLancarExcecaoQuandoListaMedicamentosForVazia() {
        Pageable pageable = Pageable.unpaged();
        given(medicamentoRepository.findAll(pageable)).willReturn(Page.empty());
        assertThrows(NenhumRegistroEncontradoException.class, () -> medicamentoService.consultarMedicamentos(pageable));
    }

    @Test
    void deveRegistrarMedicamento() {
        MedicamentoRequest medicamentoRequest = new MedicamentoRequest();
        Long nroRegistro = 1234L;
        medicamentoRequest.setNroRegistro(nroRegistro);
        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(nroRegistro);

        given(modelMapper.map(medicamentoRequest, Medicamento.class)).willReturn(medicamento);
        given(medicamentoRepository.existsById(nroRegistro)).willReturn(false);
        given(medicamentoRepository.save(any(Medicamento.class))).willReturn(medicamento);

        MedicamentoResponse medicamentoResponse = new MedicamentoResponse();
        medicamentoResponse.setNroRegistro(nroRegistro);

        given(modelMapper.map(medicamento, MedicamentoResponse.class)).willReturn(medicamentoResponse);
        medicamentoResponse = assertDoesNotThrow(() -> medicamentoService.registrarMedicamento(medicamentoRequest));
        assertEquals(nroRegistro, medicamentoResponse.getNroRegistro());
    }

    @Test
    void deveLancarExcecaoQuandoRequestDeRegistroForNulo() {
        assertThrows(RequisicaoInvalidaException.class, () -> medicamentoService.registrarMedicamento(null));
    }

    @Test
    void deveLancarExcecaoQuandoRegistroJaExistente() {
        Long nroRegistro = 1234L;

        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(1234L);
        MedicamentoRequest medicamentoRequest = new MedicamentoRequest();

        given(modelMapper.map(medicamentoRequest, Medicamento.class)).willReturn(medicamento);
        given(medicamentoRepository.existsById(nroRegistro)).willReturn(true);
        assertThrows(RegistroJaCadastradoException.class, () -> medicamentoService.registrarMedicamento(medicamentoRequest));
    }


}