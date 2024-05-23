package com.pharmacymanage.service;

import com.pharmacymanage.dto.TelefoneRequest;
import com.pharmacymanage.dto.TelefoneResponse;
import com.pharmacymanage.exceptions.CnpjInvalidoException;
import com.pharmacymanage.exceptions.RegistroNaoEncontradoException;
import com.pharmacymanage.exceptions.RequisicaoInvalidaException;
import com.pharmacymanage.model.Telefone;
import com.pharmacymanage.model.Farmacia;
import com.pharmacymanage.repository.FarmaciaRepository;
import com.pharmacymanage.repository.TelefoneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@Service
public class TelefoneService {

    private final TelefoneRepository telefoneRepository;
    private final FarmaciaService farmaciaService;

    public TelefoneService(TelefoneRepository telefoneRepository, FarmaciaService farmaciaService) {
        this.telefoneRepository = telefoneRepository;
        this.farmaciaService = farmaciaService;
    }

    @Transactional
    public TelefoneResponse registrarTelefone(Long cnpj, TelefoneRequest telefoneRequest) {
        if (cnpj == null) {
            throw new CnpjInvalidoException(cnpj);
        }
        if (telefoneRequest == null) {
            throw new RequisicaoInvalidaException("Preencha corretamente os dados.");
        }
        Farmacia farmacia = farmaciaService.consultarFarmacia(cnpj);
        if (farmacia == null) {
            throw new CnpjInvalidoException(cnpj);
        }
        Telefone telefone = new Telefone();
        BeanUtils.copyProperties(telefoneRequest, telefone);
        farmacia.getTelefone().add(telefone);
        telefone = telefoneRepository.save(telefone);
        return new TelefoneResponse(telefone);
    }

    @Transactional
    public void removeTelefonePorId(Long id, Long cnpj) {
        if (id == null) {
            throw new RequisicaoInvalidaException("O ID não deve ser nulo.");
        }
        if (cnpj == null) {
            throw new CnpjInvalidoException(cnpj);
        }
        var optionalTelefone = telefoneRepository.findById(id);
        if (optionalTelefone.isEmpty()) {
            throw new RegistroNaoEncontradoException();
        }
        Farmacia farmacia = farmaciaService.consultarFarmacia(cnpj);
        if (farmacia == null) {
            throw new RegistroNaoEncontradoException();
        }
        removeTelefoneDeFarmacia(farmacia, optionalTelefone.get());
        telefoneRepository.delete(optionalTelefone.get());
    }

    private void removeTelefoneDeFarmacia(Farmacia farmacia, Telefone telefone) {
        farmacia.getTelefone().remove(telefone);
    }
}