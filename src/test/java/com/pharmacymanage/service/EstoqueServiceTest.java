package com.pharmacymanage.service;

import com.pharmacymanage.dto.EstoqueRequest;
import com.pharmacymanage.dto.EstoqueResponse;
import com.pharmacymanage.dto.EstoqueTransferenciaRequest;
import com.pharmacymanage.exceptions.CnpjInvalidoException;
import com.pharmacymanage.exceptions.NenhumRegistroEncontradoException;
import com.pharmacymanage.exceptions.QuantidadeInvalidaException;
import com.pharmacymanage.exceptions.RegistroNaoEncontradoException;
import com.pharmacymanage.exceptions.RequisicaoInvalidaException;
import com.pharmacymanage.model.Estoque;
import com.pharmacymanage.model.Farmacia;
import com.pharmacymanage.model.IdEstoque;
import com.pharmacymanage.model.Medicamento;
import com.pharmacymanage.repository.EstoqueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private MedicamentoService medicamentoService;

    @Mock
    private FarmaciaService farmaciaService;

    @InjectMocks
    private EstoqueService estoqueService;

    @Test
    void deveConsultarEstoque() {
        Pageable pageable = Pageable.ofSize(10);
        List<Estoque> estoques = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Estoque estoque = new Estoque();
            estoque.setNroRegistro(1234L);
            estoques.add(estoque);
        }

        Page<Estoque> page = new PageImpl<>(estoques, Pageable.unpaged(), 10);

        given(estoqueRepository.findAllByCnpj(12345L, pageable)).willReturn(page);
        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(1234L);
        medicamento.setNome("Bup");

        for (Estoque estoque : estoques) {
            given(medicamentoService.consultarMedicamento(estoque.getNroRegistro())).willReturn(medicamento);
        }
        Page<EstoqueResponse> estoqueResponse = assertDoesNotThrow(() -> estoqueService.consultarEstoques(pageable, 12345L));
        assertEquals(10L, estoqueResponse.getTotalElements());
    }

    @Test
    void deveLancarExcecaoAoConsultarEstoqueQuandoCnpjForNulo() {
        Pageable pageable = Pageable.ofSize(10);
        assertThrows(CnpjInvalidoException.class, () -> estoqueService.consultarEstoques(pageable, null));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjForVazio() {
        Pageable pageable = Pageable.ofSize(10);

        given(estoqueRepository.findAllByCnpj(12345L, pageable)).willReturn(Page.empty());
        assertThrows(NenhumRegistroEncontradoException.class, () -> estoqueService.consultarEstoques(pageable, 12345L));
    }

    @Test
    void deveLancarExcecaoQuandoPageForNulo() {
        assertThrows(RequisicaoInvalidaException.class, () -> estoqueService.consultarEstoques(null, 12345L));
    }

    @Test
    void deveRegistrarEstoqueQuandoExiste() {
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(12345L);
        estoqueRequest.setNroRegistro(1234L);
        estoqueRequest.setQuantidade(2);
        Estoque estoque = new Estoque();
        estoque.setCnpj(12345L);
        estoque.setNroRegistro(1234L);
        estoque.setQuantidade(5);

        given(estoqueRepository.getReferenceById(any(IdEstoque.class))).willReturn(estoque);
        given(farmaciaService.consultarFarmacia(12345L)).willReturn(new Farmacia());
        given(estoqueRepository.existsById(any(IdEstoque.class))).willReturn(true);
        given(estoqueRepository.save(estoque)).willReturn(estoque);
        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(1234L);
        given(medicamentoService.consultarMedicamento(1234L)).willReturn(medicamento);
        Estoque estoqueResponse = assertDoesNotThrow(() -> estoqueService.registrarEstoque(estoqueRequest));
        assertEquals(estoqueRequest.getNroRegistro(), estoqueResponse.getNroRegistro());
        assertEquals(estoqueRequest.getCnpj(), estoqueResponse.getCnpj());
    }

    @Test
    void deveCriarNovoEstoqueQuandoNaoExiste() {
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(12345L);
        estoqueRequest.setNroRegistro(1234L);
        estoqueRequest.setQuantidade(2);
        Estoque estoque = new Estoque();
        estoque.setCnpj(12345L);
        estoque.setNroRegistro(1234L);
        estoque.setQuantidade(5);

        given(farmaciaService.consultarFarmacia(12345L)).willReturn(new Farmacia());
        given(estoqueRepository.existsById(any(IdEstoque.class))).willReturn(false);
        given(estoqueRepository.save(any(Estoque.class))).willReturn(estoque);
        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(1234L);
        given(medicamentoService.consultarMedicamento(1234L)).willReturn(medicamento);
        Estoque estoqueResponse = assertDoesNotThrow(() -> estoqueService.registrarEstoque(estoqueRequest));
        assertEquals(estoqueRequest.getNroRegistro(), estoqueResponse.getNroRegistro());
        assertEquals(estoqueRequest.getCnpj(), estoqueResponse.getCnpj());
    }

    @Test
    void deveLancarExcecaoQuandoRequestNulo() {
        assertThrows(RequisicaoInvalidaException.class, () -> estoqueService.registrarEstoque(null));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjParaRegistroDeEstoqueForNulo() {
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setNroRegistro(1234L);
        estoqueRequest.setQuantidade(1);

        assertThrows(CnpjInvalidoException.class, () -> estoqueService.registrarEstoque(estoqueRequest));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjParaRegistroDeEstoqueForInexistente() {
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(12345L);
        estoqueRequest.setNroRegistro(1234L);
        estoqueRequest.setQuantidade(1);

        given(farmaciaService.consultarFarmacia(estoqueRequest.getCnpj())).willReturn(null);
        assertThrows(RegistroNaoEncontradoException.class, () -> estoqueService.registrarEstoque(estoqueRequest));
    }

    @Test
    void deveLancarExcecaoQuandoNroRegParaRegistroDeEstoqueForNulo() {
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(12345L);
        assertThrows(RequisicaoInvalidaException.class, () -> estoqueService.registrarEstoque(estoqueRequest));
    }

    @Test
    void deveLancarExcecaoQuandoNumRegParaRegistroDeEstoqueForInexistente() {
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setNroRegistro(12345L);
        estoqueRequest.setCnpj(12345L);
        estoqueRequest.setQuantidade(15);

        given(farmaciaService.consultarFarmacia(estoqueRequest.getCnpj())).willReturn(new Farmacia());
        given(medicamentoService.consultarMedicamento(estoqueRequest.getNroRegistro())).willReturn(null);
        assertThrows(RegistroNaoEncontradoException.class, () -> estoqueService.registrarEstoque(estoqueRequest));
    }

    @Test
    void deveLancarExcecaoQuandoQtdRegistradaForZero() {
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(12345L);
        estoqueRequest.setNroRegistro(1234L);
        estoqueRequest.setQuantidade(-1);

        assertThrows(QuantidadeInvalidaException.class, () -> estoqueService.registrarEstoque(estoqueRequest));
    }

    @Test
    void deveVenderEstoqueSemDeletar() {
        Long cnpj = 12345L;
        Long nroRegistro = 1234L;

        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(cnpj);
        estoqueRequest.setNroRegistro(nroRegistro);
        estoqueRequest.setQuantidade(1);

        Farmacia farmacia = new Farmacia();
        farmacia.setCnpj(cnpj);

        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(nroRegistro);

        Estoque estoque = new Estoque();
        estoque.setQuantidade(2);

        given(farmaciaService.consultarFarmacia(estoqueRequest.getCnpj())).willReturn(farmacia);
        given(medicamentoService.consultarMedicamento(estoqueRequest.getNroRegistro())).willReturn(medicamento);
        given(estoqueRepository.existsById(any(IdEstoque.class))).willReturn(true);
        given(estoqueRepository.getReferenceById(any(IdEstoque.class))).willReturn(estoque);
        given(estoqueRepository.save(estoque)).willReturn(estoque);

        assertDoesNotThrow(() -> estoqueService.venderEstoque(estoqueRequest));
    }

    @Test
    void deveVenderEstoqueEDeletar() {
        Long cnpj = 12345L;
        Long nroRegistro = 1234L;

        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(cnpj);
        estoqueRequest.setNroRegistro(nroRegistro);
        estoqueRequest.setQuantidade(1);

        Farmacia farmacia = new Farmacia();
        farmacia.setCnpj(cnpj);

        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(nroRegistro);

        Estoque estoque = new Estoque();
        estoque.setQuantidade(1);

        given(farmaciaService.consultarFarmacia(estoqueRequest.getCnpj())).willReturn(farmacia);
        given(medicamentoService.consultarMedicamento(estoqueRequest.getNroRegistro())).willReturn(medicamento);
        given(estoqueRepository.existsById(any(IdEstoque.class))).willReturn(true);
        given(estoqueRepository.getReferenceById(any(IdEstoque.class))).willReturn(estoque);
        estoqueRepository.delete(estoque);

        assertDoesNotThrow(() -> estoqueService.venderEstoque(estoqueRequest));
    }
    
    @Test
    void deveLancarExcecaoQuandoNumeroRegistroForNuloAoVenderEstoque() {
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(1234L);
        assertThrows(RequisicaoInvalidaException.class, () -> estoqueService.venderEstoque(estoqueRequest));
    }

    @Test
    void deveLancarExcecaoQuandoRequisicaoForNulaAoVenderEstoque() {
        assertThrows(RequisicaoInvalidaException.class, () -> estoqueService.venderEstoque(null));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjForNuloAoVenderEstoque() {
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setNroRegistro(1234L);
        estoqueRequest.setQuantidade(1);

        assertThrows(CnpjInvalidoException.class, () -> estoqueService.venderEstoque(estoqueRequest));
    }

    @Test
    void deveLancarExcecaoQuandoQtdVendidaForMenorQueUmAoVenderEstoque() {
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(12345L);
        estoqueRequest.setNroRegistro(1234L);
        estoqueRequest.setQuantidade(-1);

        assertThrows(QuantidadeInvalidaException.class, () -> estoqueService.venderEstoque(estoqueRequest));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjFarmaciaVendedoraForInexistente() {
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(12345L);
        estoqueRequest.setNroRegistro(1234L);
        estoqueRequest.setQuantidade(1);

        given(farmaciaService.consultarFarmacia(estoqueRequest.getCnpj())).willReturn(null);

        assertThrows(RegistroNaoEncontradoException.class, () -> estoqueService.venderEstoque(estoqueRequest));
    }

    @Test
    void deveLancarExcecaoQuandoNroRegistroMedicamentoVendidoForInexistente() {
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(12345L);
        estoqueRequest.setNroRegistro(1234L);
        estoqueRequest.setQuantidade(1);

        given(farmaciaService.consultarFarmacia(estoqueRequest.getCnpj())).willReturn(new Farmacia());
        given(medicamentoService.consultarMedicamento(estoqueRequest.getNroRegistro())).willReturn(null);

        assertThrows(RegistroNaoEncontradoException.class, () -> estoqueService.venderEstoque(estoqueRequest));
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueInexistenteAoVenderEstoque() {
        Long cnpj = 12345L;
        Long nroRegistro = 1234L;
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(cnpj);
        estoqueRequest.setNroRegistro(nroRegistro);
        estoqueRequest.setQuantidade(1);

        Farmacia farmacia = new Farmacia();
        farmacia.setCnpj(cnpj);

        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(nroRegistro);

        given(farmaciaService.consultarFarmacia(estoqueRequest.getCnpj())).willReturn(farmacia);
        given(medicamentoService.consultarMedicamento(estoqueRequest.getNroRegistro())).willReturn(medicamento);
        given(estoqueRepository.existsById(any(IdEstoque.class))).willReturn(false);

        assertThrows(RegistroNaoEncontradoException.class, () -> estoqueService.venderEstoque(estoqueRequest));
    }

    @Test
    void deveLancarExcecaoQuandoQtdVendidaForMaiorQueQtdEstoque() {
        Long cnpj = 12345L;
        Long nroRegistro = 1234L;
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(cnpj);
        estoqueRequest.setNroRegistro(nroRegistro);
        estoqueRequest.setQuantidade(2);

        Farmacia farmacia = new Farmacia();
        farmacia.setCnpj(cnpj);

        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(nroRegistro);

        Estoque estoque = new Estoque();
        estoque.setQuantidade(1);

        given(farmaciaService.consultarFarmacia(estoqueRequest.getCnpj())).willReturn(farmacia);
        given(medicamentoService.consultarMedicamento(estoqueRequest.getNroRegistro())).willReturn(medicamento);
        given(estoqueRepository.existsById(any(IdEstoque.class))).willReturn(true);
        given(estoqueRepository.getReferenceById(any(IdEstoque.class))).willReturn(estoque);

        assertThrows(QuantidadeInvalidaException.class, () -> estoqueService.venderEstoque(estoqueRequest));
    }

    @Test
    void deveDeletarEstoqueAoVenderEstoque() {
        Long cnpj = 12345L;
        Long nroRegistro = 1234L;
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(cnpj);
        estoqueRequest.setNroRegistro(nroRegistro);
        estoqueRequest.setQuantidade(1);

        Farmacia farmacia = new Farmacia();
        farmacia.setCnpj(cnpj);

        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(nroRegistro);

        Estoque estoque = new Estoque();
        estoque.setQuantidade(1);

        given(farmaciaService.consultarFarmacia(estoqueRequest.getCnpj())).willReturn(farmacia);
        given(medicamentoService.consultarMedicamento(estoqueRequest.getNroRegistro())).willReturn(medicamento);
        given(estoqueRepository.existsById(any(IdEstoque.class))).willReturn(true);
        given(estoqueRepository.getReferenceById(any(IdEstoque.class))).willReturn(estoque);
        estoqueRepository.delete(estoque);

        assertDoesNotThrow(() -> estoqueService.venderEstoque(estoqueRequest));
    }

    @Test
    void deveSalvarNovaQuantidadeNoEstoqueAoVenderEstoque() {
        Long cnpj = 12345L;
        Long nroRegistro = 1234L;
        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(cnpj);
        estoqueRequest.setNroRegistro(nroRegistro);
        estoqueRequest.setQuantidade(1);

        Farmacia farmacia = new Farmacia();
        farmacia.setCnpj(cnpj);

        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(nroRegistro);

        Estoque estoque = new Estoque();
        estoque.setQuantidade(2);

        given(farmaciaService.consultarFarmacia(estoqueRequest.getCnpj())).willReturn(farmacia);
        given(medicamentoService.consultarMedicamento(estoqueRequest.getNroRegistro())).willReturn(medicamento);
        given(estoqueRepository.existsById(any(IdEstoque.class))).willReturn(true);
        given(estoqueRepository.getReferenceById(any(IdEstoque.class))).willReturn(estoque);
        given(estoqueRepository.save(estoque)).willReturn(estoque);

        assertDoesNotThrow(() -> estoqueService.venderEstoque(estoqueRequest));
    }

    @Test
    void deveTransferirEstoque() {
        Long cnpj = 12345L;
        Long nroRegistro = 1234L;

        EstoqueTransferenciaRequest request = new EstoqueTransferenciaRequest();
        request.setQuantidade(1);
        request.setCnpjOrigem(12345L);
        request.setCnpjDestino(23456L);
        request.setNroRegistro(1234L);

        EstoqueRequest estoqueRequest = new EstoqueRequest();
        estoqueRequest.setCnpj(cnpj);
        estoqueRequest.setNroRegistro(nroRegistro);
        estoqueRequest.setQuantidade(1);

        Farmacia farmacia = new Farmacia();
        farmacia.setCnpj(cnpj);

        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(nroRegistro);

        Estoque estoque = new Estoque();
        estoque.setQuantidade(2);

        given(farmaciaService.consultarFarmacia(anyLong())).willReturn(farmacia);
        given(medicamentoService.consultarMedicamento(anyLong())).willReturn(medicamento);
        given(estoqueRepository.existsById(any(IdEstoque.class))).willReturn(true);
        given(estoqueRepository.getReferenceById(any(IdEstoque.class))).willReturn(estoque);
        given(estoqueRepository.save(estoque)).willReturn(estoque);

        List<Estoque> dadosTransferencia = assertDoesNotThrow(() -> estoqueService.transferenciaEstoque(request));
        assertEquals(2, dadosTransferencia.size());
    }

    @Test
    void deveLancarExcecaoQuandoRequestForNuloAoTransferirEstoque() {
        assertThrows(RequisicaoInvalidaException.class, () -> estoqueService.transferenciaEstoque(null));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjOrigemForNuloAoTransferirEstoque() {
        EstoqueTransferenciaRequest request = new EstoqueTransferenciaRequest();
        request.setNroRegistro(1234L);
        request.setQuantidade(1);
        request.setCnpjDestino(23456L);

        assertThrows(CnpjInvalidoException.class, () -> estoqueService.transferenciaEstoque(request));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjOrigemForInexistenteAoTransferirEstoque() {
        EstoqueTransferenciaRequest request = new EstoqueTransferenciaRequest();
        request.setNroRegistro(1234L);
        request.setQuantidade(1);
        request.setCnpjOrigem(12345L);
        request.setCnpjDestino(23456L);

        given(farmaciaService.consultarFarmacia(request.getCnpjOrigem())).willReturn(null);

        assertThrows(RegistroNaoEncontradoException.class, () -> estoqueService.transferenciaEstoque(request));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjDestinoForNuloAoTransferirEstoque() {
        EstoqueTransferenciaRequest request = new EstoqueTransferenciaRequest();
        request.setNroRegistro(1234L);
        request.setQuantidade(1);
        request.setCnpjOrigem(12345L);

        assertThrows(CnpjInvalidoException.class, () -> estoqueService.transferenciaEstoque(request));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjDestinoForInexistenteAoTransferirEstoque() {
        EstoqueTransferenciaRequest request = new EstoqueTransferenciaRequest();
        request.setNroRegistro(1234L);
        request.setQuantidade(1);
        request.setCnpjOrigem(12345L);
        request.setCnpjDestino(23456L);

        given(farmaciaService.consultarFarmacia(request.getCnpjOrigem())).willReturn(new Farmacia());
        given(farmaciaService.consultarFarmacia(request.getCnpjOrigem())).willReturn(null);

        assertThrows(RegistroNaoEncontradoException.class, () -> estoqueService.transferenciaEstoque(request));
    }

    @Test
    void deveLancarExcecaoQuandoNroRegistroForNuloAoTransferirEstoque() {
        assertThrows(RequisicaoInvalidaException.class, () -> estoqueService.transferenciaEstoque(null));
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeForNulaAoTransferirEstoque() {
        EstoqueTransferenciaRequest request = new EstoqueTransferenciaRequest();
        request.setNroRegistro(1234L);
        request.setCnpjOrigem(12345L);
        request.setCnpjDestino(23456L);

        assertThrows(RequisicaoInvalidaException.class, () -> estoqueService.transferenciaEstoque(request));
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeForMenorQueUmAoTransferirEstoque() {
        EstoqueTransferenciaRequest request = new EstoqueTransferenciaRequest();
        request.setNroRegistro(1234L);
        request.setCnpjOrigem(12345L);
        request.setCnpjDestino(23456L);
        request.setQuantidade(0);

        assertThrows(QuantidadeInvalidaException.class, () -> estoqueService.transferenciaEstoque(request));
    }

    @Test
    void deveLancarExcecaoQuandoNroRegistroForInexistenteAoTransferirEstoque() {
        EstoqueTransferenciaRequest request = new EstoqueTransferenciaRequest();
        request.setNroRegistro(1234L);
        request.setQuantidade(1);
        request.setCnpjOrigem(12345L);
        request.setCnpjDestino(23456L);

        given(farmaciaService.consultarFarmacia(request.getCnpjOrigem())).willReturn(new Farmacia());
        given(farmaciaService.consultarFarmacia(request.getCnpjDestino())).willReturn(new Farmacia());
        given(medicamentoService.consultarMedicamento(request.getNroRegistro())).willReturn(null);

        assertThrows(RegistroNaoEncontradoException.class, () -> estoqueService.transferenciaEstoque(request));
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueOrigemForInexistenteAoTransferirEstoque() {
        EstoqueTransferenciaRequest request = new EstoqueTransferenciaRequest();
        request.setNroRegistro(1234L);
        request.setQuantidade(1);
        request.setCnpjOrigem(12345L);
        request.setCnpjDestino(23456L);

        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(1234L);

        given(farmaciaService.consultarFarmacia(request.getCnpjOrigem())).willReturn(new Farmacia());
        given(farmaciaService.consultarFarmacia(request.getCnpjDestino())).willReturn(new Farmacia());
        given(medicamentoService.consultarMedicamento(request.getNroRegistro())).willReturn(medicamento);
        given(estoqueRepository.existsById(any(IdEstoque.class))).willReturn(false);

        assertThrows(RegistroNaoEncontradoException.class, () -> estoqueService.transferenciaEstoque(request));
    }

    @Test
    void deveLancarExcecaoQuandoQtdTransferidaForMaiorQueQtdEstoqueAoTransferirEstoque() {
        EstoqueTransferenciaRequest request = new EstoqueTransferenciaRequest();
        request.setNroRegistro(1234L);
        request.setQuantidade(2);
        request.setCnpjOrigem(12345L);
        request.setCnpjDestino(23456L);

        Medicamento medicamento = new Medicamento();
        medicamento.setNroRegistro(1234L);

        Estoque estoque = new Estoque();
        estoque.setQuantidade(1);

        given(farmaciaService.consultarFarmacia(request.getCnpjOrigem())).willReturn(new Farmacia());
        given(farmaciaService.consultarFarmacia(request.getCnpjDestino())).willReturn(new Farmacia());
        given(medicamentoService.consultarMedicamento(request.getNroRegistro())).willReturn(medicamento);
        given(estoqueRepository.existsById(any(IdEstoque.class))).willReturn(true);
        given(estoqueRepository.getReferenceById(any(IdEstoque.class))).willReturn(estoque);

        assertThrows(QuantidadeInvalidaException.class, () -> estoqueService.transferenciaEstoque(request));
    }

}