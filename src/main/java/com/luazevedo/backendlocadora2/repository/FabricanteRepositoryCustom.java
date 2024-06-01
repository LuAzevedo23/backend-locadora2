package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.dto.FabricanteDTO;
import com.luazevedo.backendlocadora2.entity.Fabricante;
import com.luazevedo.backendlocadora2.filter.FabricanteFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class FabricanteRepositoryCustom {

    @Autowired
    private JdbcClient jdbcClient;

    private final RowMapper<FabricanteDTO> fabricanteDTOMapper = new FabricanteRepositoryCustom.FabricanteDTOMapper();

    public List<FabricanteDTO> buscarTodosFabricantes(FabricanteFilter filtro) {
        StringJoiner where = new StringJoiner(" AND ");
        Map<String, Object> params = new HashMap<>();


        if (filtro != null) {
            if (filtro.getFabricanteNome() != null) {
                where.add("c.nome = :nome");
                params.put("nome", filtro.getFabricanteNome());
            }
        }
    }

    private static class FabricanteDTOMapper implements RowMapper<FabricanteDTO> {

        @Override
        public FabricanteDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            FabricanteDTO fabricanteDTO = new FabricanteDTO();
            fabricanteDTO.setId(rs.getLong("id"));
            fabricanteDTO.setFabricante_nome(rs.getString("nome"));

            return fabricanteDTO;
        }
    }

    public Optional<FabricanteDTO> buscarFabricantePorId(Long idFabricante) {
        return jdbcClient
                .sql("""
                        SELECT c.*, e.id as idFabricante
                        FROM fabricante
                        WHERE c.id = :idFabricante
                        """)
                .param("idFabricante", idFabricante)
                .query(fabricanteDTOMapper)
                .optional();
    }

    public boolean checarExistenciaFabricantePorId(Long id) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM fabricante WHERE id = :idFabricante)")
                .param("idFabricante", id)
                .query(Boolean.class)
                .single();
    }

    public Integer inserirFabricante(Fabricante fabricante) {
        String sql = """
                INSERT INTO fabricante (fabricante_nome)
                VALUES (:fabricante_nome)
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("nome", fabricante.getFabricante_nome());

        return jdbcClient.sql(sql)
                .params(params)
                .update();
    }

    public Integer atualizarFabricante(Fabricante fabricante) {
        String sql = """
                UPDATE fabricante
                SET nome = :fabricante_nome
                WHERE id = :idFabricante
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("nome", fabricante.getFabricante_nome());

        return jdbcClient.sql(sql)
                .params(params)
                .update();

    }

    public Integer deletarFabricante(Long idFabricante) {
        String sql = "DELETE FROM fabricante WHERE id = :idFabricante";
        Map<String, Object> params = new HashMap<>();
        params.put("idFabricante", idFabricante);

        return jdbcClient.sql(sql)
                .params(params)
                .update();

    }

}

