package br.desafio.prodiga.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Service.ClienteService;
import br.desafio.prodiga.Service.FaturaService;
import br.desafio.prodiga.dto.Fatura.FaturaForm;
import br.desafio.prodiga.dto.Fatura.Situacao;

@Controller
@RequestMapping("/faturas")
public class FaturaController {

    @Autowired
    private FaturaService faturaService;
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/gerar")
    public String formularioGerarFatura(Model model) {
        model.addAttribute("faturaForm", new FaturaForm());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("meses", getMeses());
        return "faturas/formulario";
    }

    @PostMapping("/gerar")
    public String gerarFatura(@ModelAttribute FaturaForm form) {
        faturaService.gerarFatura(form);
        return "redirect:/clientes/" + form.getClienteId() + "/faturas";
    }
    //TODO ARRUMAR AQUI 
    @GetMapping("/cliente/{clienteId}")
    public String listarFaturasCliente(@PathVariable Long clienteId, Model model) {
        model.addAttribute("faturas", faturaService.listarPorCliente(clienteId));
        model.addAttribute("cliente", clienteService.buscarPorId(clienteId).orElseThrow());
        return "faturas/lista";
    }

    @ModelAttribute("meses")
    public Map<Integer, String> getMeses() {
        return Map.ofEntries(
            Map.entry(1, "Janeiro"),
            Map.entry(2, "Fevereiro"),
            Map.entry(3, "Março"),
            Map.entry(4, "Abril"),
            Map.entry(5, "Maio"),
            Map.entry(6, "Junho"),
            Map.entry(7, "Julho"),
            Map.entry(8, "Agosto"),
            Map.entry(9, "Setembro"),
            Map.entry(10, "Outubro"),
            Map.entry(11, "Novembro"),
            Map.entry(12, "Dezembro")
        );
    }
}
