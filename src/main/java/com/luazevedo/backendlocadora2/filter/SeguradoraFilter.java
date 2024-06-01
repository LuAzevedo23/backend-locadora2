package com.luazevedo.backendlocadora2.filter;

import com.luazevedo.backendlocadora2.entity.Endereco;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeguradoraFilter {
    private Long id;
    private String nome;
    private String cnpj;
    private String telefone;
    private String email;
    private Double valor;
    private Endereco endereco;

    public SeguradoraFilter(Map<String, String> parametros) {
        parametros.forEach((chave, valor) -> {
            try {
                switch (chave) {
                    case "id":
                        id = Long.valueOf(valor);
                        break;
                    case "nome":
                        nome = valor;
                        break;
                    case "cnpj":
                        cnpj = valor;
                        break;
                    case "telefone":
                        telefone = valor;
                        break;
                    case "email":
                        email = valor;
                        break;
                    case "valor":
                        this.valor = Double.valueOf(valor);
                        break;
                    case "endereco":
                        break;
                    default:
                        throw new IllegalArgumentException("Parâmetro desconhecido: " + chave);
                }
            } catch (NumberFormatException e) {
                System.err.println("Erro ao converter valor: " + valor + " para o campo: " + chave);
            }
        });
    }

    public boolean isVazio() {
        return id == null &&
                (nome == null || nome.isEmpty()) &&
                (cnpj == null || cnpj.isEmpty()) &&
                (telefone == null || telefone.isEmpty()) &&
                (email == null || email.isEmpty()) &&
                valor == null &&
                endereco == null;
    }
}