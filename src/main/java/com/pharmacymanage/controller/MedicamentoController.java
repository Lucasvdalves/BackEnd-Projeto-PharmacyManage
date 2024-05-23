package com.pharmacymanage.controller;

import com.pharmacymanage.dto.MedicamentoRequest;
import com.pharmacymanage.dto.MedicamentoResponse;
import com.pharmacymanage.service.MedicamentoService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import java.net.URI;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    public MedicamentoController(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }

    @GetMapping
    public Page<MedicamentoResponse> consultarMedicamentos(Pageable pageable,
                                                           @RequestParam(required = false, defaultValue = "3") Integer size,
                                                           @RequestParam(required = false, defaultValue = "0") Integer page) {
        pageable = PageRequest.of(page, size);
        return medicamentoService.consultarMedicamentos(pageable);
    }

    @PostMapping
    public ResponseEntity<MedicamentoResponse> registrarMedicamento(@RequestBody @Valid MedicamentoRequest medicamentoRequest) {
        var medicamentoResponse = medicamentoService.registrarMedicamento(medicamentoRequest);
        var locationUri = URI.create("/medicamentos/" + medicamentoResponse.getNroRegistro());
        return ResponseEntity.created(locationUri).body(medicamentoResponse);
    }

}