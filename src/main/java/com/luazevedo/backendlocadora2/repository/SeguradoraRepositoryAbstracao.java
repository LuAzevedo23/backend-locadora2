package com.luazevedo.backendlocadora2.repository;

import com.luazevedo.backendlocadora2.entity.Carro;
import com.luazevedo.backendlocadora2.entity.Seguradora;
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
public class SeguradoraRepositoryAbstracao extends AbstractRepository<Seguradora> {
    private final JdbcClient jdbcClient;
    private final RowMapper<Seguradora> seguradoraMapper;

    @Autowired
    public SeguradoraRepositoryAbstracao(JdbcClient jdbcClient) {
        super(jdbcClient);
        this.jdbcClient = jdbcClient;
        this.seguradoraMapper = this::mapRowToSeguradora;
    }

    @Override
    public List<Seguradora> getAll(Object filtro) {
        return List.of();
    }

    private Seguradora mapRowToSeguradora(ResultSet rs, int rowNum) throws SQLException {
        Seguradora seguradora = new Seguradora();
        seguradora.setId(rs.getLong("id"));
        seguradora.setNome(rs.getString("nome"));
        seguradora.setCnpj(rs.getString("cnpj"));
        seguradora.setTelefone(rs.getString("telefone"));
        seguradora.setValor(rs.getDouble("valor"));
        seguradora.setEmail(rs.getString("email"));
        return seguradora;
    }

    public Optional<Seguradora> getByID(Long id) {
        return jdbcClient
                .sql("SELECT * FROM seguradora WHERE id = :idSeguradora")
                .param("idSeguradora", id)
                .query(seguradoraMapper)
                .optional();
    }

    public boolean existsByID(Long id) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM seguradora WHERE id = :idSeguradora)")
                .param("idSeguradora", id)
                .query(Boolean.class)
                .single();
    }

    @Override
    public Integer insert(Seguradora seguradora) {
        String querySql = "INSERT INTO public.seguradora (id_endereco, nome, cnpj, telefone, email, valor) " +
                "VALUES (:idEndereco, :placa, :cnpj, :telefone, :email, :valor);";

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

    public Integer update(Seguradora seguradora) {
        String querySql = """
                UPDATE public.seguradora
                SET id_modelo = :idEndereco, nome = :nome, cnpj = :cnpj, telefone = :telefone, email = :email, valor = :valor
                WHERE id = :idSeguradora;
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

    public Integer delete(Long id) {
        String querySql = "DELETE FROM public.seguradora WHERE id = :idSeguradora;";

        return jdbcClient.sql(querySql)
                .param("idSeguradora", id)
                .update();
    }
}