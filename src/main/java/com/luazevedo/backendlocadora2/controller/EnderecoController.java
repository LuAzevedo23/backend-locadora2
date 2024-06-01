package com.luazevedo.backendlocadora2.controller;

import com.luazevedo.backendlocadora2.entity.Endereco;
import com.luazevedo.backendlocadora2.exception.AbstractMinhaException;
import com.luazevedo.backendlocadora2.exception.ExceptionResponse;
import com.luazevedo.backendlocadora2.filter.EnderecoFilter;
import com.luazevedo.backendlocadora2.service.EnderecoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/endereco")
public class EnderecoController implements CrudController<Endereco> {

    @Autowired
    EnderecoService service;


    @Override
    @GetMapping ("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.obterTodosEnderecos());
    }

    @Override
    @GetMapping
    public ResponseEntity<List<?>> getAll(@RequestParam Map<String, String> parametros) {
        EnderecoFilter filtro = new EnderecoFilter(parametros);
        return ResponseEntity.status(HttpStatus.OK).body(service.obterTodosEnderecos(filtro));
    }

    @Override
    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Endereco endereco) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Endereco salvo com sucesso");
    }

    @Override
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Endereco endereco) {
        try {
            service.atualizarEndereco(endereco);

            return ResponseEntity.status(HttpStatus.CREATED).body("Endereco atualizado com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar Endereco: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            service.deletarEndereco(id);

            return ResponseEntity.status(HttpStatus.CREATED).body("Endereço excluido com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao excluir Endereço");
        }
    }

    @ExceptionHandler(AbstractMinhaException.class)
    public ResponseEntity<ExceptionResponse> handleAbstractMinhaException(AbstractMinhaException ex, HttpServletRequest request) throws IOException {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}









