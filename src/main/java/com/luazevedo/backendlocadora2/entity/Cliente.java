package com.luazevedo.backendlocadora2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    @Id
    private Long id;
    private String nome;
    private String rg;
    private String cpf;
    private String cnh;
    private String telefone;
    private String email;
    private LocalDate cnhVencimento;
    @Column("id")
    private Endereco endereco;
}
