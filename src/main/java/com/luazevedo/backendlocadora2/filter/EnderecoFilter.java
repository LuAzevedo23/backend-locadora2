package com.luazevedo.backendlocadora2.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoFilter {

    private Long id;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    public EnderecoFilter(Map<String, String> parametros) {
        parametros.forEach((chave, valor) -> {
            switch (chave) {
                case "id":
                    id = Long.valueOf(valor);
                    break;
                case "rua":
                    rua = valor;
                    break;
                case "numero":
                    numero = valor;
                    break;
                case "bairro":
                    bairro = valor;
                    break;
                case "cidade":
                    cidade = valor;
                    break;
                case "estado":
                    estado = valor;
                    break;
                case "cep":
                    cep = valor;
                    break;
                default:
                    throw new IllegalArgumentException("Parâmetro desconhecido: " + chave);
            }
        });
    }

    public boolean isVazio() {
        return id == null && (rua == null || rua.isEmpty()) &&
                (numero == null || numero.isEmpty()) &&
                (bairro == null || bairro.isEmpty()) &&
                (cidade == null || cidade.isEmpty()) &&
                (estado == null || estado.isEmpty()) &&
                (cep == null || cep.isEmpty());
    }
}