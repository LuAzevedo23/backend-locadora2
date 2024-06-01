package com.luazevedo.backendlocadora2.service;

import com.luazevedo.backendlocadora2.dto.CarroDTO;
import com.luazevedo.backendlocadora2.entity.Carro;
import com.luazevedo.backendlocadora2.exception.ValorJaExistenteNaBaseDeDadosException;
import com.luazevedo.backendlocadora2.exception.ValorNaoExistenteNaBaseDeDadosException;
import com.luazevedo.backendlocadora2.filter.CarroFilter;
import com.luazevedo.backendlocadora2.repository.CarroRepository;
import com.luazevedo.backendlocadora2.repository.CarroRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarroService {

    private CarroRepository repository;
    private CarroRepositoryCustom repositoryJdbcClient;
    private final List<Carro> carros = new ArrayList<>();
    private long nextId = 1;

    public void salvarCarro(Carro carro) {
        carro.setId(nextId++);
        carros.add(carro);
    }

    @Autowired
    public CarroService(CarroRepository repository, CarroRepositoryCustom repositoryJdbcClient) {
        this.repository = repository;
        this.repositoryJdbcClient = repositoryJdbcClient;
    }

    public Carro obterCarroPorId(Long idCarro) {
        return repository.findById(idCarro)
                .orElseThrow(() -> new ValorNaoExistenteNaBaseDeDadosException(idCarro.toString()));
    }

    public void inserirCarro(Long id, String nome) {
        if (repository.existsById(id)) {
            throw new ValorJaExistenteNaBaseDeDadosException();
        }

        Carro carro = new Carro();
        carro.setId(id);
        validarCarro(carro);

        repository.save(carro);
    }

    public void deletarCarro(Long id) {
        checarSeExisteCarro(id);
        repository.deleteById(id);
    }

    @Transactional
    public void atualizarCarro(Carro carro) {
        Carro carroExistente = repository.findById(carro.getId())
                .orElseThrow(() -> new ValorNaoExistenteNaBaseDeDadosException(carro.getId().toString()));

        carroExistente.setPlaca(carro.getPlaca());
        carroExistente.setAno(carro.getAno());
        carroExistente.setCor(carro.getCor());
        validarCarro(carroExistente);

        repository.save(carroExistente);
    }

    public List<CarroDTO> obterTodosCarros(CarroFilter filtro) {
        return repositoryJdbcClient.buscarTodosCarros(filtro);
    }

    public CarroDTO buscarCarroPorId(Long id) {
        return repositoryJdbcClient.buscarCarroPorId(id)
                .orElseThrow(() -> new RuntimeException("Carro não encontrado para o ID: " + id));
    }

    private void checarSeExisteCarro(Long id) {
        boolean existe = repositoryJdbcClient.checarExistenciaCarroPorId(id);

        if (!existe) {
            throw new ValorNaoExistenteNaBaseDeDadosException(id.toString());
        }
    }
    private void validarCarro(Carro carro) {

        if (carro.getPlaca() == null || carro.getPlaca().isEmpty()) {
            throw new IllegalArgumentException("O campo 'placa' é obrigatório.");
        }
        if (carro.getAno() == null || carro.getAno().isEmpty()) {
            throw new IllegalArgumentException("O campo 'ano' é obrigatório.");
        }
        if (carro.getCor() == null || carro.getCor().isEmpty()) {
            throw new IllegalArgumentException("O campo 'cor' é obrigatório.");
        }
    }
}

