package br.desafio.prodiga.Controller;

import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController
;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Service.ClienteService;
import br.desafio.prodiga.dto.Cliente.ClienteForm;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/lista";
    }

    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        model.addAttribute("clienteForm", new ClienteForm());
        return "clientes/formulario";
    }

    @PostMapping("/salvar")
    public String salvarCliente(@ModelAttribute ClienteForm form) {
        clienteService.salvar(form);
        return "redirect:/clientes";
    }
      //TODO ARRUMAR 
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Cliente inválido"));
                
        
        ClienteForm form = new ClienteForm();
        form.setNome(cliente.getNome());
        form.setCpf(cliente.getCpf());
        form.setEmail(cliente.getEmail());
        form.setTelefone(cliente.getTelefone());
        form.setLogradouro(cliente.getEndereco().getLogradouro());
        form.setBairro(cliente.getEndereco().getBairro());
        form.setCep(cliente.getEndereco().getCep());
        form.setCidade(cliente.getEndereco().getCidade());
        form.setUf(cliente.getEndereco().getUf());
        
        model.addAttribute("clienteForm", form);
        model.addAttribute("clienteId", id);
        
        return "clientes/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable Long id) {
        clienteService.excluir(id);
        return "redirect:/clientes";
    }
}

