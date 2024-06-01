package com.luazevedo.backendlocadora2.controller;

import com.luazevedo.backendlocadora2.entity.Carro;
import com.luazevedo.backendlocadora2.exception.AbstractMinhaException;
import com.luazevedo.backendlocadora2.exception.ExceptionResponse;
import com.luazevedo.backendlocadora2.filter.CarroFilter;
import com.luazevedo.backendlocadora2.service.CarroService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carro")
public class CarroController implements CrudController<Carro> {

    @Autowired
    CarroService service;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.obterTodosCarros());

    }

    @Override
    @GetMapping
    public ResponseEntity<List<?>> getAll(@RequestParam Map<String, String> parametros) {
        CarroFilter filtro = new CarroFilter(parametros);
        return ResponseEntity.status(HttpStatus.OK).body(service.obterTodosCarros(filtro));

    }

    @Override
    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Carro carro) {
        service.salvarCarro(carro);
        return ResponseEntity.status(HttpStatus.CREATED).body("Carro salvo com sucesso");
    }

    @Override
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Carro carro) {
        try {
            service.atualizarCarro(carro);
            return ResponseEntity.status(HttpStatus.OK).body("Carro atualizado com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar Carro: " + ex.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            service.deletarCarro(id);

            return ResponseEntity.status(HttpStatus.OK).body("Carro excluído com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao excluir Carro");
        }
    }

    @ExceptionHandler(AbstractMinhaException.class)
    public ResponseEntity<ExceptionResponse> handleAbstractMinhaException(AbstractMinhaException ex, HttpServletRequest request) throws IOException {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }
}







