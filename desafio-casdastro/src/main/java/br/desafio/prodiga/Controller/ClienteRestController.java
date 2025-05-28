package br.desafio.prodiga.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Service.ClienteServico;
import br.desafio.prodiga.dto.Cliente.DadosClientes;
import br.desafio.prodiga.dto.Cliente.DadosListaClientes;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")  
public class ClienteRestController {

    @Autowired
    private ClienteServico clienteServico;

    @GetMapping
    public ResponseEntity<List<DadosListaClientes>> listarClientes() {
        List<DadosListaClientes> clientes = clienteServico.listarTodos()
                .stream()
                .map(DadosListaClientes::new)
                .toList();
        return ResponseEntity.ok(clientes);
    }

    @PostMapping
    public ResponseEntity<?> salvarCliente(@RequestBody @Valid DadosClientes dados, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try {
            Cliente clienteSalvo = clienteServico.cadastrarCliente(dados);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCliente(@PathVariable Long id) {
        try {
            Cliente cliente = clienteServico.buscarPorId(id);
            return ResponseEntity.ok(new DadosListaClientes(cliente));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody @Valid DadosClientes dados, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try {
            Cliente clienteAtualizado = clienteServico.atualizarCliente(id, dados);
            return ResponseEntity.ok(clienteAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCliente(@PathVariable Long id) {
        try {
            clienteServico.excluirCliente(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir cliente: " + e.getMessage());
        }
    }
}