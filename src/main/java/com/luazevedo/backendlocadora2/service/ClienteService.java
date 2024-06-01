package com.luazevedo.backendlocadora2.service;

import com.luazevedo.backendlocadora2.dto.ClienteDTO;
import com.luazevedo.backendlocadora2.entity.Cliente;
import com.luazevedo.backendlocadora2.exception.ValorJaExistenteNaBaseDeDadosException;
import com.luazevedo.backendlocadora2.exception.ValorNaoExistenteNaBaseDeDadosException;
import com.luazevedo.backendlocadora2.filter.ClienteFilter;
import com.luazevedo.backendlocadora2.json.response.ResourceNotFoundException;
import com.luazevedo.backendlocadora2.repository.ClienteRepository;
import com.luazevedo.backendlocadora2.repository.ClienteRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final ClienteRepositoryCustom repositoryJdbcClient;

    @Autowired
    public ClienteService(ClienteRepository repository, ClienteRepositoryCustom repositoryJdbcClient) {
        this.repository = repository;
        this.repositoryJdbcClient = repositoryJdbcClient;
    }

    public Cliente obterCliente(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para o ID: " + id));
    }

    public List<ClienteDTO> obterTodosClientes(ClienteFilter filtro) {
        return repositoryJdbcClient.buscarTodosClientes(filtro);
    }

    public Cliente obterClientePorId(Long idCliente) {
        return repository.findById(idCliente)
                .orElseThrow(() -> new ValorNaoExistenteNaBaseDeDadosException(idCliente.toString()));
    }

    public void inserirCliente(Cliente cliente) {
        validarCliente(cliente);

        repository.save(cliente);
    }

    public void deletarCliente(Long id) {
        checarSeExisteCliente(id);
        repository.deleteById(id);
    }

    @Transactional
    public void atualizarCliente(Cliente cliente) {
        Cliente clienteExistente = repository.findById(cliente.getId())
                .orElseThrow(() -> new ValorNaoExistenteNaBaseDeDadosException(cliente.getId().toString()));

        clienteExistente.setNome(cliente.getNome());
        validarCliente(clienteExistente);

        repository.save(clienteExistente);
    }

    public ClienteDTO buscarClientePorId(Long id) {
        return repositoryJdbcClient.buscarClientePorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado para o ID: " + id));
    }

    private void checarSeExisteCliente(Long id) {
        boolean existe = repositoryJdbcClient.checarExistenciaClientePorId(id);

        if (!existe) {
            throw new ValorNaoExistenteNaBaseDeDadosException(id.toString());
        }
    }

    private void validarCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            throw new IllegalArgumentException("O campo 'nome' é obrigatório.");
        }
        if (cliente.getRg() == null || cliente.getRg().isEmpty()) {
            throw new IllegalArgumentException("O campo 'rg' é obrigatório.");
        }
        if (cliente.getCpf() == null || cliente.getCpf().isEmpty()) {
            throw new IllegalArgumentException("O campo 'cpf' é obrigatório.");
        }
        if (cliente.getCnh() == null || cliente.getCnh().isEmpty()) {
            throw new IllegalArgumentException("O campo 'cnh' é obrigatório.");
        }
        if (cliente.getTelefone() == null || cliente.getTelefone().isEmpty()) {
            throw new IllegalArgumentException("O campo 'telefone' é obrigatório.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O campo 'email' é obrigatório.");
        }
        if (cliente.getCnhVencimento() == null || cliente.getCnhVencimento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).isEmpty()) {
            throw new IllegalArgumentException("O campo 'cnhVencimento' é obrigatório.");
        }
    }
}
