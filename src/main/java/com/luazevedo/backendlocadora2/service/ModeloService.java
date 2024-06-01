package com.luazevedo.backendlocadora2.service;

import com.luazevedo.backendlocadora2.dto.ModeloDTO;
import com.luazevedo.backendlocadora2.entity.Modelo;
import com.luazevedo.backendlocadora2.exception.ValorJaExistenteNaBaseDeDadosException;
import com.luazevedo.backendlocadora2.exception.ValorNaoExistenteNaBaseDeDadosException;
import com.luazevedo.backendlocadora2.filter.ModeloFilter;
import com.luazevedo.backendlocadora2.repository.ModeloRepository;
import com.luazevedo.backendlocadora2.repository.ModeloRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModeloService {

    private  ModeloRepository repository;
    private  ModeloRepositoryCustom repositoryJdbcClient;

    @Autowired
    public ModeloService(ModeloRepository repository, ModeloRepositoryCustom repositoryJdbcClient) {
        this.repository = repository;
        this.repositoryJdbcClient = repositoryJdbcClient;
    }

    public Modelo obterModeloPorId(Long idModelo) {
        return repository.findById(idModelo)
                .orElseThrow(() -> new ValorNaoExistenteNaBaseDeDadosException(idModelo.toString()));
    }

    public void inserirModelo(Long id, String nome) {
        if (repository.existsById(id)) {
            throw new ValorJaExistenteNaBaseDeDadosException();
        }

        Modelo modelo = new Modelo();
        modelo.setId(id);
        modelo.setModelo_nome(nome);
        validarModelo(modelo);

        repository.save(modelo);
    }

    public void deletarModelo(Long id) {
        checarSeExisteModelo(id);
        repository.deleteById(id);
    }

    @Transactional
    public void atualizarModelo(Modelo modelo) {
        Modelo modeloExistente = repository.findById(modelo.getId())
                .orElseThrow(() -> new ValorNaoExistenteNaBaseDeDadosException(modelo.getId().toString()));

        modeloExistente.setModelo_nome(modelo.getModelo_nome());
        validarModelo(modeloExistente);

        repository.save(modeloExistente);
    }

    public List<ModeloDTO> obterTodosModelos(ModeloFilter filtro) {
        return repositoryJdbcClient.buscarTodosModelos(filtro);
    }

    public ModeloDTO buscarModeloPorId(Long id) {
        return repositoryJdbcClient.buscarModeloPorId(id)
                .orElseThrow(() -> new RuntimeException("Modelo não encontrado para o ID: " + id));
    }

    private void checarSeExisteModelo(Long id) {
        boolean existe = repositoryJdbcClient.checarExistenciaModeloPorId(id);

        if (!existe) {
            throw new ValorNaoExistenteNaBaseDeDadosException(id.toString());
        }
    }

    private void validarModelo(Modelo modelo) {
        modelo.setModelo_nome(modelo.getModelo_nome().toUpperCase());
    }
}