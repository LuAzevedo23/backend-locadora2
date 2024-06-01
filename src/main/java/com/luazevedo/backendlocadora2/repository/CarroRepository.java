package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.entity.Carro;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarroRepository extends CrudRepository<Carro, Long> {

    @Override
    List<Carro> findAll();

    @Query("DELETE FROM carro WHERE id = :id")
    void deletarPorID(@Param("id") Long id);

    @Query("INSERT INTO carro (placa, ano, disponivel, valorLocacao, cor) VALUES (:placa, :ano, :disponivel, :valorLocacao, :cor)")
    void inserirCarro(@Param("placa") String placa,
                      @Param("ano") int ano,
                      @Param("disponivel") boolean disponivel,
                      @Param("valorLocacao") Double valorLocacao,
                      @Param("cor") String cor);

    @Query("UPDATE carro SET placa = :placa, ano = :ano, disponivel = :disponivel, valorLocacao = :valorLocacao, cor = :cor WHERE id = :id")
    void atualizarCarro(@Param("placa") String placa,
                        @Param("ano") int ano,
                        @Param("disponivel") boolean disponivel,
                        @Param("valorLocacao") Double valorLocacao,
                        @Param("cor") String cor,
                        @Param("id") Long id);
}

