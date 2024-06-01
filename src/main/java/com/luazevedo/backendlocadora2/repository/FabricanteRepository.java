package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.entity.Fabricante;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FabricanteRepository extends CrudRepository<Fabricante, Long> {

    @Override
    List<Fabricante> findAll();

    @Query("DELETE FROM fabricante WHERE id = :id")
    void deletarPorID(@Param("id") Long id);

    @Query("INSERT INTO fabricante (fabricante_nome) VALUES (:fabricante_nome)")
    void inserirFabricante(@Param("fabricante_nome") String fabricante_nome);

    @Query("UPDATE fabricante SET fabricante_nome = :fabricante_nome WHERE id = :id")
    void atualizarFabricante(@Param("fabricante_nome") String fabricante_nome,
                             @Param("id") Long id);
}
