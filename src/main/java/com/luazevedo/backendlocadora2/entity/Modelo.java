package com.luazevedo.backendlocadora2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Modelo {
    @Id
    private Long id;
    private String modelo_nome;
    @Column("id")
    private Fabricante fabricante;
}
