package br.desafio.prodiga.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Service.ClienteServico;


@Controller
public class FormularioController {

  @Autowired
  private ClienteServico clienteServico;

     @GetMapping("/Formulario")
     public String mostrarFormulario(Model model) {
          model.addAttribute("cliente", new Client());
          return "Formulario";
     }

     @PostMapping("/cadastrar")
     public String cadastrarFormularioCliente(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes){
      clienteServico.salvarCliente(cliente);
      redirectAttributes.addFlashAttribute("mensagem", "Concluido");
      return"redirect:/Formulario";
     }
    

    }