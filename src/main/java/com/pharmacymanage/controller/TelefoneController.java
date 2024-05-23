package com.pharmacymanage.controller;

import com.pharmacymanage.dto.TelefoneRequest;
import com.pharmacymanage.dto.TelefoneResponse;
import com.pharmacymanage.service.TelefoneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/farmacias/{cnpj}/telefones")
public class TelefoneController {

    private final TelefoneService telefoneService;

    public TelefoneController(TelefoneService telefoneService) {
        this.telefoneService = telefoneService;
    }

    @PostMapping
    public ResponseEntity<Object> registrarTelefone(@PathVariable(value = "cnpj") Long cnpj,
                                                    @RequestBody @Valid TelefoneRequest telefoneRequest) {
        TelefoneResponse telefoneResponse = telefoneService.registrarTelefone(cnpj, telefoneRequest);
        if (telefoneResponse != null) {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replacePath("/farmacias/{cnpj}/telefones")
                    .buildAndExpand(cnpj)
                    .toUri();
            return ResponseEntity.created(uri).body(telefoneResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Preencha os campos corretamente.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeTelefone(@PathVariable (value = "id") Long id, @PathVariable (value = "cnpj") Long cnpj) {
        telefoneService.removeTelefonePorId(id, cnpj);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}