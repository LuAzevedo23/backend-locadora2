package com.luazevedo.backendlocadora2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    private Long id;
    private EnderecoDTO endereco;
    private SeguradoraDTO seguradoraDTO;
    private CarroDTO carroDTO;
    private String nome;
    private String rg;
    private String cpf;
    private String cnh;
    private String telefone;
    private String email;
    private LocalDate cnhVencimento;

}
