package com.luazevedo.backendlocadora2.controller;

import org.springframework.web.bind.annotation.PathVariable;
import com.luazevedo.backendlocadora2.exception.AbstractMinhaException;
import com.luazevedo.backendlocadora2.dto.ClienteDTO;
import com.luazevedo.backendlocadora2.entity.Cliente;
import com.luazevedo.backendlocadora2.exception.ExceptionResponse;
import com.luazevedo.backendlocadora2.filter.ClienteFilter;
import com.luazevedo.backendlocadora2.service.ClienteService;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController implements CrudController<Cliente> {

    @Autowired
    ClienteService service;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        ClienteDTO cliente = service.obterCliente(id);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<?>> getAll(@RequestParam Map<String, String> parametros) {
        ClienteFilter filtro = new ClienteFilter(parametros);
        List<ClienteDTO> clientes = service.obterTodosClientes(filtro);
        return ResponseEntity.status(HttpStatus.OK).body(clientes);
    }

    @Override
    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Cliente cliente) {
        service.inserirCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente salvo com sucesso");
    }

    @Override
    @PutMapping
    public ResponseEntity<?> update(@RequestBody Cliente cliente) {
        try {
            service.atualizarCliente(cliente);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente atualizado com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar Cliente: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            service.deletarCliente(id);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente excluído com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao excluir Cliente: " + ex.getMessage());
        }
    }

    @ExceptionHandler(AbstractMinhaException.class)
    public ResponseEntity<ExceptionResponse> handleAbstractMinhaException(AbstractMinhaException ex, HttpServletRequest request) throws IOException {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}