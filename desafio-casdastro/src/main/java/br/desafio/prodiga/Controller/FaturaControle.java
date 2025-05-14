package br.desafio.prodiga.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Service.ClienteServico;
import br.desafio.prodiga.Service.FaturaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/faturas")
public class FaturaControle {
    // Reformulação do FaturaController, baseado na minha nova ideia dos HTML.
    // meio que estou refatorando os codigos.
    @Autowired
    private FaturaService faturaService;

    @GetMapping("/gerar")
    public String formulariogeracacaoFaturas() {
        return "gerarFaturas";
    }

    @PostMapping("/gerar")
    public String gerarFaturas(@RequestParam("ano") int ano,
            @RequestParam("mes") int mes, Model model) {
        try {    
            List<Cliente> clientes = ClienteServico.listarClientes();
            int faturasGeradas = 0;
            Random random = new Random();

            for (Cliente cliente : clientes) {
                Fatura novaFatura = new Fatura();
                novaFatura.setCliente(cliente);
                novaFatura.setAnoReferencia(ano);
                novaFatura.setMesReferencia(mes);
                novaFatura.setValor(10.0 + (100.0 - 10.0) * random.nextDouble());
                LocalDate dataGeracao = LocalDate.of(ano, mes, LocalDate.now().getDayOfMonth());
                novaFatura.setDataVencimento(dataGeracao.plusDays(30));
                novaFatura.setSituacao("GERADA");
                novaFatura.setNumfatura(UUID.randomUUID().toString());
                novaFatura.setCodigoBoleto(UUID.randomUUID().toString().substring(0, 20));
                faturaService.salvarFatura(novaFatura);
                faturasGeradas++;
                model.addAttribute("mensagem", faturasGeradas + " faturas geradas para " + mes + "/" + ano);
        } } catch (Exception e) {
            model.addAttribute("ERRO", "Erro ao gerar faturas: " + e.getMessage());
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

    @GetMapping("/{id}/pagamento")
    public String pagamentoFatura(@PathVariable Long id, Model model) {
        Fatura fatura = faturaService.buscarFaturaPorId(id);

        if (fatura != null) {
            model.addAttribute("fatura", fatura);
            return "pagarFatura";
        } else {
            model.addAttribute("ERRO", "Fatura não encontrada.");
            return "/faturasGeradas";
        }
    }

    @PostMapping("/{id}/pagamento")
    public String pagandoFatura(@PathVariable Long id, @RequestParam("dataPagamento") String datapagamentoStr,
            Model model) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataPagamento = LocalDate.parse(datapagamentoStr, dateFormatter);
            Fatura faturapaga = faturaService.pagarFatura(id, dataPagamento);
            if (faturapaga != null) {
                model.addAttribute("mensagem", "Fatura " + faturapaga.getNumfatura() +
                        " paga em " + dataPagamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } else {
                model.addAttribute("ERRO", "Erro ao pagar a fatura.");
            }
        } catch (Exception e) {
            model.addAttribute("ERROTRACK", e.getStackTrace());
            model.addAttribute("ERRO", e.getCause());
        }
        return "/faturasGeradas";
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
}