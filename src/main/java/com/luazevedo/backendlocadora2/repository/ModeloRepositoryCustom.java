package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.dto.ModeloDTO;
import com.luazevedo.backendlocadora2.entity.Modelo;
import com.luazevedo.backendlocadora2.filter.ModeloFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ModeloRepositoryCustom {

    @Autowired
    JdbcClient jdbcClient;

    RowMapper<ModeloDTO> mapperModelo = (rs, rowNum) -> {
        ModeloDTO modelo = new ModeloDTO();
        modelo.setId(rs.getInt("id"));
        modelo.setModelo_nome(rs.getString("nome"));
        return modelo;
    };

    public List<ModeloDTO> buscarTodosModelos(ModeloFilter filtro) {
        StringJoiner where = new StringJoiner(" AND ", " WHERE ", "");
        Map<String, Object> params = new HashMap<>();

        if (filtro.getId() != null) {
            where.add("id_Modelo = :idModelo");
            params.put("idModelo", filtro.getId());
        }
        if (filtro.getModeloNome() != null) {
            where.add("modelo_nome = :modeloNome");
            params.put("modeloNome", filtro.getModeloNome());
        }

        if (!params.isEmpty()) {
            String sql = """
                    SELECT m.*, f.id as idFabricante, f.nome
                    FROM modelo m
                    INNER JOIN fabricante f ON m.id_fabricante = f.id
                    """ + where.toString();

            return jdbcClient.sql(sql)
                    .params(params)
                    .query(mapperModelo)
                    .list();
        } else {
            return Collections.emptyList();  // Retorna lista vazia se não há filtros aplicados
        }
    }

    public Optional<ModeloDTO> buscarModeloPorId(Long idModelo) {
        return jdbcClient
                .sql("SELECT m.*, f.id as id, f.nome FROM modelo m INNER JOIN fabricante f ON m.id = f.id WHERE id = :idModelo")
                .param("idModelo", idModelo)
                .query(mapperModelo)
                .optional();
    }

    public boolean checarExistenciaModeloPorId(Long idModelo) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM modelo WHERE id = :idModelo)")
                .param("idModelo", idModelo)
                .query(Boolean.class)
                .single();
    }

    public Integer criarModelo(Modelo modelo) {
        String querySql = "INSERT INTO public.modelo (nome,id) VALUES (:nome, :id);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(querySql)
                .param("nome", modelo.getModelo_nome())
                .param("id", modelo.getId())
                .update(keyHolder, "id");

        return keyHolder.getKeyAs(Integer.class);
    }

    public Integer atualizarModelo(Modelo modelo) {
        String querySql = "UPDATE public.modelo SET nome=:nome,id=:id WHERE id=:idModelo;";

        return jdbcClient.sql(querySql)
                .param("nome", modelo.getModelo_nome())
                .param("id", modelo.getId())
                .param("idModelo", modelo.getId())
                .update();
    }

    public Integer deletarModelo(Integer idModelo) {
        String querySql = "DELETE FROM public.modelo WHERE id=:idModelo;";

        return jdbcClient.sql(querySql)
                .param("idModelo", idModelo)
                .update();
    }

}
