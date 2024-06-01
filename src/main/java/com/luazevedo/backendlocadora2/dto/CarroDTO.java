package com.luazevedo.backendlocadora2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarroDTO {
    private Long id;
    private ModeloDTO modelo;
    private String ano;
    private String cor;
    private String placa;
    private Double valorlocacao;
    private Boolean disponivel;

}
