package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.entity.Cliente;
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
public class ClienteRepositoryAbstracao extends AbstractRepository<Cliente> {
    private final JdbcClient jdbcClient;
    private final RowMapper<Cliente> clienteMapper;

    @Autowired
    public ClienteRepositoryAbstracao(JdbcClient jdbcClient) {
        super(jdbcClient);
        this.jdbcClient = jdbcClient;
        this.clienteMapper = this::mapRowToCliente;
    }

    @Override
    public List<Cliente> getAll(Object filtro) {
        // Implementação do método getAll
        return List.of();
    }

    private Cliente mapRowToCliente(ResultSet rs, int rowNum) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCnhVencimento(rs.getDate("cnhVencimento").toLocalDate());

        return cliente;
    }

    @Override
    public Optional<Cliente> getByID(Long id) {
        return jdbcClient
                .sql("SELECT * FROM cliente WHERE id = :idCliente")
                .param("idCliente", id)
                .query(clienteMapper)
                .optional();
    }

    @Override
    public boolean existsByID(Long id) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM cliente WHERE id = :idCliente)")
                .param("idCliente", id)
                .query(Boolean.class)
                .single();
    }

    @Override
    public Integer insert(Cliente cliente) {
        String querySql = "INSERT INTO public.cliente (nome, cnhVencimento) VALUES (:nome, :cnhVencimento);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(querySql)
                .param("nome", cliente.getNome())
                .param("cnhVencimento", cliente.getCnhVencimento())
                .update(keyHolder, "id");
        return keyHolder.getKeyAs(Integer.class);
    }

    @Override
    public Integer update(Cliente cliente) {
        String querySql = """
                UPDATE public.cliente
                SET nome = :nome, cnhVencimento = :cnhVencimento
                WHERE id = :idCliente;
                """;
        return jdbcClient.sql(querySql)
                .param("idCliente", cliente.getId())
                .param("nome", cliente.getNome())
                .param("cnhVencimento", cliente.getCnhVencimento())
                .update();
    }

    @Override
    public Integer delete(Long id) {
        String querySql = "DELETE FROM public.cliente WHERE id = :idCliente;";
        return jdbcClient.sql(querySql)
                .param("idCliente", id)
                .update();
    }
}