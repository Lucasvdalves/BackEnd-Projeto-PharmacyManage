package com.pharmacymanage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "FARMACIAS")
public class Farmacia {

    @Id
    private Long cnpj;

    private String razaoSocial;

    private String nomeFantasia;

    private String email;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Telefone> telefone = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Endereco endereco;

}
