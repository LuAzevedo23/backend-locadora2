package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.entity.Endereco;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends CrudRepository<Endereco, Long> {

    @Override
    List<Endereco> findAll();

    @Query("DELETE FROM endereco WHERE id = :id")
    void deletarPorID(@Param("id") Long id);

    @Query("INSERT INTO endereco (rua, numero, bairro, cidade, estado, cep) VALUES (:rua, :numero, :bairro, :cidade, :estado, :cep)")
    void inserirEndereco(@Param("rua") String rua,
                         @Param("numero") String numero,
                         @Param("bairro") String bairro,
                         @Param("cidade") String cidade,
                         @Param("estado") String estado,
                         @Param("cep") String cep);

    @Query("UPDATE endereco SET rua = :rua, numero = :numero, bairro = :bairro, cidade = :cidade, estado = :estado, cep = :cep WHERE id = :id")
    void atualizarEndereco(@Param("rua") String rua,
                           @Param("numero") String numero,
                           @Param("bairro") String bairro,
                           @Param("cidade") String cidade,
                           @Param("estado") String estado,
                           @Param("cep") String cep,
                           @Param("id") Long id);
}