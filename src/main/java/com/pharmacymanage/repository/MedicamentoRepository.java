package com.pharmacymanage.repository;

import com.pharmacymanage.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository  extends JpaRepository<Medicamento, Long> {

}
