package br.desafio.prodiga.Controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Service.ClienteServico;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/Cliente")
public class ClienteControle {

  @Autowired
  private ClienteServico clienteServico;

  @GetMapping
  public ResponseEntity<List<Cliente>> listarClientes() {
    List<Cliente> clientes = clienteServico.listarClientes();
    return new ResponseEntity<>(clientes, HttpStatus.OK);
  }

  @PostMapping("/salvar")
  public ResponseEntity<Cliente> salvarCliente(@Valid Cliente cliente) {
    Cliente clienteSalvo = clienteServico.salvarCliente(cliente);
    return new ResponseEntity<>(clienteSalvo, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Cliente> buscarClienteId(@PathVariable Long id) {
    Optional<Cliente> cliente = clienteServico.buscarClienteId(id);
    return cliente.map(c -> new ResponseEntity<>(c, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

  }

  @PutMapping("atualizar/{id}")
  public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
    Optional<Cliente> clienteExistenteOptional = clienteServico.buscarClienteId(id);
    if (clienteExistenteOptional.isPresent()) {
      clienteAtualizado.setId(id);
      Cliente clienteSalvo = clienteServico.salvarCliente(clienteAtualizado);
      return new ResponseEntity<>(clienteSalvo, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @SuppressWarnings("rawtypes")
  @GetMapping("/excluir/{id}")
  public ResponseEntity excluirCliente(@PathVariable Long id) {
    if (clienteServico.excluirCliente(id)) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/Formulario")
  public String mostrarFormulario(Model model) {
    model.addAttribute("Cliente", new Cliente());
    return "Formulario";
  }

  


}