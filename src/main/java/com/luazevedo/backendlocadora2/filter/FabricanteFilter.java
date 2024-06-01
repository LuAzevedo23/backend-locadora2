package com.luazevedo.backendlocadora2.filter;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.Map;

@Data
@NoArgsConstructor
@FieldNameConstants
public class FabricanteFilter {

    private Long id;
    private String fabricanteNome;

    public FabricanteFilter(Map<String, String> parametros) {
        parametros.forEach((chave, valor) -> {
            switch (chave) {
                case Fields.fabricanteNome:
                    fabricanteNome = valor;
                    break;
                default:
                    throw new IllegalArgumentException("Parâmetro desconhecido: " + chave);
            }
        });
    }

    public boolean isVazio() {
        return fabricanteNome == null || fabricanteNome.isEmpty();
    }
}