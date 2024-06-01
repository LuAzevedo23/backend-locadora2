package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.entity.Modelo;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeloRepository extends CrudRepository<Modelo, Long> {

    @Override
    List<Modelo> findAll();

    @Query("DELETE FROM modelo WHERE id = :id")
    void deletarPorID(@Param("id") Long id);

    @Query("INSERT INTO modelo (modelo_nome) VALUES (:modelo_nome)")
    void inserirModelo(@Param("modelo_nome") String modelo_nome);

    @Query("UPDATE modelo SET modelo_nome = :modelo_nome WHERE id = :id")
    void atualizarModelo(@Param("modelo_nome") String modelo_nome,
                         @Param("id") Long id);
}