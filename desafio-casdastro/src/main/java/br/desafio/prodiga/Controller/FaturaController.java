package br.desafio.prodiga.Controller;


import br.desafio.prodiga.Model.Cliente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import br.desafio.prodiga.Service.ClienteService;
import br.desafio.prodiga.Service.FaturaService;


@Controller
@RequestMapping("/faturas")
public class FaturaController {
    private final FaturaService faturaService;
    private final ClienteService clienteService;

    public FaturaController(FaturaService faturaService, ClienteService clienteService) {
        this.faturaService = faturaService;
        this.clienteService = clienteService;
    }

    @GetMapping("/cliente/{clienteId}")
    public String listarPorCliente(@PathVariable Long clienteId, Model model) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        model.addAttribute("cliente", cliente);
        model.addAttribute("faturas", faturaService.listarPorCliente(clienteId));
        return "visualizar-faturas";
    }

    @PostMapping("/gerar/{clienteId}")
    public String gerarFatura(
            @PathVariable Long clienteId,
            @RequestParam String mesReferencia
    ) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        faturaService.gerarFatura(cliente, mesReferencia);
        return "redirect:/faturas/cliente/" + clienteId;
    }
}
