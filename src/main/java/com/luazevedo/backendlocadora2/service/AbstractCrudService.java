package com.luazevedo.backendlocadora2.service;

import org.springframework.stereotype.Service;
import java.util.List;

// Sempre declarar a classe como abstract do prof. não estava assim.
@Service
public abstract class AbstractCrudService {
    public abstract List<?> getAll(Object filtro);
    public abstract List<?> getByID(Object filtro);
    public abstract <T> T create(T entity);
    public abstract <T> T update(T entity);
    public abstract <T> T delete(T entity);
}

