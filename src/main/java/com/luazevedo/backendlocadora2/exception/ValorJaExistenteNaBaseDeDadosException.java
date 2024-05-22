package com.luazevedo.backendlocadora2.exception;

public class ValorJaExistenteNaBaseDeDadosException extends AbstractMinhaException {
    public ValorJaExistenteNaBaseDeDadosException() {
        super("Valor Já existente na Base de Dados");
    }
}
