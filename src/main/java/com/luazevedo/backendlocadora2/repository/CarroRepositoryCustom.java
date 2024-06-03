package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.dto.CarroDTO;
import com.luazevedo.backendlocadora2.dto.ModeloDTO;
import com.luazevedo.backendlocadora2.entity.Carro;
import com.luazevedo.backendlocadora2.entity.Fabricante;
import com.luazevedo.backendlocadora2.filter.CarroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CarroRepositoryCustom {

    @Autowired
    JdbcClient jdbcClient;

    RowMapper<CarroDTO> carroDTOMapper = (rs, rowNum) -> {
        CarroDTO carroDTO = new CarroDTO();
        carroDTO.setId(rs.getLong("id"));
        carroDTO.setPlaca(rs.getString("placa"));
        carroDTO.setCor(rs.getString("cor"));
        carroDTO.setDisponivel(rs.getBoolean("disponivel"));
        carroDTO.setValorlocacao(rs.getDouble("valorlocacao"));
        carroDTO.setAno(rs.getString("ano"));

        Fabricante fabricante = new Fabricante();
        fabricante.setId(rs.getLong("idFabricante_nome"));
        fabricante.setFabricante_nome(rs.getString("fabricante_nome"));

        ModeloDTO modeloDTO = new ModeloDTO();
        modeloDTO.setId(rs.getInt("idModelo_nome"));
        modeloDTO.setModelo_nome(rs.getString("modelo_nome"));
        modeloDTO.setModelo_nome(fabricante.getFabricante_nome());

        carroDTO.setModelo(modeloDTO);

        return carroDTO;
    };

    public List<CarroDTO> buscarTodosCarros(CarroFilter filtro) {
        StringJoiner where = new StringJoiner(" AND ");
        Map<String, Object> params = new HashMap<>();

        if (filtro.getIdModelo() != null) {
            where.add("id_modelo_nome = :idModelo_nome");
            params.put("idModelo_nome", filtro.getIdModelo());
        }
        if (filtro.getIdFabricante() != null) {
            where.add("id_fabricante_nome = :idFabricante_nome");
            params.put("idFabricante_nome", filtro.getIdFabricante());
        }

        if (filtro.getPlaca() != null) {
            where.add("placa = :placa");
            params.put("placa", filtro.getPlaca());
        }

        if (filtro.getCor() != null) {
            where.add("cor = :cor");
            params.put("cor", filtro.getCor());
        }

        if (filtro.getAno() != null) {
            where.add("ano = :ano");
            params.put("ano", filtro.getAno());
        }

        if (filtro.getDisponivel() != null) {
            where.add("disponivel = :disponivel");
            params.put("disponivel", filtro.getDisponivel());
        }

        if (filtro.getValorLocacao() != null) {
            where.add(("valorLocacao = :valorLocacao"));
            params.put("valorLocacao", filtro.getValorLocacao());
        }

        String sql = """
                SELECT c.*, e.id as idEndereco
                FROM cliente c
                LEFT JOIN endereco e ON c.idEndereco = e.id
                """ + (params.isEmpty() ? "" : " WHERE " + where);

        return jdbcClient.sql(sql)
                .params(params)
                .query(carroDTOMapper)
                .list();
    }

    public Optional<CarroDTO> buscarCarroPorId(Long idCarro) {
        return jdbcClient
                .sql("""
                                SELECT c.*, m.id as idModelo, m.descricao, f.id as idFabricante, f.nome
                                FROM carro c
                                INNER JOIN modelo m ON
                                    c.id_modelo = m.id
                                INNER JOIN fabricante f ON
                                    m.id_fabricante = f.id
                                WHERE c.id = :idCarro
                        """)
                .param("idCarro", idCarro)
                .query(carroDTOMapper)
                .optional();
    }

    public boolean checarExistenciaCarroPorId(Long idCarro) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM carro WHERE id = :idCarro)")
                .param("idCarro", idCarro)
                .query(Boolean.class)
                .single();
    }

    public Integer inserirCarro(Carro carro) {
        String querySql = "INSERT INTO public.carro (id_modelo,placa,cor,disponivel,ano) " +
                " VALUES (:idModelo,:placa,:cor,:disponivel,:ano);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(querySql)
                .param("idModelo", carro.getModelo())
                .param("placa", carro.getPlaca())
                .param("cor", carro.getCor())
                .param("disponivel", carro.getDisponivel())
                .param("ano", carro.getAno())
                .update(keyHolder, "id");

        return keyHolder.getKeyAs(Integer.class);
    }

    public Integer atualizarCarro(Carro carro) {
        String querySql = """
                UPDATE public.carro
                	SET cor=:cor,id_modelo=:idModelo,ano=:ano,placa=:placa,disponivel=:disp
                	WHERE id=:idCarro;
                """;

        return jdbcClient.sql(querySql)
                .param("idCarro", carro.getId())
                .param("idModelo", carro.getModelo())
                .param("placa", carro.getPlaca())
                .param("cor", carro.getCor())
                .param("disp", carro.getDisponivel())
                .param("ano", carro.getAno())
                .update();
    }

    public Integer deletarCarro(Long idCarro) {
        String querySql = "DELETE FROM public.carro WHERE id=:idCarro;";
        return jdbcClient.sql(querySql)
                .param("idCarro", idCarro)
                .update();
    }
}
