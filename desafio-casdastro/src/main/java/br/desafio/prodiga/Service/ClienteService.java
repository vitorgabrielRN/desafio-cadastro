package br.desafio.prodiga.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Endereco;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.dto.Cliente.ClienteDTO;
import br.desafio.prodiga.dto.Cliente.ClienteForm;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteDTO cadastrar(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        cliente.setEndereco(new Endereco(
                dto.endereco().logradouro(),
                dto.endereco().bairro(),
                dto.endereco().cep(),
                dto.endereco().cidade(),
                dto.endereco().uf()));
        return new ClienteDTO(clienteRepository.save(cliente));
    }

    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(ClienteDTO::new)
                .toList();
    }

    public ClienteDTO buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .map(ClienteDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    public void excluir(Long id) {
        clienteRepository.deleteById(id);
    }

    public void salvar(ClienteForm form) {

        try {
            Cliente cliente = new Cliente();
            cliente.setNome(form.getNome());
            cliente.setCpf(form.getCpf());
            cliente.setEmail(form.getEmail());
            cliente.setTelefone(form.getTelefone());

            Endereco endereco = new Endereco();
            endereco.setLogradouro(form.getLogradouro());
            endereco.setBairro(form.getBairro());
            endereco.setCep(form.getCep());
            endereco.setCidade(form.getCidade());
            endereco.setUf(form.getUf());

            cliente.setEndereco(endereco);
            clienteRepository.save(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}