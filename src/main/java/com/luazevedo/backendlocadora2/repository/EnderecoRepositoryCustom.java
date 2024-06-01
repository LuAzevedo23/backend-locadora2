package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.dto.EnderecoDTO;
import com.luazevedo.backendlocadora2.entity.Endereco;
import com.luazevedo.backendlocadora2.filter.EnderecoFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class EnderecoRepositoryCustom {

    @Autowired
    JdbcClient jdbcClient;

    RowMapper<EnderecoDTO> enderecoDTOMapper = (rs, rowNum) -> {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setId(rs.getLong("id"));
        enderecoDTO.setRua(rs.getString("rua"));
        enderecoDTO.setNumero(rs.getString("numero"));
        enderecoDTO.setBairro(rs.getString("bairro"));
        enderecoDTO.setCidade(rs.getString("cidade"));
        enderecoDTO.setEstado(rs.getString("estado"));
        enderecoDTO.setCep(rs.getString("cep"));

        return enderecoDTO;
    };

    public List<EnderecoDTO> buscarTodosEnderecos(EnderecoFilter filtro) {
        StringJoiner where = new StringJoiner(" AND ");
        Map<String, Object> params = new HashMap<>();

        if (filtro.getRua() != null) {
            where.add("rua = :rua");
            params.put("rua", filtro.getRua());
        }

        if (filtro.getNumero() != null) {
            where.add("numero = :numero");
            params.put("numero", filtro.getNumero());
        }

        if (filtro.getBairro() != null) {
            where.add("bairro = :bairro");
            params.put("bairro", filtro.getBairro());
        }

        if (filtro.getCidade() != null) {
            where.add("cidade = :cidade");
            params.put("cidade", filtro.getCidade());
        }

        if (filtro.getEstado() != null) {
            where.add(("estado = :estado"));
            params.put("estado", filtro.getEstado());
        }

        if (filtro.getCep() != null) {
            where.add(("cep = :cep"));
            params.put("cep", filtro.getCep());
        }

        if (!params.isEmpty()) {
            String sql = """
                SELECT id, rua, numero, bairro, cidade, estado, cep
                FROM endereco
                WHERE """ + where.toString();

            return jdbcClient.sql(sql)
                    .params(params)
                    .query(enderecoDTOMapper)
                    .list();
        } else {
            // Caso não haja filtros, retorna todos os endereços
            String sql = "SELECT id, rua, numero, bairro, cidade, estado, cep FROM endereco";
            return jdbcClient.sql(sql)
                    .query(enderecoDTOMapper)
                    .list();
        }
    }


    public Optional<EnderecoDTO> buscarEnderecoPorId(Long idEndereco) {
        return jdbcClient
                .sql("SELECT * FROM endereco WHERE id = :idEndereco")
                .param("idEndereco", idEndereco)
                .query(enderecoDTOMapper)
                .optional();
    }

    public boolean checarExistenciaEnderecoPorId(Long idEndereco) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM endereco WHERE id = :idEndereco)")
                .param("idEndereco", idEndereco)
                .query(Boolean.class)
                .single();
    }

    public Integer inserirEndereco(Endereco endereco) {
        String querySql = "INSERT INTO public.endereco (rua, numero, bairro, cidade, estado, cep) " +
                "VALUES (:rua, :numero, :bairro, :cidade, :estado, :cep);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(querySql)
                .param("rua", endereco.getRua())
                .param("bairro", endereco.getBairro())
                .param("cidade", endereco.getCidade())
                .param("estado", endereco.getEstado())
                .param("cep", endereco.getCep())
                .update(keyHolder, "id");

        return keyHolder.getKeyAs(Integer.class);
    }

    public Integer atualizarEndereco(Endereco endereco) {
        String querySql = """
                UPDATE public.endereco
                	SET rua=:rua,numero =:numero , bairro =:bairro , cidade =:cidade, estado =:estado, cep = :cep
                	WHERE id=:idEndereco;
                """;

        return jdbcClient.sql(querySql)
                .param("rua", endereco.getRua())
                .param("bairro", endereco.getBairro())
                .param("cidade", endereco.getCidade())
                .param("estado", endereco.getEstado())
                .param("cep", endereco.getCep())
                .update();
    }

    public Integer deletarEndereco(Long idEndereco) {
        String querySql = "DELETE FROM public.endereco WHERE id=:idEndereco;";
        return jdbcClient.sql(querySql)
                .param("idEndereco", idEndereco)
                .update();
    }
}

