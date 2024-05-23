package com.pharmacymanage.client;

import com.pharmacymanage.dto.EnderecoCepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CepClient", url = "https://viacep.com.br/ws")
public interface CepClient {

    @GetMapping("/{cep}/json/")
    EnderecoCepResponse consultarCep(@PathVariable("cep") String cep);

}
