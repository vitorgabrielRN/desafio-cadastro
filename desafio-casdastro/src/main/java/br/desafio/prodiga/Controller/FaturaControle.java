package br.desafio.prodiga.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Service.FaturaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/faturas")

public class FaturaControle {

    @Autowired
    private FaturaService faturaService;

    @GetMapping("/faturas")
    public String listarFaturas(Model model) {
        List<Fatura> listaFaturas = faturaService.listarFatura();
        model.addAttribute("listarFaturas", listaFaturas);
        return "listarFaturasPorCliente";
    }

    @PostMapping("/faturas/gerar")
    public String gerarFaturas(@RequestParam("ano") int ano, @RequestParam("mes") int mes, Model model) {
        try {
            faturaService.gerarFaturas(ano, mes);
            model.addAttribute("mensagem", "fatura gerada" + mes + " " + ano);
        } catch (Exception e) {
            model.addAttribute("ERRO", e.getMessage());
            model.addAttribute("erroTRACK", e.getStackTrace());
        }
        return "/faturasGeradas";
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
        return "/faturasGeradas";
    }

@GetMapping("/{id}/cancelar")
public String cancelamentoFatura(@PathVariable Long id, Model model){
    Fatura canceladaFatura = faturaService.cancelarFatura(id);

    if (canceladaFatura != null) {
            model.addAttribute("mensagem","Fatura" + canceladaFatura.getNumfatura() + "cancelada.");
        } else {
            model.addAttribute("ERRO", "Erro pra cancelar");
        }    
    return"/faturasGeradas";
}
//TODO preparar a verificacação final para e a criação dos HTML
//TODO reajuste do situacao
}