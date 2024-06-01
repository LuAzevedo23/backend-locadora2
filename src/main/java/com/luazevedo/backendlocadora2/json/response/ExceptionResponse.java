package com.luazevedo.backendlocadora2.json.response;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Calendar;

@Getter
@FieldDefaults(makeFinal = true)
public class ExceptionResponse {
    String error;
    String url;
    Instant dataHora;

    public ExceptionResponse(Exception ex, String url) {
        error = ex.getMessage();
        dataHora = Calendar.getInstance().toInstant();
        this.url = url;
    }
}
