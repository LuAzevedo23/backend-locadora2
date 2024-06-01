package com.luazevedo.backendlocadora2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seguradora {
    @Id
    private Long id;
    private String nome;
    private String cnpj;
    private String telefone;
    private String email;
    private Double valor;
    @Column("id")
    private Endereco endereco;

}
