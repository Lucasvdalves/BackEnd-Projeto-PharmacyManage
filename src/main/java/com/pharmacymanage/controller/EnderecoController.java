package com.pharmacymanage.controller;


import com.pharmacymanage.dto.EnderecoResponse;
import com.pharmacymanage.service.EnderecoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping("{cep}/busca")
    public ResponseEntity<?> consultarEnderecoViaCep(@PathVariable("cep") String cep) {
        EnderecoResponse enderecoResponse = enderecoService.consultarEnderecoViaCep(cep);
        return ResponseEntity.ok(enderecoResponse);
    }
}