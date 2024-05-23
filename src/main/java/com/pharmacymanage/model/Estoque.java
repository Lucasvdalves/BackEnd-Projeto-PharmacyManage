package com.pharmacymanage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "ESTOQUES")
@IdClass(IdEstoque.class)
public class Estoque {

    @Id
    private Long cnpj;

    @Id
    private Long nroRegistro;

    private Integer quantidade;

    private LocalDateTime dataAtualizacao;

}