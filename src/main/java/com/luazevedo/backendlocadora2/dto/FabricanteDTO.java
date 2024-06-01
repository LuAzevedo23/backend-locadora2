package com.luazevedo.backendlocadora2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FabricanteDTO {
    private Long id;
    private ModeloDTO modeloDTO;
    private String fabricante_nome;
}
