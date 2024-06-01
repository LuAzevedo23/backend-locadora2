package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.entity.Carro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CarroRepositoryAbstracao extends AbstractRepository<Carro> {
    private final JdbcClient jdbcClient;
    private final RowMapper<Carro> carroMapper;

    @Autowired
    public CarroRepositoryAbstracao(JdbcClient jdbcClient) {
        super(jdbcClient);
        this.jdbcClient = jdbcClient;
        this.carroMapper = this::mapRowToCarro;
    }

    @Override
    public List<Carro> getAll(Object filtro) {
        return List.of();
    }

    private Carro mapRowToCarro(ResultSet rs, int rowNum) throws SQLException {
        Carro carro = new Carro();
        carro.setId(rs.getLong("id"));
        carro.setPlaca(rs.getString("placa"));
        carro.setCor(rs.getString("cor"));
        carro.setDisponivel(rs.getBoolean("disponivel"));
        carro.setValorlocacao(rs.getDouble("valorlocacao"));
        carro.setAno(rs.getString("ano"));
        return carro;
    }

    public Optional<Carro> getByID(Long id) {
        return jdbcClient
                .sql("SELECT * FROM carro WHERE id = :idCarro")
                .param("idCarro", id)
                .query(carroMapper)
                .optional();
    }

    public boolean existsByID(Long id) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM carro WHERE id = :idCarro)")
                .param("idCarro", id)
                .query(Boolean.class)
                .single();
    }

    @Override
    public Integer insert(Carro carro) {
        String querySql = "INSERT INTO public.carro (id_modelo, placa, cor, disponivel, ano, valorlocacao) " +
                "VALUES (:idModelo, :placa, :cor, :disponivel, :ano, :valorLocacao);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(querySql)
                .param("idModelo", carro.getModelo())
                .param("placa", carro.getPlaca())
                .param("cor", carro.getCor())
                .param("disponivel", carro.getDisponivel())
                .param("ano", carro.getAno())
                .param("valorLocacao", carro.getValorlocacao())
                .update(keyHolder, "id");

        return keyHolder.getKeyAs(Integer.class);
    }

    public Integer update(Carro carro) {
        String querySql = """
                UPDATE public.carro
                SET id_modelo = :idModelo, placa = :placa, cor = :cor, disponivel = :disponivel, ano = :ano, valorlocacao = :valorLocacao
                WHERE id = :idCarro;
                """;

        return jdbcClient.sql(querySql)
                .param("idCarro", carro.getId())
                .param("idModelo", carro.getModelo())
                .param("placa", carro.getPlaca())
                .param("cor", carro.getCor())
                .param("disponivel", carro.getDisponivel())
                .param("ano", carro.getAno())
                .param("valorLocacao", carro.getValorlocacao())
                .update();
    }

    public Integer delete(Long id) {
        String querySql = "DELETE FROM public.carro WHERE id = :idCarro;";

        return jdbcClient.sql(querySql)
                .param("idCarro", id)
                .update();
    }
}