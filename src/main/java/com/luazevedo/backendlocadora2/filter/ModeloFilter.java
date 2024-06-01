package com.luazevedo.backendlocadora2.filter;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.Map;

@Data
@NoArgsConstructor
@FieldNameConstants
public class ModeloFilter {
    private Long id;
    private String modeloNome;
    private Long fabricanteId;

    public ModeloFilter(Map<String, String> parametros) {
        parametros.forEach((chave, valor) -> {
            try {
                switch (chave) {
                    case Fields.id:
                        id = Long.valueOf(valor);
                        break;
                    case Fields.modeloNome:
                        modeloNome = valor;
                        break;
                    case Fields.fabricanteId:
                        fabricanteId = Long.valueOf(valor);
                        break;
                    default:
                        throw new IllegalArgumentException("Parâmetro desconhecido: " + chave);
                }
            } catch (NumberFormatException e) {
                System.err.println("Erro ao converter valor: " + valor + " para o campo: " + chave);
            }
        });
    }
}