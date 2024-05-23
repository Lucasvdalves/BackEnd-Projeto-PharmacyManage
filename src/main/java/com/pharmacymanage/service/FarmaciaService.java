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

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FarmaciaService {

    private final FarmaciaRepository farmaciaRepository;
    private final ModelMapper modelMapper;


    public FarmaciaService(FarmaciaRepository farmaciaRepository, ModelMapper modelMapper) {
        this.farmaciaRepository = farmaciaRepository;
        this.modelMapper = modelMapper;
    }

    public Farmacia consultarFarmacia(Long cnpj) {
        if (cnpj == null) {
            throw new CnpjInvalidoException(cnpj);
        }
        return farmaciaRepository.findById(cnpj)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Farmacia", cnpj));
    }

    public Page<FarmaciaResponse> consultarFarmacias(Pageable pageable) {
        Page<Farmacia> farmacias = farmaciaRepository.findAll(pageable);
        if (farmacias.isEmpty()) {
            throw new NenhumRegistroEncontradoException("Nenhum registro encontrado na lista de farmácias.");
        }
        return farmacias.map(FarmaciaResponse::new);
    }

    @Transactional
    public FarmaciaResponse registrarFarmacia(@NotNull FarmaciaRequest farmaciaRequest) {
        if (farmaciaRequest == null) {
            throw new RequisicaoInvalidaException("Dados inválidos.");
        }
        Farmacia farmacia = modelMapper.map(farmaciaRequest, Farmacia.class);
        boolean registroJaCadastrado = farmaciaRepository.existsById(farmacia.getCnpj());
        if (registroJaCadastrado)
            throw new RegistroJaCadastradoException("Farmacia", farmacia.getCnpj());
        farmacia = farmaciaRepository.save(farmacia);
        return new FarmaciaResponse(farmacia);
    }
}