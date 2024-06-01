package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.entity.Seguradora;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeguradoraRepository extends ListCrudRepository<Seguradora, Long> {
    @Override
    List<Seguradora> findAll();

    @Modifying
    @Query("DELETE FROM carro WHERE id = :x")
    void deletarPorID(@Param("x") Long id);

    @Query("INSERT INTO seguradora (nome, cnpj, telefone, email, valor) VALUES (:nome, :cnpj, :telefone, :email, :valor)")
    void inserirSeguradora(@Param("nome") String nome,
                           @Param("cnpj") String cnpj,
                           @Param("telefone") String telefone,
                           @Param("email") String email,
                           @Param("valor") Double valor);

    @Query("UPADATE seguradora SET nome = :nome, cnpj = :cnpj, telefone = :telefone, email = :email, valor = :valor WHERE id = :id")
    void atualizarSeguradora(@Param("nome") String nome,
                             @Param("cnpj") String cnpj,
                             @Param("telefone") String telefone,
                             @Param("email") String email,
                             @Param("valor") Double valor,
                             @Param("id") Long id);
}




