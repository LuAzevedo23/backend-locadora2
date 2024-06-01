package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.dto.FabricanteDTO;
import com.luazevedo.backendlocadora2.entity.Fabricante;
import com.luazevedo.backendlocadora2.filter.FabricanteFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FabricanteRepositoryCustom {

    @Autowired
    private JdbcClient jdbcClient;

    RowMapper<FabricanteDTO> fabricanteDTOMapper = (rs, rowNum) -> {
        FabricanteDTO fabricanteDTO = new FabricanteDTO();
        fabricanteDTO.setId(rs.getLong("id"));
        fabricanteDTO.setFabricante_nome(rs.getString("fabricante_nome"));

        return fabricanteDTO;
    };

    public List<FabricanteDTO> buscarTodosFabricantes(FabricanteFilter filtro) {
        StringJoiner where = new StringJoiner(" AND ");
        Map<String, Object> params = new HashMap<>();

        if (filtro.getFabricanteNome() != null) {
            where.add("fabricante_nome = :fabricanteNome");
            params.put("fabricanteNome", filtro.getFabricanteNome());
        }

        String sql = """
            SELECT id, fabricante_nome
            FROM fabricante
        """;

        if (!params.isEmpty()) {
            sql += " WHERE " + where.toString();
        }

        return jdbcClient.sql(sql)
                .params(params)
                .query(fabricanteDTOMapper)
                .list();
    }

    public Optional<FabricanteDTO> buscarFabricantePorId(Long idFabricante) {
        String sql = """
            SELECT id, fabricante_nome
            FROM fabricante
            WHERE id = :idFabricante
        """;
        return jdbcClient.sql(sql)
                .param("idFabricante", idFabricante)
                .query(fabricanteDTOMapper)
                .optional();
    }

    public boolean checarExistenciaFabricantePorId(Long idFabricante) {
        String sql = """
            SELECT EXISTS(SELECT 1
                          FROM fabricante
                          WHERE id = :idFabricante)
        """;
        return jdbcClient.sql(sql)
                .param("idFabricante", idFabricante)
                .query(Boolean.class)
                .single();
    }

    public Integer inserirFabricante(Fabricante fabricante) {
        String querySql = """
            INSERT INTO public.fabricante (fabricante_nome)
            VALUES (:fabricanteNome)
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(querySql)
                .param("fabricanteNome", fabricante.getFabricante_nome())
                .update(keyHolder, "id");

        return keyHolder.getKeyAs(Integer.class);
    }

    public Integer atualizarFabricante(Fabricante fabricante) {
        String querySql = """
            UPDATE public.fabricante
            SET fabricante_nome = :fabricanteNome
            WHERE id = :idFabricante
        """;

        return jdbcClient.sql(querySql)
                .param("fabricanteNome", fabricante.getFabricante_nome())
                .param("idFabricante", fabricante.getId())
                .update();
    }

    public Integer deletarFabricante(Long idFabricante) {
        String querySql = """
            DELETE FROM public.fabricante
            WHERE id = :idFabricante
        """;

        return jdbcClient.sql(querySql)
                .param("idFabricante", idFabricante)
                .update();
    }
}