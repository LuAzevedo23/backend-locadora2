package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.entity.Cliente;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {

    @Override
    List<Cliente> findAll();


    @Query("DELETE FROM cliente WHERE id = :id")
    void deletarPorID(@Param("id") Long id);


    @Query("INSERT INTO cliente (nome, rg, cpf, cnh, telefone, email, cnh_vencimento) VALUES (:nome, :rg, :cpf, :cnh, :telefone, :email, :cnhVencimento)")
    void inserirCliente(@Param("nome") String nome,
                        @Param("rg") String rg,
                        @Param("cpf") String cpf,
                        @Param("cnh") String cnh,
                        @Param("telefone") String telefone,
                        @Param("email") String email,
                        @Param("cnhVencimento") LocalDate cnhVencimento);

    @Query("UPDATE cliente SET nome = :nome, rg = :rg, cpf = :cpf, cnh = :cnh, telefone = :telefone, email = :email, cnh_vencimento = :cnhVencimento WHERE id = :id")
    void atualizarCliente(@Param("id") Long id,
                          @Param("nome") String nome,
                          @Param("rg") String rg,
                          @Param("cpf") String cpf,
                          @Param("cnh") String cnh,
                          @Param("telefone") String telefone,
                          @Param("email") String email,
                          @Param("cnhVencimento") LocalDate cnhVencimento);
}