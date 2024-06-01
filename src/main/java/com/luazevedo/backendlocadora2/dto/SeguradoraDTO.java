package com.luazevedo.backendlocadora2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeguradoraDTO {
    private Long id;
    private EnderecoDTO endereco;
    private ClienteDTO clienteDTO;
    private String nome;
    private String cnpj;
    private String telefone;
    private String email;
    private Double valor;

}
