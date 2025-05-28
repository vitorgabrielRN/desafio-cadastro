package br.desafio.prodiga.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Service.ClienteServico;
import br.desafio.prodiga.dto.Cliente.DadosListaClientes;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClienteController{ 
   
   @Autowired
   private ClienteServico clienteServico;
   
   
   @GetMapping("/clientes")
    public String listarClientes(Model model) {
        List<DadosListaClientes> clientes = clienteServico.listarTodos()
                .stream()
                .map(DadosListaClientes::new)
                .toList();
        model.addAttribute("clientes", clientes);
        return "clientes/lista";
    }

    @GetMapping("/clientes/novo")
    public String novoCliente(Model model) {
        if (!model.containsAttribute("cliente")) {
            model.addAttribute("cliente", new Cliente());
        }
        return "clientes/novo";
    }

    @PostMapping("/clientes")
    public String salvarCliente(
            @Valid @ModelAttribute("cliente") Cliente cliente,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.cliente", result);
            redirectAttributes.addFlashAttribute("cliente", cliente);
            return "redirect:/clientes/clientes";
        }
        try {
            clienteServico.salvar(cliente);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente cadastrado com sucesso");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar cliente: " + e.getMessage());
            return "redirect:/clientes/clientes/novo";
        }
        return "redirect:/clientes/clientes";
    }

    @GetMapping("/clientes/{id}/editar")
    public String editarCliente(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteServico.buscarPorId(id);
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente não encontrado");
            }
            model.addAttribute("cliente", cliente);
            return "clientes/editar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/clientes/clientes";
        }
    }

    @PutMapping("/clientes/{id}/editar")
    public String atualizarCliente(
            @PathVariable Long id,
            @Valid @ModelAttribute("cliente") Cliente cliente,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.cliente", result);
            redirectAttributes.addFlashAttribute("cliente", cliente);
            return "redirect:/clientes/clientes/" + id + "/editar";
        }
        try {
            cliente.setId(id);
            clienteServico.atualizar(cliente);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente atualizado com sucesso");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar cliente: " + e.getMessage());
            return "redirect:/faturas/clientes/" + id + "/editar";
        }
        return "redirect:/clientes/clientes";
    }

    @DeleteMapping("/clientes/{id}/excluir")
    public String excluirCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteServico.excluirCliente(id);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente excluído com sucesso");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir cliente: " + e.getMessage());
        }
        return "redirect:/clientes/clientes";
    }
 }