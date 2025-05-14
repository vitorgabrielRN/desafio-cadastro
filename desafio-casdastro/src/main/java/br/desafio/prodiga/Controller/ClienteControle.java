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

@Controller
@RequestMapping("/")
public class ClienteControle {
   //Reformulação do ClienteController, baseado na minha nova versão dos HTML.
    @Autowired
    private ClienteServico clienteServico;

    @GetMapping
    public String menuPrincipal(){
    return "index";
}
    @GetMapping("/Cliente")
    public String listarClientes(Model model){
        List<Cliente> clientes = clienteServico.listarClientes();
        model.addAttribute("clientes", clientes);
        return "listaClientes";
    }
    @GetMapping("/Cliente/formulario")
    public String formulario(Model model){
        model.addAttribute("Cliente", new Cliente());
        return "formulario";
    }
    @PostMapping("/Cliente/salvar")
    public String salvarCliente(@Valid Cliente cliente, Model model){
        clienteServico.salvarCliente(cliente);
        model.addAttribute("mensagem", "Cliente criado");
        return "redirect:/Cliente";
    }
    @GetMapping("/Cliente/editar/{id}")
    public String novoFormulario(@PathVariable Long id, Model model){
        Optional<Cliente> clienteOptional = clienteServico.buscarClienteId(id);
        clienteOptional.ifPresentOrElse(
            Cliente -> model.addAttribute("Cliente", Cliente),
            () -> model.addAttribute("ERRO", "bugo"));
            return "formulario";
    }
    @PostMapping("/Cliente/atualizar/{id}")
    public String atualizarCliente(@PathVariable Long id, @Valid Cliente clienteAtualizado, Model model) {
        Optional<Cliente> clienteExistenteOptional = clienteServico.buscarClienteId(id);
        if (clienteExistenteOptional.isPresent()) {
            clienteAtualizado.setId(id);
            clienteServico.salvarCliente(clienteAtualizado);
            model.addAttribute("mensagem", "Cliente atualizado com sucesso");
            return "redirect:/Cliente";
        } else {
            model.addAttribute("mensagemErro", "erro, não deu pra atualizar");
            return "redirect:/Cliente";
        }
    }
     @GetMapping("/Cliente/excluir/{id}")
    public String excluirCliente(@PathVariable Long id) {
        clienteServico.excluirCliente(id);
        return "redirect:/Cliente";
    }
    @GetMapping("/Cliente/{id}")
     public ResponseEntity<Cliente> buscarClienteId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteServico.buscarClienteId(id);
        return cliente.map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
     }
}    