package com.pharmacymanage.repository;

import com.pharmacymanage.model.Estoque;
import com.pharmacymanage.model.IdEstoque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, IdEstoque> {

    List<Estoque> findAllByCnpj(Long cnpj);

    Page<Estoque> findAllByCnpj(Long cnpj, Pageable pageable);

}