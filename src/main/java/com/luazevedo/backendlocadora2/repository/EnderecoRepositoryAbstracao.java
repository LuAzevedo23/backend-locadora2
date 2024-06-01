package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.entity.Endereco;
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
public class EnderecoRepositoryAbstracao extends AbstractRepository<Endereco> {
    private final JdbcClient jdbcClient;
    private final RowMapper<Endereco> enderecoMapper;

    @Autowired
    public EnderecoRepositoryAbstracao(JdbcClient jdbcClient) {
        super(jdbcClient);
        this.jdbcClient = jdbcClient;
        this.enderecoMapper = this::mapRowToEndereco;
    }

    @Override
    public List<Endereco> getAll(Object filtro) {
        return List.of();
    }

    private Endereco mapRowToEndereco(ResultSet rs, int rowNum) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setId(rs.getLong("id"));
        endereco.setRua(rs.getString("rua"));
        endereco.setNumero(rs.getString("numero"));
        endereco.setBairro(rs.getString("bairro"));
        endereco.setCidade(rs.getString("cidade"));
        endereco.setEstado(rs.getString("estado"));
        endereco.setCep(rs.getString("cep"));
        return endereco;
    }

    @Override
    public Optional<Endereco> getByID(Long id) {
        return jdbcClient
                .sql("SELECT * FROM endereco WHERE id = :idEndereco")
                .param("idEndereco", id)
                .query(enderecoMapper)
                .optional();
    }

    @Override
    public boolean existsByID(Long id) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM endereco WHERE id = :idEndereco)")
                .param("idEndereco", id)
                .query(Boolean.class)
                .single();
    }

    @Override
    public Integer insert(Endereco endereco) {
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
    }

    @Override
    public Integer update(Endereco endereco) {
        String querySql = """
                UPDATE public.endereco
                SET rua = :rua
                WHERE id = :idEndereco;
                """;
        return jdbcClient.sql(querySql)
                .param("rua", endereco.getRua())
                .param("bairro", endereco.getBairro())
                .param("cidade", endereco.getCidade())
                .param("estado", endereco.getEstado())
                .param("cep", endereco.getCep())
                .update();
    }

    public Integer delete(Long id) {
        String querySql = "DELETE FROM public.endereco WHERE id = :idEndereco;";

        return jdbcClient.sql(querySql)
                .param("idEndereco", id)
                .update();
    }
}









