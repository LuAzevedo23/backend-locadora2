package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.dto.EnderecoDTO;
import com.luazevedo.backendlocadora2.dto.SeguradoraDTO;
import com.luazevedo.backendlocadora2.entity.Seguradora;
import com.luazevedo.backendlocadora2.filter.SeguradoraFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class SeguradoraRepositoryCustom {
    @Autowired
    private JdbcClient jdbcClient;

    RowMapper<SeguradoraDTO> seguradoraDTOMapper = (rs, rowNum) -> {
        SeguradoraDTO seguradoraDTO = new SeguradoraDTO();
        seguradoraDTO.setId(rs.getLong("id"));
        seguradoraDTO.setNome(rs.getString("nome"));
        seguradoraDTO.setCnpj(rs.getString("cnpj"));
        seguradoraDTO.setTelefone(rs.getString("telefone"));
        seguradoraDTO.setEmail(rs.getString("email"));
        seguradoraDTO.setValor(rs.getDouble("valor"));

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setId(rs.getLong("id_endereco"));
        enderecoDTO.setRua(rs.getString("rua"));
        enderecoDTO.setNumero(rs.getString("numero"));
        enderecoDTO.setBairro(rs.getString("bairro"));
        enderecoDTO.setCidade(rs.getString("cidade"));
        enderecoDTO.setEstado(rs.getString("estado"));
        enderecoDTO.setCep(rs.getString("cep"));

        seguradoraDTO.setEndereco(enderecoDTO);
        return seguradoraDTO;
    };

    public List<SeguradoraDTO> buscarTodasSeguradoras(SeguradoraFilter filtro) {
        StringJoiner where = new StringJoiner(" AND ");
        Map<String, Object> params = new HashMap<>();

        if (filtro.getEndereco() != null) {
            where.add("id_endereco = :idEndereco");
        }

        if (filtro.getId() != null) {
            where.add("id_seguradora = :idSeguradora");
            params.put("idSeguradora", filtro.getId());

        }
        if (filtro.getNome() != null) {
            where.add("nome = :nome");
            params.put("nome", filtro.getNome());

        }
        if (filtro.getCnpj() != null) {
            where.add("cnpj = :cnpj");
            params.put("cnpj", filtro.getCnpj());
        }

        if (filtro.getTelefone() != null) {
            where.add("telefone = :telefone");
            params.put("telefone", filtro.getTelefone());
        }

        if (filtro.getEmail() != null) {
            where.add("email = :email");
        }

        if (filtro.getValor() != null) {
            where.add("valor = :valor");
        }

        if (!params.isEmpty()) {
            String sql = """
                       SELECT c.*, m.id as idModelo, m.nome, f.id as idFabricante, f.nome
                       FROM carro c
                       INNER JOIN modelo m ON
                           c.id_modelo = m.id
                       INNER JOIN fabricante f ON
                           m.id_fabricante = f.id
                       WHERE
                    """ + where;

            return jdbcClient.sql(sql)
                    .params(params)
                    .query(seguradoraDTOMapper).list();
        } else {
            return jdbcClient.sql("""
                               SELECT c.*, m.id as idModelo, m.nome, f.id as idFabricante, f.nome
                               FROM carro c
                               INNER JOIN modelo m ON
                                   c.id_modelo = m.id
                               INNER JOIN fabricante f ON
                                   m.id_fabricante = f.id
                            """)
                    .query(seguradoraDTOMapper)
                    .list();
        }
    }

    public Optional<SeguradoraDTO> buscarSeguradoraPorId(Long idSeguradora) {
        return jdbcClient
                .sql("""
                                SELECT s.*, e.id as id_endereco, e.rua, e.numero, e.bairro, e.cidade, e.estado, e.cep
                                FROM seguradora c
                                INNER JOIN endereco e ON s.id_endereco = e.id
                                WHERE s.id = :idSeguradora
                                WHERE c.id = :idSeguradora
                        """)
                .param("idSeguradora", idSeguradora)

                .query(seguradoraDTOMapper)
                .optional();
    }

    public boolean checarExistenciaSeguradoraPorId(Long idSeguradora) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM seguradora WHERE id = :idSeguradora)")
                .param("idSeguradora", idSeguradora)
                .query(Boolean.class)
                .single();
    }

    public Integer inserirSeguradora(Seguradora seguradora) {
        String querySql = "INSERT INTO public.seguradora (id_endereco,nome,cnpj, telefone, email, valor) " +
                " VALUES (:idEndereco,:nome,:cnpj,:telefone,:email, :valor);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(querySql)
                .param("idEndereco", seguradora.getEndereco())
                .param("nome", seguradora.getNome())
                .param("cnpj", seguradora.getCnpj())
                .param("telefone", seguradora.getTelefone())
                .param("email", seguradora.getEmail())
                .param("valor", seguradora.getValor())
                .update(keyHolder, "id");

        return keyHolder.getKeyAs(Integer.class);
    }

    public Integer atualizarSeguradora(Seguradora seguradora) {
        String querySql = """
                UPDATE public.seguradora
                	SET cor=:cor,id_modelo=:idModelo,ano=:ano,placa=:placa,disponivel=:disp
                	WHERE id=:idCarro;
                """;

        return jdbcClient.sql(querySql)
                .param("idEndereco", seguradora.getEndereco())
                .param("nome", seguradora.getNome())
                .param("cnpj", seguradora.getCnpj())
                .param("telefone", seguradora.getTelefone())
                .param("email", seguradora.getEmail())
                .param("valor", seguradora.getValor())
                .update();
    }

    public Integer deletarSeguradora(Long idSeguradora) {
        String querySql = "DELETE FROM public.seguradora WHERE id=:idSeguradora;";
        return jdbcClient.sql(querySql)
                .param("idSeguradora", idSeguradora)
                .update();
    }
}




