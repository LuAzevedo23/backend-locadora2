package com.luazevedo.backendlocadora2.controller;

import com.luazevedo.backendlocadora2.entity.Modelo;
import com.luazevedo.backendlocadora2.exception.AbstractMinhaException;
import com.luazevedo.backendlocadora2.exception.ExceptionResponse;
import com.luazevedo.backendlocadora2.filter.ModeloFilter;
import com.luazevedo.backendlocadora2.service.ModeloService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/modelo")
public class ModeloController implements CrudController<Modelo> {

    @Autowired
    ModeloService service;


    @Override
    @GetMapping ("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarModeloPorId(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<?>> getAll(@RequestParam Map<String, String> parametros) {
        ModeloFilter filtro = new ModeloFilter(parametros);
        return ResponseEntity.status(HttpStatus.OK).body(service.obterTodosModelos(filtro));
    }

    @Override
    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Modelo modelo) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body("Modelo salvo com sucesso");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao salvar Modelo: " + ex.getMessage());
        }
    }

    @Override
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Modelo modelo) {
        try {
            service.atualizarModelo(modelo);
            return ResponseEntity.status(HttpStatus.OK).body("Modelo atualizado com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar Modelo: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            service.deletarModelo(id);
            return ResponseEntity.status(HttpStatus.OK).body("Modelo excluido com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao excluir Modelo: " + ex.getMessage());
        }
    }

    @ExceptionHandler(AbstractMinhaException.class)
    public ResponseEntity<ExceptionResponse> handleAbstractMinhaException(AbstractMinhaException ex, HttpServletRequest request) throws IOException {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}









