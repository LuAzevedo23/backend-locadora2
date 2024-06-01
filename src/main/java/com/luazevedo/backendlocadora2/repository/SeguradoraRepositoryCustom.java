package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.entity.Seguradora;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


@Repository
public class SeguradoraRepositoryCustom {
    @Autowired
    private NamedParameterJdbcTemplate jdbcClient;

    public Long criarSeguradora(Seguradora seguradora) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("nome", seguradora.getNome())
                .addValue("cnpj", seguradora.getCnpj())
                .addValue("telefone", seguradora.getTelefone())
                .addValue("email", seguradora.getEmail())
                .addValue("valor", seguradora.getValor())
                .addValue("endereco", seguradora.getEndereco());
        jdbcClient.update(
                "INSERT INTO seguradora (nome, cnpj, telefone, email, valor, endereco) VALUES (:nome, :cnpj, :telefone, :email, :valor, :endereco)",
                params, keyHolder, new String[]{"id"});

        return keyHolder.getKey().longValue();
    }

    public Integer atualizarSeguradora(Seguradora seguradora) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", seguradora.getId())
                .addValue("nome", seguradora.getNome())
                .addValue("cnpj", seguradora.getCnpj())
                .addValue("telefone", seguradora.getTelefone())
                .addValue("email", seguradora.getEmail())
                .addValue("valor", seguradora.getValor())
                .addValue("endereco", seguradora.getEndereco());

        return jdbcClient.update(
                "UPDATE seguradora SET nome=:nome, cnpj=:cnpj, telefone=:telefone, email=:email, valor=:valor, endereco=:endereco WHERE id=:id",
                params
        );
    }

    public int deletarSeguradora(Long idSeguradora) {
        SqlParameterSource params = new MapSqlParameterSource("id", idSeguradora);

        return jdbcClient.update(
                "DELETE FROM seguradora WHERE id=:id",
                params);
    }

}
