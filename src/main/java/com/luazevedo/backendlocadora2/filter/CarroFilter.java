package com.luazevedo.backendlocadora2.filter;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.Map;

@Data
@NoArgsConstructor
@FieldNameConstants
public class CarroFilter {

    private Long idModelo;
    private Long idFabricante;
    private String placa;
    private String cor;
    private Boolean disponivel;
    private Integer ano;
    private Double valorLocacao;

    public CarroFilter(Map<String, String> parametros) {
        parametros.forEach((chave, valor) -> {
            try {
                switch (chave) {
                    case Fields.idModelo:
                        idModelo = Long.valueOf(valor);
                        break;
                    case Fields.idFabricante:
                        idFabricante = Long.valueOf(valor);
                        break;
                    case Fields.cor:
                        cor = valor;
                        break;
                    case Fields.placa:
                        placa = valor;
                        break;
                    case Fields.disponivel:
                        disponivel = Boolean.valueOf(valor);
                        break;
                    case Fields.ano:
                        ano = Integer.valueOf(valor);
                        break;
                    case Fields.valorLocacao:
                        valorLocacao = Double.valueOf(valor);
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
        return idModelo == null &&
                idFabricante == null &&
                (placa == null || placa.isEmpty()) &&
                (cor == null || cor.isEmpty()) &&
                disponivel == null &&
                ano == null &&
                valorLocacao == null;
    }
}