package com.pharmacymanage.service;

import com.pharmacymanage.dto.MedicamentoRequest;
import com.pharmacymanage.dto.MedicamentoResponse;
import com.pharmacymanage.exceptions.NenhumRegistroEncontradoException;
import com.pharmacymanage.exceptions.RegistroJaCadastradoException;
import com.pharmacymanage.exceptions.RegistroNaoEncontradoException;
import com.pharmacymanage.exceptions.RequisicaoInvalidaException;
import com.pharmacymanage.model.Medicamento;
import com.pharmacymanage.repository.MedicamentoRepository;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final ModelMapper modelMapper;

    public MedicamentoService(MedicamentoRepository medicamentoRepository, ModelMapper modelMapper) {
        this.medicamentoRepository = medicamentoRepository;
        this.modelMapper = modelMapper;
    }

    public Medicamento consultarMedicamento(Long numeroRegistro) {
        if (numeroRegistro == null) {
            throw new RequisicaoInvalidaException("Dados inválidos.");
        }
        return medicamentoRepository.findById(numeroRegistro)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Medicamento", numeroRegistro));
    }

    public Page<MedicamentoResponse> consultarMedicamentos(Pageable pageable) {
        Page<Medicamento> medicamentos = medicamentoRepository.findAll(pageable);
        if (medicamentos.isEmpty()) {
            throw new NenhumRegistroEncontradoException("Nenhum registro encontrado na lista de medicamentos.");
        }
        return medicamentos.map(this::convertToResponse);
    }

    private MedicamentoResponse convertToResponse(Medicamento medicamento) {
        return modelMapper.map(medicamento, MedicamentoResponse.class);
    }

    @Transactional
    public MedicamentoResponse registrarMedicamento(MedicamentoRequest request) {
        if (request == null) {
            throw new RequisicaoInvalidaException("Dados inválidos.");
        }

        Medicamento medicamento = modelMapper.map(request, Medicamento.class);
        boolean registroJaCadastrado = medicamentoRepository.existsById(medicamento.getNroRegistro());

        if (registroJaCadastrado) {
            throw new RegistroJaCadastradoException("Medicamento", medicamento.getNroRegistro());
        }

        medicamento = medicamentoRepository.save(medicamento);
        return modelMapper.map(medicamento, MedicamentoResponse.class);
    }
}