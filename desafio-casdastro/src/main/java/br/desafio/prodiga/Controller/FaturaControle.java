package br.desafio.prodiga.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Service.FaturaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/faturas")

public class FaturaControle {

    @Autowired
    private FaturaService faturaService;

    @GetMapping("/faturas")
    public String listarFaturas(Model model) {
        model.addAttribute("faturas", faturaService.listarFatura());
        return "listarFaturasPorCliente"; 
    }

    @GetMapping("/faturas/gerar")
    public String formGeracaoFaturas() {
        return "gerarFaturas"; 
    }

    @PostMapping("/fatuas/gerar")
    public String gerarFaturas(@RequestParam("ano") int ano, @RequestParam("mes") int mes, Model model) {
        try {
            faturaService.gerarFaturas(ano, mes);
            model.addAttribute("mensagem", "Faturas geradas" + mes + " " + ano);
        } catch (Exception e) {
            model.addAttribute("mensagemErro", e.getMessage());
        }
        return "resultadoGeracao"; 
    }

    @GetMapping("/clientes/{clienteId}/faturas")
    public String listarFaturasPorCliente(@PathVariable Long clienteId, Model model) {
        List<Fatura> faturas = faturaService.buscarFaturaPorCliente(clienteId);
        model.addAttribute("faturas", faturas);
        model.addAttribute("clienteId", clienteId);
        return "listarFaturasPorCliente";
    }

    @GetMapping("/situacao")
    public String situacaoFaturas(@RequestParam("situacao") String situacao, Model model) {
        List<Fatura> faturas = faturaService.listarFaturasPorSituacao(situacao);
        model.addAttribute("faturas", faturas);
        model.addAttribute("situacao", situacao);
        return "situacaoFaturas"; 
    }

    @GetMapping("/{id}/pagar")
    public String listaFaturasPagas(@PathVariable Long id, Model model) {
        Fatura fatura = faturaService.buscarFaturaPorId(id);
        if (fatura != null) {
            model.addAttribute("fatura", fatura);
            return "pagarFatura";
        } else {
            model.addAttribute("mensagemErro", "Fatura não encontrada.");
            return "resultadoGeracao"; 
        }
    }

    @PostMapping("/{id}/pagar")
    public String pagarFatura(@PathVariable Long id, @RequestParam("dataPagamento") String dataPagamentoStr,
            Model model) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataPagamento = LocalDate.parse(dataPagamentoStr, dateFormatter);
            Fatura faturaPaga = faturaService.pagarFatura(id, dataPagamento);
            if (faturaPaga != null) {
                model.addAttribute("mensagem", "Fatura " + faturaPaga.getNumfatura() + " paga em "
                        + dataPagamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } else {
                model.addAttribute("mensagemErro", "Erro ao pagar a fatura.");
            }
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Formato de data inválido.");
        }
        return "resultadoGeracao"; 
    }

    @GetMapping("/{id}/cancelar")
    public String cancelarFatura(@PathVariable Long id, Model model) {
        Fatura faturaCancelada = faturaService.cancelarFatura(id);
        if (faturaCancelada != null) {
            model.addAttribute("mensagem", "Fatura " + faturaCancelada.getNumfatura() + " cancelada com sucesso.");
        } else {
            model.addAttribute("mensagemErro", "Erro ao cancelar a fatura.");
        }
        return "resultadoGeracao"; 
    }

}