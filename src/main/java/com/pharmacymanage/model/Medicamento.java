package com.pharmacymanage.model;

import com.pharmacymanage.model.enums.TipoMedicamentoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "MEDICAMENTOS")
public class Medicamento {

    @Id
    private Long nroRegistro;

    private String nome;

    private String laboratorio;

    private String dosagem;

    private String descricao;

    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    private TipoMedicamentoEnum tipo;

}
