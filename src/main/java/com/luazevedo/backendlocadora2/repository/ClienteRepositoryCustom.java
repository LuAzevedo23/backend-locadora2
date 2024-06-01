package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.dto.ClienteDTO;
import com.luazevedo.backendlocadora2.dto.EnderecoDTO;
import com.luazevedo.backendlocadora2.entity.Cliente;
import com.luazevedo.backendlocadora2.filter.ClienteFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ClienteRepositoryCustom {

    @Autowired
    private JdbcClient jdbcClient;

    private final RowMapper<ClienteDTO> clienteDTOMapper = new ClienteDTOMapper();

    public List<ClienteDTO> buscarTodosClientes(ClienteFilter filtro) {
        StringJoiner where = new StringJoiner(" AND ");
        Map<String, Object> params = new HashMap<>();

        if (filtro != null) {
            if (filtro.getNome() != null) {
                where.add("c.nome = :nome");
                params.put("nome", filtro.getNome());
            }
            if (filtro.getRg() != null) {
                where.add("c.rg = :rg");
                params.put("rg", filtro.getRg());
            }
            if (filtro.getCpf() != null) {
                where.add("c.cpf = :cpf");
                params.put("cpf", filtro.getCpf());
            }
            if (filtro.getCnh() != null) {
                where.add("c.cnh = :cnh");
                params.put("cnh", filtro.getCnh());
            }
            if (filtro.getTelefone() != null) {
                where.add("c.telefone = :telefone");
                params.put("telefone", filtro.getTelefone());
            }
            if (filtro.getEmail() != null) {
                where.add("c.email = :email");
                params.put("email", filtro.getEmail());
            }
            if (filtro.getCnhVencimento() != null) {
                where.add("c.cnhVencimento >= :cnhVencimentoInicio");
                params.put("cnhVencimentoInicio", filtro.getCnhVencimento());
            }
            if (filtro.getCnhVencimento() != null) {
                where.add("c.cnhVencimento <= :cnhVencimentoFim");
                params.put("cnhVencimentoFim", filtro.getCnhVencimento());
            }
        }

        String sql = """
                SELECT c.*, e.id as idEndereco
                FROM cliente c
                LEFT JOIN endereco e ON c.idEndereco = e.id
                """ + (params.isEmpty() ? "" : " WHERE " + where.toString());

        return jdbcClient.sql(sql)
                .params(params)
                .query(clienteDTOMapper)
                .list();
    }

    private static class ClienteDTOMapper implements RowMapper<ClienteDTO> {
        @Override
        public ClienteDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setId(rs.getLong("id"));
            clienteDTO.setNome(rs.getString("nome"));
            clienteDTO.setRg(rs.getString("rg"));
            clienteDTO.setCpf(rs.getString("cpf"));
            clienteDTO.setCnh(rs.getString("cnh"));
            clienteDTO.setTelefone(rs.getString("telefone"));
            clienteDTO.setEmail(rs.getString("email"));
            clienteDTO.setCnhVencimento(rs.getDate("cnhVencimento").toLocalDate());

            EnderecoDTO enderecoDTO = new EnderecoDTO();
            enderecoDTO.setId(rs.getLong("idEndereco"));
            clienteDTO.setEndereco(enderecoDTO);

            return clienteDTO;
        }
    }

    public Optional<ClienteDTO> buscarClientePorId(Long idCliente) {
        return jdbcClient
                .sql("""
                        SELECT c.*, e.id as idEndereco
                        FROM cliente c
                        LEFT JOIN endereco e ON c.idEndereco = e.id
                        WHERE c.id = :idCliente
                        """)
                .param("idCliente", idCliente)
                .query(clienteDTOMapper)
                .optional();
    }

    public boolean checarExistenciaClientePorId(Long id) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM cliente WHERE id = :idCliente)")
                .param("idCliente", id)
                .query(Boolean.class)
                .single();
    }

    public Integer inserirCliente(Cliente cliente) {
        String sql = """
                INSERT INTO cliente (nome, rg, cpf, cnh, telefone, email, cnhVencimento, idEndereco)
                VALUES (:nome, :rg, :cpf, :cnh, :telefone, :email, :cnhVencimento, :idEndereco)
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("nome", cliente.getNome());
        params.put("rg", cliente.getRg());
        params.put("cpf", cliente.getCpf());
        params.put("cnh", cliente.getCnh());
        params.put("telefone", cliente.getTelefone());
        params.put("email", cliente.getEmail());
        params.put("cnhVencimento", cliente.getCnhVencimento());
        params.put("idEndereco", cliente.getEndereco().getId());

        return jdbcClient.sql(sql)
                .params(params)
                .update();

    }

    public Integer atualizarCliente(Cliente cliente) {
        String sql = """
                UPDATE cliente
                SET nome = :nome, rg = :rg, cpf = :cpf, cnh = :cnh, telefone = :telefone,
                    email = :email, cnhVencimento = :cnhVencimento, idEndereco = :idEndereco
                WHERE id = :idCliente
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("nome", cliente.getNome());
        params.put("rg", cliente.getRg());
        params.put("cpf", cliente.getCpf());
        params.put("cnh", cliente.getCnh());
        params.put("telefone", cliente.getTelefone());
        params.put("email", cliente.getEmail());
        params.put("cnhVencimento", cliente.getCnhVencimento());
        params.put("idEndereco", cliente.getEndereco().getId());
        params.put("idCliente", cliente.getId());

        return jdbcClient.sql(sql)
                .params(params)
                .update();

    }

    public Integer deletarCliente(Long idCliente) {
        String sql = "DELETE FROM cliente WHERE id = :idCliente";
        Map<String, Object> params = new HashMap<>();
        params.put("idCliente", idCliente);

        return jdbcClient.sql(sql)
                .params(params)
                .update();

    }

}

