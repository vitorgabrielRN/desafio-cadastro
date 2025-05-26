package br.desafio.prodiga.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.Service.ClienteServico;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClienteControle {
  @Autowired
  ClienteServico clienteServico;
  @Autowired
  ClienteRepository clienteRepository;

  @GetMapping
  public String listarClientes(Model model) {
    List<Cliente> clientes = clienteServico.listarTodos();
    model.addAttribute("clientes", clientes);
    return "clientes";
  }

  @PostMapping
  public String cadastrarCliente(@Valid @ModelAttribute("cliente") Cliente cliente,
      BindingResult result) {
    if (result.hasErrors()) {
      return "clientes/formulario";
    }
    clienteServico.salvarCliente(cliente);
    return "redirect:/clientes";

  }

  @GetMapping("/novo")
  public String cadastro(Model model) {
    model.addAttribute("cliente", new Cliente());
    return "clientes/formulario";
  }

  @GetMapping("/editar/{id}")
  public String mostrarFoEdicao(@PathVariable Long id, Model model) {
    Cliente cliente = clienteServico.buscarPorId(id)
        .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    model.addAttribute("cliente", cliente);
    return "clientes/formulario";
  }

  @PutMapping("/atualizar/{id}")
  public String atualizarCliente(@PathVariable Long id, @Valid @ModelAttribute("cliente") Cliente cliente,
                                   BindingResult result) {
    if (result.hasErrors()) {
      return "clientes/formulario";
    }
    cliente.setId(id);
    clienteServico.salvarCliente(cliente);
    return "redirect:/clientes";
  }

  @DeleteMapping("/{id}")
  public String excluirCliente(@PathVariable Long id) {
    clienteServico.excluirCliente(id);
    return "redirect:/clientes";
  }

}