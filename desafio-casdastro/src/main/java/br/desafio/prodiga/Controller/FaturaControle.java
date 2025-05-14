package br.desafio.prodiga.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Service.ClienteServico;
import br.desafio.prodiga.Service.FaturaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/faturas")
public class FaturaControle {

    private ClienteServico clienteServico;

    private FaturaService faturaService;

    public FaturaControle(ClienteServico clienteServico, FaturaService faturaService) {
        this.clienteServico = clienteServico;
        this.faturaService = faturaService;
    }

    @GetMapping()
    public String listarFaturas(Model model) {
        Cliente cliente = new Cliente();
        model.addAttribute("listarFaturas", faturaService.listarFatura());
        model.addAttribute("cliente", cliente);
        return "listarFaturasPorCliente";
    }

    @GetMapping("/gerar")
    public String mostrarFormularioGerarFaturas(Model model) {
        model.addAttribute("clientes", clienteServico.listarClientes());
        return "gerarFaturas";
    }

    @PostMapping("/gerar")
    public String gerarFaturas(
            @RequestParam("ano") int ano,
            @RequestParam("mes") int mes,
            @RequestParam(value = "clienteId", required = true) Long clienteId,
            Model model) {

        try {
            faturaService.gerarFaturas(ano, mes, clienteId);
            model.addAttribute("mensagem", "Fatura gerada para " + mes + "/" + ano);
        } catch (Exception e) {
            model.addAttribute("ERRO", "Erro ao gerar fatura: " + e.getMessage());
        }
        return "redirect:/faturas";
    }

    @GetMapping("/situacao")
    public String situacaoFaturas(@RequestParam("situacao") String situacao, Model model) {
        List<Fatura> SituFaturas = faturaService.listarFaturasPorSituacao(situacao);
        model.addAttribute("fatura", SituFaturas);
        model.addAttribute("situacao", SituFaturas);
        return "situacaoFaturas";
    }

    @GetMapping("/faturas/{id}/pagamento")
    public String pagamentoFatura(@PathVariable Long id, Model model) {
        Fatura fatura = faturaService.buscarFaturaPorId(id);

        if (fatura != null) {
            model.addAttribute("fatura", fatura);
            return "pagarFatura";
        } else {
            model.addAttribute("ERRO", "fatura não encontrada.");
            return "/faturasGeradas";
        }
    }

    @PostMapping("/faturas/{id}/pagamento")
    public String pagandoFatura(@PathVariable Long id, @RequestParam("dataPagamento") String datapagamentoStr,
            Model model) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataPagamento = LocalDate.parse(datapagamentoStr, dateFormatter);
            Fatura faturapaga = faturaService.pagarFatura(id, dataPagamento);
            if (faturapaga != null) {
                model.addAttribute("mensagem", "Fatura" + faturapaga.getNumfatura() +
                        "paga em " + datapagamentoStr.formatted(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } else {
                model.addAttribute("ERRO", "ERRO Ao pagar");
            }
        } catch (Exception e) {
            model.addAttribute("ERROTRACK", e.getStackTrace());
            model.addAttribute("ERRO", e.getCause());
        }
        return "faturasGeradas";
    }

    @GetMapping("/{id}/cancelar")
    public String cancelamentoFatura(@PathVariable Long id, Model model) {
        Fatura canceladaFatura = faturaService.cancelarFatura(id);

        if (canceladaFatura != null) {
            model.addAttribute("mensagem", "Fatura" + canceladaFatura.getNumfatura() + "cancelada.");
        } else {
            model.addAttribute("ERRO", "Erro pra cancelar");
        }
        return "/faturasGeradas";
    }

    @GetMapping("/cliente/{id}")
    public String listarFaturasPorCliente(@PathVariable Long id, Model model) {
        Optional<Cliente> cliente = clienteServico.buscarClienteId(id);
        if (cliente.isPresent()) {
            model.addAttribute("cliente", cliente.get());
            model.addAttribute("faturas", faturaService.listarFaturasPorCliente(id));
            return "listarFaturasPorCliente";
        }
        return "redirect:/faturas";
    }

    // vamos ver se funciona! pelo amor
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
                Map.entry(12, "Dezembro"));
    }

}