package com.luazevedo.backendlocadora2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public interface CrudController<ENTITY> {

@GetMapping ("/{id}")
   ResponseEntity<?> get(@PathVariable(value = "id") Long id);

  ResponseEntity<List<?>> getAll(@RequestParam Map<String,String> parametros);

  @PostMapping
  ResponseEntity<?> insert(@RequestBody ENTITY entity);

   @PatchMapping
  ResponseEntity<?> update(@RequestBody ENTITY entity);

}
