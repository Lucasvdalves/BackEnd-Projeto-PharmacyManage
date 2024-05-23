package com.pharmacymanage.controller;

import com.pharmacymanage.dto.EstoqueRequest;
import com.pharmacymanage.dto.EstoqueResponse;
import com.pharmacymanage.dto.EstoqueTransferenciaRequest;
import com.pharmacymanage.dto.EstoqueTransferenciaResponse;
import com.pharmacymanage.exceptions.RequisicaoInvalidaException;
import com.pharmacymanage.model.Estoque;
import com.pharmacymanage.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/farmacias/{cnpj}/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping
    public Page<EstoqueResponse> consultarEstoques(Pageable pageable,
                                                   @PathVariable("cnpj") Long cnpj,
                                                   @RequestParam(required = false, defaultValue = "3") Integer size,
                                                   @RequestParam(required = false, defaultValue = "0") Integer page) {
        pageable = PageRequest.of(page, size);
        return estoqueService.consultarEstoques(pageable, cnpj);
    }

    @PostMapping
    public ResponseEntity<EstoqueResponse> registrarEstoque(@PathVariable("cnpj") Long cnpj, @RequestBody @Valid EstoqueRequest request) {
        if (!cnpj.equals(request.getCnpj())) {
            throw new RequisicaoInvalidaException(String.format("CNPJ da requisição é diferente de %s", cnpj));
        }
        var estoque = estoqueService.registrarEstoque(request);
        return new ResponseEntity<EstoqueResponse>(estoqueService.convertToResponse(estoque), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<EstoqueResponse> venderEstoque(@PathVariable("cnpj") Long cnpj, @RequestBody @Valid EstoqueRequest request) {
        if (!cnpj.equals(request.getCnpj())) {
            throw new RequisicaoInvalidaException(String.format("CNPJ da requisição é diferente de %s", cnpj));
        }
        var estoque = estoqueService.venderEstoque(request);
        return new ResponseEntity<EstoqueResponse>(estoqueService.convertToResponse(estoque), HttpStatus.CREATED);
    }

    @PutMapping("/troca")
    public ResponseEntity<?> transferenciaEstoque(@PathVariable("cnpj") Long cnpj, @RequestBody @Valid EstoqueTransferenciaRequest request) {
        if (!cnpj.equals(request.getCnpjOrigem()) && !cnpj.equals(request.getCnpjDestino())) {
            throw new RequisicaoInvalidaException(String.format("CNPJ da requisição é diferente de %s", cnpj));
        }
        List<Estoque> estoques = estoqueService.transferenciaEstoque(request);
        return ResponseEntity.ok(new EstoqueTransferenciaResponse(estoques));
    }
}