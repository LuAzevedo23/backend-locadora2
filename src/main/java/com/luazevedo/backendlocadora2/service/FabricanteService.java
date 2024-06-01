package com.luazevedo.backendlocadora2.service;

import com.luazevedo.backendlocadora2.dto.FabricanteDTO;
import com.luazevedo.backendlocadora2.entity.Fabricante;
import com.luazevedo.backendlocadora2.exception.ValorJaExistenteNaBaseDeDadosException;
import com.luazevedo.backendlocadora2.exception.ValorNaoExistenteNaBaseDeDadosException;
import com.luazevedo.backendlocadora2.filter.FabricanteFilter;
import com.luazevedo.backendlocadora2.repository.FabricanteRepository;
import com.luazevedo.backendlocadora2.repository.FabricanteRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FabricanteService {

    private FabricanteRepository repository;
    private FabricanteRepositoryCustom repositoryJdbcClient;

    @Autowired
    public FabricanteService(FabricanteRepository repository, FabricanteRepositoryCustom repositoryJdbcClient) {
        this.repository = repository;
        this.repositoryJdbcClient = repositoryJdbcClient;
    }

    public void inserirFabricante(Long id, String nome) {
        if (repository.existsById(id)) {
            throw new ValorJaExistenteNaBaseDeDadosException();
        }

        Fabricante fabricante = new Fabricante();
        fabricante.setFabricante_nome(nome);
        fabricante.setFabricante_nome(nome);
        validarFabricante(fabricante);

        repository.save(fabricante);
    }

    public void deletarFabricante(Long id) {
        checarSeExisteFabricante(id);
        repository.deleteById(id);
    }

    @Transactional
    public void atualizarFabricante(Fabricante fabricante) {
        Fabricante fabricanteExistente = repository.findById(fabricante.getId())
                .orElseThrow(() -> new ValorNaoExistenteNaBaseDeDadosException(fabricante.getId().toString()));

        fabricanteExistente.setFabricante_nome(fabricante.getFabricante_nome());
        validarFabricante(fabricanteExistente);

        repository.save(fabricanteExistente);
    }

    public List<FabricanteDTO> obterTodosFabricantes(FabricanteFilter filtro) {
        return repositoryJdbcClient.buscarTodosFabricantes(filtro);
    }

    public FabricanteDTO buscarFabricantePorId(Long id) {
        return repositoryJdbcClient.buscarFabricantePorId(id)
                .orElseThrow(() -> new RuntimeException("Fabricante não encontrado para o ID: " + id));
    }

    private void checarSeExisteFabricante(Long id) {
        boolean existe = repositoryJdbcClient.checarExistenciaFabricantePorId(id);

        if (!existe) {
            throw new ValorNaoExistenteNaBaseDeDadosException(id.toString());
        }
    }

    private void validarFabricante(Fabricante fabricante) {
        fabricante.setFabricante_nome(fabricante.getFabricante_nome().toUpperCase());

        if (fabricante.getFabricante_nome() == null || fabricante.getFabricante_nome().isEmpty()) ;
        throw new IllegalArgumentException("O campo 'fabricante' é obrigatório.");
    }
}

