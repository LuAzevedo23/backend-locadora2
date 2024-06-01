package com.luazevedo.backendlocadora2.filter;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Data
@NoArgsConstructor
@FieldNameConstants
public class ClienteFilter {

    private Long idEndereco;
    private String nome;
    private String rg;
    private String cpf;
    private String cnh;
    private String telefone;
    private String email;
    private LocalDate cnhVencimento;

    public ClienteFilter(Map<String, String> parametros) {
        parametros.forEach((chave, valor) -> {
            switch (chave) {
                case Fields.idEndereco:
                    idEndereco = Long.valueOf(valor);
                    break;
                case Fields.nome:
                    nome = valor;
                    break;
                case Fields.rg:
                    rg = valor;
                    break;
                case Fields.cpf:
                    cpf = valor;
                    break;
                case Fields.cnh:
                    cnh = valor;
                    break;
                case Fields.telefone:
                    telefone = valor;
                    break;
                case Fields.email:
                    email = valor;
                    break;
                case Fields.cnhVencimento:
                    try {
                        cnhVencimento = LocalDate.parse(valor);
                    } catch (DateTimeParseException e) {
                        System.err.println("Erro ao parsear data de vencimento da CNH: " + valor);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Parâmetro desconhecido: " + chave);
            }
        });
    }

    public boolean isVazio() {
        return idEndereco == null && (nome == null || nome.isEmpty()) &&
                (rg == null || rg.isEmpty()) && (cpf == null || cpf.isEmpty()) &&
                (cnh == null || cnh.isEmpty()) && (telefone == null || telefone.isEmpty()) &&
                (email == null || email.isEmpty()) && cnhVencimento == null;
    }
}