package com.luazevedo.backendlocadora2.repository;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public abstract class AbstractRepository<T> { // Uso de Generics com 'T' representando o tipo de entidade
    protected final JdbcClient jdbcClient; // Protegido para permitir acesso nas subclasses

    public AbstractRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public abstract List<T> getAll(Object filtro); // Método para obter todas as entidades com filtro

    public abstract Optional<T> getByID(Long id); // Método para obter uma entidade pelo ID

    public abstract boolean existsByID(Long id); // Método para verificar a existência de uma entidade pelo ID

    public abstract Integer insert(T entity); // Método para inserir uma nova entidade

    public abstract Integer update(T entity); // Método para atualizar uma entidade

    public abstract Integer delete(Long id); // Método para deletar uma entidade pelo ID
}
