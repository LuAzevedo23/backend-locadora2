package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.entity.Fabricante;
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
public class FabricanteRepositoryAbstracao extends AbstractRepository<Fabricante> {
    private final JdbcClient jdbcClient;
    private final RowMapper<Fabricante> fabricanteMapper;

    @Autowired
    public FabricanteRepositoryAbstracao(JdbcClient jdbcClient) {
        super(jdbcClient);
        this.jdbcClient = jdbcClient;
        this.fabricanteMapper = this::mapRowToFabricante;
    }

    @Override
    public List<Fabricante> getAll(Object filtro) {
        return List.of();
    }

    private Fabricante mapRowToFabricante(ResultSet rs, int rowNum) throws SQLException {
        Fabricante fabricante = new Fabricante();
        fabricante.setId(rs.getLong("id"));
        fabricante.setFabricante_nome(rs.getString("fabricante_nome"));
        return fabricante;
    }

    @Override
    public Optional<Fabricante> getByID(Long id) {
        return jdbcClient
                .sql("SELECT * FROM fabricante WHERE id = :idFabricante")
                .param("idFabricante", id)
                .query(fabricanteMapper)
                .optional();
    }

    @Override
    public boolean existsByID(Long id) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM fabricante WHERE id = :idFabricante)")
                .param("idFabricante", id)
                .query(Boolean.class)
                .single();
    }

    @Override
    public Integer insert(Fabricante fabricante) {
        String querySql = "INSERT INTO public.fabricante (fabricante_nome) VALUES (:fabricante_nome);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(querySql)
                .param("fabricante_nome", fabricante.getFabricante_nome())
                .update(keyHolder, "id");
        return keyHolder.getKeyAs(Integer.class);
    }

    @Override
    public Integer update(Fabricante fabricante) {
        String querySql = """
                UPDATE public.fabricante
                SET fabricante_nome = :fabricante_nome
                WHERE id = :idFabricante;
                """;
        return jdbcClient.sql(querySql)
                .param("idFabricante", fabricante.getId())
                .param("fabricante_nome", fabricante.getFabricante_nome())
                .update();
    }

    public Integer delete(Long id) {
        String querySql = "DELETE FROM public.fabricante WHERE id = :idFabricante;";
        return jdbcClient.sql(querySql)
                .param("idFabricante", id)
                .update();
    }
}









