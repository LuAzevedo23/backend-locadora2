package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.entity.Fabricante;
import com.luazevedo.backendlocadora2.entity.Modelo;
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
public class ModeloRepositoryAbstracao extends AbstractRepository<Modelo> {
    private final JdbcClient jdbcClient;
    private final RowMapper<Modelo> modeloMapper;

    @Autowired
    public ModeloRepositoryAbstracao(JdbcClient jdbcClient) {
        super(jdbcClient);
        this.jdbcClient = jdbcClient;
        this.modeloMapper = this::mapRowToModelo;
    }

    @Override
    public List<Modelo> getAll(Object filtro) {
        return List.of();
    }

    private Modelo mapRowToModelo(ResultSet rs, int rowNum) throws SQLException {
        Modelo modelo = new Modelo();
        modelo.setId(rs.getLong("id"));
        modelo.setModelo_nome(rs.getString("modelo_nome"));
        return modelo;
    }

    @Override
    public Optional<Modelo> getByID(Long id) {
        return jdbcClient
                .sql("SELECT * FROM modelo WHERE id = :idModelo")
                .param("idModelo", id)
                .query(modeloMapper)
                .optional();
    }

    @Override
    public boolean existsByID(Long id) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM modelo WHERE id = :idModelo)")
                .param("idModelo", id)
                .query(Boolean.class)
                .single();
    }

    @Override
    public Integer insert(Modelo modelo) {
        String querySql = "INSERT INTO public.modelo (modelo_nome) VALUES (:modelo_nome);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(querySql)
                .param("modelo_nome", modelo.getModelo_nome())
                .update(keyHolder, "id");
        return keyHolder.getKeyAs(Integer.class);
    }

    @Override
    public Integer update(Modelo modelo) {
        String querySql = """
                UPDATE public.modelo
                SET modelo_nome = :modelo_nome
                WHERE id = :idModelo;
                """;
        return jdbcClient.sql(querySql)
                .param("idModelo", modelo.getId())
                .param("modelo_nome", modelo.getModelo_nome())
                .update();
    }

    public Integer delete(Long id) {
        String querySql = "DELETE FROM public.modelo WHERE id = :idModelo;";
        return jdbcClient.sql(querySql)
                .param("idmodelo", id)
                .update();
    }
}









