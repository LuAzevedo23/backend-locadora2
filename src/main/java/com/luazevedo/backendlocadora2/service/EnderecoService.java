package com.luazevedo.backendlocadora2.service;

import com.luazevedo.backendlocadora2.dto.EnderecoDTO;
import com.luazevedo.backendlocadora2.entity.Carro;
import com.luazevedo.backendlocadora2.entity.Endereco;
import com.luazevedo.backendlocadora2.exception.ValorNaoExistenteNaBaseDeDadosException;
import com.luazevedo.backendlocadora2.filter.EnderecoFilter;
import com.luazevedo.backendlocadora2.repository.EnderecoRepository;
import com.luazevedo.backendlocadora2.repository.EnderecoRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnderecoService {

    private final EnderecoRepository repository;
    private final EnderecoRepositoryCustom repositoryJdbcClient;

    @Autowired
    public EnderecoService(EnderecoRepository repository, EnderecoRepositoryCustom repositoryJdbcClient) {
        this.repository = repository;
        this.repositoryJdbcClient = repositoryJdbcClient;
    }
    public Carro obterEnderecoPorId(Long idEndereco) {
        return repository.findById(idEndereco)
                .orElseThrow(() -> new ValorNaoExistenteNaBaseDeDadosException(idEndereco.toString()));
    }

    public List<EnderecoDTO> obterTodosEnderecos(EnderecoFilter filtro) {
        return repositoryJdbcClient.buscarTodosEnderecos(filtro);
    }

    public EnderecoDTO buscarEnderecoPorId(Long id) {
        return repositoryJdbcClient.buscarEnderecoPorId(id)
                .orElseThrow(() -> new RuntimeException("Endereco não encontrado para o ID: " + id));
    }

    @Transactional
    public EnderecoDTO inserirEndereco(Endereco endereco) {
        validarEndereco(endereco);
        Integer idEndereco = repositoryJdbcClient.inserirEndereco(endereco);
        return buscarEnderecoPorId(idEndereco.longValue());
    }

    public void deletarEndereco(Long id) {
        checarSeExisteEndereco(id);
        repository.deleteById(id);
    }

    @Transactional
    public EnderecoDTO atualizarEndereco(Endereco endereco) {
        validarEndereco(endereco);
        Long idEndereco = repositoryJdbcClient.atualizarEndereco(endereco);
        return buscarEnderecoPorId(idEndereco.longValue());
    }

    private void checarSeExisteEndereco(Long id) {
        boolean existe = repositoryJdbcClient.checarExistenciaEnderecoPorId(id);

        if (!existe) {
            throw new ValorNaoExistenteNaBaseDeDadosException(id.toString());
        }
    }

    public void validarEndereco(Endereco endereco) {
        if (endereco.getRua() == null || endereco.getRua().isEmpty()) {
            throw new IllegalArgumentException("O campo 'rua' é obrigatório.");
        }
        if (endereco.getBairro() == null || endereco.getBairro().isEmpty()) {
            throw new IllegalArgumentException("O campo 'bairro' é obrigatório.");
        }
        if (endereco.getCidade() == null || endereco.getCidade().isEmpty()) {
            throw new IllegalArgumentException("O campo 'cidade' é obrigatório.");
        }
        if (endereco.getEstado() == null || endereco.getEstado().isEmpty()) {
            throw new IllegalArgumentException("O campo 'Estado' é obrigatório.");
        }
        if (endereco.getCep() == null || endereco.getCep().isEmpty()) {
            throw new IllegalArgumentException("O campo 'CEP' é obrigatório.");
        }
    }


}