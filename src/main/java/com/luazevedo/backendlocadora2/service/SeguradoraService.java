package com.luazevedo.backendlocadora2.service;

import com.luazevedo.backendlocadora2.dto.SeguradoraDTO;
import com.luazevedo.backendlocadora2.entity.Seguradora;
import com.luazevedo.backendlocadora2.exception.ValorJaExistenteNaBaseDeDadosException;
import com.luazevedo.backendlocadora2.exception.ValorNaoExistenteNaBaseDeDadosException;
import com.luazevedo.backendlocadora2.filter.SeguradoraFilter;
import com.luazevedo.backendlocadora2.repository.SeguradoraRepository;
import com.luazevedo.backendlocadora2.repository.SeguradoraRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeguradoraService {

    private SeguradoraRepository repository;
    private SeguradoraRepositoryCustom repositoryJdbcClient;

    @Autowired
    public SeguradoraService(SeguradoraRepository repository, SeguradoraRepositoryCustom repositoryJdbcClient) {
        this.repository = repository;
        this.repositoryJdbcClient = repositoryJdbcClient;
    }

    public Seguradora obterSeguradoraPorId(Long idSeguradora) {
        return repository.findById(idSeguradora)
                .orElseThrow(() -> new ValorNaoExistenteNaBaseDeDadosException(idSeguradora.toString()));
    }

    public void inserirSeguradora(Long id, String nome) {
        if (repository.existsById(id)) {
            throw new ValorJaExistenteNaBaseDeDadosException();
        }

        Seguradora seguradora = new Seguradora();
        seguradora.setId(id);
        seguradora.setNome(nome);
        validarSeguradora(seguradora);

        repository.save(seguradora);
    }

    public void deletarSeguradora(Long id) {
        checarSeExisteSeguradora(id);
        repository.deleteById(id);
    }

    @Transactional
    public void atualizarSeguradora(Seguradora seguradora) {
        Seguradora seguradoraExistente = repository.findById(seguradora.getId())
                .orElseThrow(() -> new ValorNaoExistenteNaBaseDeDadosException(seguradora.getId().toString()));

        seguradoraExistente.setNome(seguradora.getNome());
        validarSeguradora(seguradoraExistente);

        repository.save(seguradoraExistente);
    }

    public List<SeguradoraDTO> obterTodasSeguradoras(SeguradoraFilter filtro) {
        return repositoryJdbcClient.buscarTodasSeguradoras(filtro);
    }

    public SeguradoraDTO buscarSeguradoraPorId(Long id) {
        return repositoryJdbcClient.buscarSeguradoraPorId(id)
                .orElseThrow(() -> new ValorNaoExistenteNaBaseDeDadosException(id.toString()));
    }

    private void checarSeExisteSeguradora(Long id) {
        boolean existe = repositoryJdbcClient.checarExistenciaSeguradoraPorId(id);

        if (!existe) {
            throw new ValorNaoExistenteNaBaseDeDadosException(id.toString());
        }
    }

    private void validarSeguradora(Seguradora seguradora) {
        seguradora.setNome((seguradora.getNome().toUpperCase()));
    }
}

