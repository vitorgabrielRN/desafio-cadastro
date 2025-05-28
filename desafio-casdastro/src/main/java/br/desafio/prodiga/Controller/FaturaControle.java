package br.desafio.prodiga.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Service.ClienteServico;
import br.desafio.prodiga.Service.FaturaService;
import br.desafio.prodiga.dto.Cliente.DadosListaClientes;
import br.desafio.prodiga.dto.Fatura.DadosFatura;
import br.desafio.prodiga.dto.Fatura.DadosGerarFatura;
import br.desafio.prodiga.dto.Fatura.DadosPagamentoFatura;
import br.desafio.prodiga.dto.Fatura.Situacao;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/faturas")
public class FaturaControle {
    private static final String REDIRECT_FATURAS = "redirect:/faturas";
    private static final String ERRO_FATURA_NAO_ENCONTRADA = "Fatura não encontrada";

    private final ClienteServico clienteServico;
    private final FaturaService faturaService;

    public FaturaControle(ClienteServico clienteServico, FaturaService faturaService) {
        this.clienteServico = clienteServico;
        this.faturaService = faturaService;
    }


    @GetMapping
    public String listarFaturas(Model model) {
        List<DadosFatura> faturas = faturaService.listarFatura()
                .stream()
                .map(DadosFatura::new)
                .toList();
        model.addAttribute("faturas", faturas);
        return "faturas/lista";
    }

    @GetMapping("/gerar")
    public String mostrarFormularioGerarFaturas(Model model) {
        preencherModelGerarFatura(model);
        return "faturas/gerar";
    }

    private void preencherModelGerarFatura(Model model) {
        if (!model.containsAttribute("clientes")) {
            List<DadosListaClientes> clientes = clienteServico.listarTodos()
                    .stream()
                    .map(DadosListaClientes::new)
                    .toList();
            model.addAttribute("clientes", clientes);
        }

        if (!model.containsAttribute("dadosGerar")) {
            model.addAttribute("dadosGerar", new DadosGerarFatura(
                    LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue(),
                    null
            ));
        }
    }

    @PostMapping("/gerar")
    public String gerarFaturas(
            @Valid @ModelAttribute("dadosGerar") DadosGerarFatura dados,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            preencherModelGerarFatura(model);
            return "faturas/gerar";
        }

        try {
            validarDadosGeracao(dados);
            faturaService.gerarFaturas(dados);
            redirectAttributes.addFlashAttribute("mensagem",
                    String.format("Fatura gerada com sucesso para %s/%d",
                            getMeses().get(dados.mes()), dados.ano()));
            return REDIRECT_FATURAS;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return REDIRECT_FATURAS;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao gerar fatura: " + e.getMessage());
            return REDIRECT_FATURAS;
        }
    }

    private void validarDadosGeracao(DadosGerarFatura dados) {
        if (dados.mes() < 1 || dados.mes() > 12) {
            throw new IllegalArgumentException("Mês inválido");
        }
        if (dados.ano() < 2000 || dados.ano() > LocalDate.now().getYear() + 1) {
            throw new IllegalArgumentException("Ano inválido");
        }
    }

    @GetMapping("/{id}/pagamento")
    public String mostrarPagamentoFatura(@PathVariable Long id, Model model) {
        try {
            Fatura fatura = buscarFaturaOuLancarErro(id);

            if (fatura.getDataPagamento() != null) {
                throw new IllegalStateException("Esta fatura já foi paga");
            }

            if (fatura.getSituacao() == Situacao.GERADA) {
                throw new IllegalStateException("Não é possível pagar uma fatura cancelada");
            }

            model.addAttribute("fatura", new DadosFatura(fatura));
            model.addAttribute("dadosPagamento", new DadosPagamentoFatura(LocalDate.now()));
            return "faturas/pagamento";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar fatura: " + e.getMessage());
            return REDIRECT_FATURAS;
        }
    }

    @PostMapping("/{id}/pagamento")
    public String processarPagamento(
            @PathVariable Long id,
            @Valid @ModelAttribute("dadosPagamento") DadosPagamentoFatura dados,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            try {
                Fatura fatura = buscarFaturaOuLancarErro(id);
                model.addAttribute("fatura", new DadosFatura(fatura));
                return "faturas/pagamento";
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("erro", e.getMessage());
                return REDIRECT_FATURAS;
            }
        }

        try {
            validarDataPagamento(dados.dataPagamento());
            faturaService.pagarFatura(id, dados.dataPagamento());
            redirectAttributes.addFlashAttribute("mensagem", "Pagamento realizado com sucesso");
            return REDIRECT_FATURAS;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao processar pagamento: " + e.getMessage());
            return REDIRECT_FATURAS;
        }
    }

    private void validarDataPagamento(LocalDate dataPagamento) {
        if (dataPagamento == null) {
            throw new IllegalArgumentException("Data de pagamento é obrigatória");
        }
        if (dataPagamento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de pagamento não pode ser futura");
        }
    }

    @PostMapping("/{id}/cancelar")
    public String cancelarFatura(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            Fatura fatura = buscarFaturaOuLancarErro(id);
            if (fatura.getSituacao() == Situacao.CANCELADA) {
                throw new IllegalStateException("Fatura já está cancelada");
            }

            if (fatura.getDataPagamento() != null) {
                throw new IllegalStateException("Não é possível cancelar uma fatura já paga");
            }

            faturaService.cancelarFatura(id);
            redirectAttributes.addFlashAttribute("mensagem", "Fatura cancelada com sucesso");
            return REDIRECT_FATURAS;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao cancelar fatura: " + e.getMessage());
            return REDIRECT_FATURAS;
        }
    }

    @GetMapping("/cliente/{id}")
    public String listarFaturasPorCliente(@PathVariable Long id, Model model) {
        try {
            Cliente cliente = clienteServico.buscarPorId(id);
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente não encontrado");
            }

            List<DadosFatura> faturas = faturaService.listarFaturasPorCliente(id)
                    .stream()
                    .map(DadosFatura::new)
                    .toList();

            model.addAttribute("cliente", new DadosListaClientes(cliente));
            model.addAttribute("faturas", faturas);
            return "faturas/lista-cliente";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar faturas do cliente: " + e.getMessage());
            return REDIRECT_FATURAS;
        }
    }

    private Fatura buscarFaturaOuLancarErro(Long id) {
        return faturaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, ERRO_FATURA_NAO_ENCONTRADA));
    }

    @ModelAttribute("meses")
    public Map<Integer, String> getMeses() {
        return Map.ofEntries(
                Map.entry(1, "Janeiro"), Map.entry(2, "Fevereiro"), Map.entry(3, "Março"), Map.entry(4, "Abril"),
                Map.entry(5, "Maio"), Map.entry(6, "Junho"), Map.entry(7, "Julho"), Map.entry(8, "Agosto"),
                Map.entry(9, "Setembro"), Map.entry(10, "Outubro"), Map.entry(11, "Novembro"), Map.entry(12, "Dezembro")
        );
    }
}