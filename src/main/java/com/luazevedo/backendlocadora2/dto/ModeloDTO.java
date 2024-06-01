package com.luazevedo.backendlocadora2.dto;

import com.luazevedo.backendlocadora2.entity.Fabricante;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModeloDTO {
    private int id;
    private CarroDTO carroDTO;
    private String modelo_nome;

}
