package com.pharmacymanage.service;

import com.pharmacymanage.dto.EstoqueRequest;
import com.pharmacymanage.dto.EstoqueResponse;
import com.pharmacymanage.dto.EstoqueTransferenciaRequest;
import com.pharmacymanage.exceptions.CnpjInvalidoException;
import com.pharmacymanage.exceptions.NenhumRegistroEncontradoException;
import com.pharmacymanage.exceptions.QuantidadeInvalidaException;
import com.pharmacymanage.exceptions.RegistroNaoEncontradoException;
import com.pharmacymanage.exceptions.RequisicaoInvalidaException;
import com.pharmacymanage.model.Farmacia;
import com.pharmacymanage.model.Estoque;
import com.pharmacymanage.model.IdEstoque;
import com.pharmacymanage.model.Medicamento;
import com.pharmacymanage.repository.EstoqueRepository;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class EstoqueService {

    private static final String DADOS_INVALIDOS = "Dados inválidos.";

    private final FarmaciaService farmaciaService;

    private final MedicamentoService medicamentoService;

    private final EstoqueRepository estoqueRepository;

    public EstoqueService(FarmaciaService farmaciaService, MedicamentoService medicamentoService, EstoqueRepository estoqueRepository) {
        this.farmaciaService = farmaciaService;
        this.medicamentoService = medicamentoService;
        this.estoqueRepository = estoqueRepository;
    }

    public Page<EstoqueResponse> consultarEstoques(Pageable pageable, Long cnpj) {
        if (pageable == null) {
            throw new RequisicaoInvalidaException(DADOS_INVALIDOS);
        }

        if (cnpj == null) {
            throw new CnpjInvalidoException(cnpj);
        }

        Page<Estoque> estoques = estoqueRepository.findAllByCnpj(cnpj, pageable);
        if (estoques.isEmpty()) {
            throw new NenhumRegistroEncontradoException("Nenhum registro encontrado na lista de estoque.");
        }

        return estoques.map(this::convertToResponse);
    }

    public EstoqueResponse convertToResponse(Estoque estoque) {
        String nome = medicamentoService.consultarMedicamento(estoque.getNroRegistro()).getNome();
        return new EstoqueResponse(estoque, nome);
    }

    @Transactional
    public Estoque registrarEstoque(EstoqueRequest estoqueRequest) {
        validarRegistrarVenderEstoque(estoqueRequest);

        Farmacia farmacia = farmaciaService.consultarFarmacia(estoqueRequest.getCnpj());
        validarRegistroInexistente(farmacia);

        Medicamento medicamento = medicamentoService.consultarMedicamento(estoqueRequest.getNroRegistro());
        validarRegistroInexistente(medicamento);

        IdEstoque id = new IdEstoque(farmacia.getCnpj(), medicamento.getNroRegistro());
        if (!estoqueRepository.existsById(id)) {
            Estoque estoque = new Estoque(farmacia.getCnpj(), medicamento.getNroRegistro(), estoqueRequest.getQuantidade(), LocalDateTime.now());
            estoque = estoqueRepository.save(estoque);
            return estoque;
        }
        Estoque estoque = estoqueRepository.getReferenceById(id);
        estoque.setQuantidade(estoque.getQuantidade() + estoqueRequest.getQuantidade());
        estoque.setDataAtualizacao(LocalDateTime.now());
        estoque = estoqueRepository.save(estoque);

        return estoque;
    }

    @Transactional
    public Estoque venderEstoque(EstoqueRequest estoqueRequest) {
        validarRegistrarVenderEstoque(estoqueRequest);

        Farmacia farmacia = farmaciaService.consultarFarmacia(estoqueRequest.getCnpj());
        validarRegistroInexistente(farmacia);

        Medicamento medicamento = medicamentoService.consultarMedicamento(estoqueRequest.getNroRegistro());
        validarRegistroInexistente(medicamento);

        IdEstoque id = new IdEstoque(farmacia.getCnpj(), medicamento.getNroRegistro());
        if (!estoqueRepository.existsById(id)) {
            throw new RegistroNaoEncontradoException("Estoque", estoqueRequest.getNroRegistro().toString());
        }

        Estoque estoque = estoqueRepository.getReferenceById(id);
        if (estoqueRequest.getQuantidade() > estoque.getQuantidade()) {
            throw new QuantidadeInvalidaException("Estoque", estoqueRequest.getQuantidade());
        }

        estoque.setQuantidade(estoque.getQuantidade() - estoqueRequest.getQuantidade());
        estoque.setDataAtualizacao(LocalDateTime.now());
        if (estoque.getQuantidade() == 0) {
            estoqueRepository.delete(estoque);
            return estoque;
        }
        estoque = estoqueRepository.save(estoque);

        return estoque;
    }

    private void validarRegistrarVenderEstoque(EstoqueRequest estoqueRequest) {
        if (estoqueRequest == null || estoqueRequest.getNroRegistro() == null || estoqueRequest.getQuantidade() == null) {
            throw new RequisicaoInvalidaException(DADOS_INVALIDOS);
        }
        
        if (estoqueRequest.getCnpj() == null) {
            throw new CnpjInvalidoException(null);
        }
        
        if (estoqueRequest.getQuantidade() <= 0) {
            throw new QuantidadeInvalidaException("Quantidade", estoqueRequest.getQuantidade());
        }
    }

    @Transactional
    public List<Estoque> transferenciaEstoque(EstoqueTransferenciaRequest request) {
        if (request == null || request.getNroRegistro() == null || request.getQuantidade() == null) {
            throw new RequisicaoInvalidaException(DADOS_INVALIDOS);
        }

        if (request.getCnpjOrigem() == null || request.getCnpjDestino() == null) {
            throw new CnpjInvalidoException(null);
        }

        if (request.getQuantidade() <= 0) {
            throw new QuantidadeInvalidaException("Quantidade", request.getQuantidade());
        }

        Farmacia farmaciaOrigem = farmaciaService.consultarFarmacia(request.getCnpjOrigem());
        validarRegistroInexistente(farmaciaOrigem);
        Farmacia farmaciaDestino = farmaciaService.consultarFarmacia(request.getCnpjDestino());
        validarRegistroInexistente(farmaciaDestino);
        Medicamento medicamento = medicamentoService.consultarMedicamento(request.getNroRegistro());
        validarRegistroInexistente(medicamento);

        Estoque estoqueFarmaciaOrigem = getEstoquePorFarmaciaEMedicamento(farmaciaOrigem.getCnpj(), medicamento);
        if (request.getQuantidade() > estoqueFarmaciaOrigem.getQuantidade()) {
            throw new QuantidadeInvalidaException("Quantidade", request.getQuantidade());
        }

        var estoqueOrigem = venderEstoque(new EstoqueRequest(request.getCnpjOrigem(), request.getNroRegistro(), request.getQuantidade()));
        var estoqueDestino = registrarEstoque(new EstoqueRequest(request.getCnpjDestino(), request.getNroRegistro(), request.getQuantidade()));
        return Arrays.asList(estoqueOrigem, estoqueDestino);
    }

    private Estoque getEstoquePorFarmaciaEMedicamento(Long cnpj, Medicamento medicamento) {
        IdEstoque id = new IdEstoque(cnpj, medicamento.getNroRegistro());
        if (!estoqueRepository.existsById(id)) {
            throw new RegistroNaoEncontradoException("Estoque", medicamento.getNroRegistro().toString());
        }

        return estoqueRepository.getReferenceById(id);
    }

    private void validarRegistroInexistente(Object registro) {
        if (registro == null) {
            throw new RegistroNaoEncontradoException();
        }
    }
}