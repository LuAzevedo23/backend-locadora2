package com.luazevedo.backendlocadora2.controller;

import com.luazevedo.backendlocadora2.entity.Fabricante;
import com.luazevedo.backendlocadora2.exception.AbstractMinhaException;
import com.luazevedo.backendlocadora2.exception.ExceptionResponse;
import com.luazevedo.backendlocadora2.filter.FabricanteFilter;
import com.luazevedo.backendlocadora2.service.FabricanteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fabricante")
public class FabricanteController implements CrudController<Fabricante> {

    @Autowired
    FabricanteService service;


    @Override
    @GetMapping ("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarFabricantePorId(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<?>> getAll(@RequestParam Map<String, String> parametros) {
        FabricanteFilter filtro = new FabricanteFilter(parametros);
        return ResponseEntity.status(HttpStatus.OK).body(service.obterTodosFabricantes(filtro));
    }

    @Override
    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Fabricante fabricante) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Fabricante salvo com sucesso");
    }

    @Override
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Fabricante fabricante) {
        try {
            service.atualizarFabricante(fabricante);

            return ResponseEntity.status(HttpStatus.CREATED).body("Fabricante atualizado com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar Fabricante: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            service.deletarFabricante(id);

            return ResponseEntity.status(HttpStatus.CREATED).body("Fabricante excluido com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao excluir Fabricante");
        }
    }

    @ExceptionHandler(AbstractMinhaException.class)
    public ResponseEntity<ExceptionResponse> handleAbstractMinhaException(AbstractMinhaException ex, HttpServletRequest request) throws IOException {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}









