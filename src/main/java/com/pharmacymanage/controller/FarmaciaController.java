package com.pharmacymanage.controller;

import com.pharmacymanage.dto.FarmaciaRequest;
import com.pharmacymanage.dto.FarmaciaResponse;
import com.pharmacymanage.service.FarmaciaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/farmacias")
public class FarmaciaController {

    private final FarmaciaService farmaciaService;

    public FarmaciaController(FarmaciaService farmaciaService) {
        this.farmaciaService = farmaciaService;
    }

    @GetMapping("/{cnpj}")
    public ResponseEntity<FarmaciaResponse> consultarFarmacia(@PathVariable("cnpj") Long cnpj) {
        var farmacia = farmaciaService.consultarFarmacia(cnpj);
        return ResponseEntity.ok(new FarmaciaResponse(farmacia));
    }

    @PostMapping
    public ResponseEntity<FarmaciaResponse> registrarFarmacia(@RequestBody @Valid FarmaciaRequest request) {
        var response = farmaciaService.registrarFarmacia(request);
        URI location = URI.create("/farmacias/" + response.getCnpj());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public Page<FarmaciaResponse> consultarFarmacias(Pageable pageable,
                                                     @RequestParam(required = false, defaultValue = "3") Integer size,
                                                     @RequestParam(required = false, defaultValue = "0") Integer page) {
        pageable = PageRequest.of(page, size);
        return farmaciaService.consultarFarmacias(pageable);
    }
}