package com.luazevedo.backendlocadora2.controller;

import com.luazevedo.backendlocadora2.entity.Seguradora;
import com.luazevedo.backendlocadora2.exception.AbstractMinhaException;
import com.luazevedo.backendlocadora2.exception.ExceptionResponse;
import com.luazevedo.backendlocadora2.filter.SeguradoraFilter;
import com.luazevedo.backendlocadora2.service.SeguradoraService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seguradora")
public class SeguradoraController implements CrudController<Seguradora> {

    @Autowired
    SeguradoraService service;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarMSeguradoraPorId(id));

    }

    @Override
    @GetMapping
    public ResponseEntity<List<?>> getAll(@RequestParam Map<String, String> parametros) {
        SeguradoraFilter filtro = new SeguradoraFilter(parametros);
        return ResponseEntity.status(HttpStatus.OK).body(service.obterTodasSeguradoras(filtro));

    }

    @Override
    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Seguradora seguradora) {
        service.salvarSeguradora(seguradora);
        return ResponseEntity.status(HttpStatus.CREATED).body("Seguradora salva com sucesso");
    }

    @Override
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Seguradora seguradora) {
        try {
            service.atualizarSeguradora(seguradora);
            return ResponseEntity.status(HttpStatus.OK).body("Seguradora atualizada com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar Seguradora: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            service.deletarSeguradora(id);

            return ResponseEntity.status(HttpStatus.OK).body("Seguradora excluído com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao excluir Seguradora");
        }
    }

    @ExceptionHandler(AbstractMinhaException.class)
    public ResponseEntity<ExceptionResponse> handleAbstractMinhaException(AbstractMinhaException ex, HttpServletRequest request) throws IOException {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }
}







