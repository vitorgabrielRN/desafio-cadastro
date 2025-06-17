package br.desafio.prodiga.Controller;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Service.ClienteService;
import br.desafio.prodiga.Service.FaturaService;

@Controller
@RequestMapping("/")
public class WebController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private FaturaService faturaService;

    @GetMapping({ "/", "/clientes" })
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarClientes());
        model.addAttribute("cliente", new Cliente());
        return "clientes";
    }

  @PostMapping("/clientes/salvar")
    public String salvarCliente(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        try {
            clienteService.salvarCliente(cliente);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cadastrar cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/editar/{id}")
    public String exibirFormularioEdicaoCliente(@PathVariable Long id, Model model) {
        try {
            clienteService.buscarClientePorId(id).ifPresentOrElse(
                    cliente -> model.addAttribute("cliente", cliente),
                    () -> model.addAttribute("errorMessage", "Cliente não encontrado!"));
        } catch (Exception e) {
            System.err.println("deu erro pra editar o cliente" + e.getMessage());
            e.printStackTrace();

        }
        return "editar-cliente";
    }

    @PostMapping("/clientes/atualizar/{id}")
    public String atualizarCliente(@PathVariable Long id, @ModelAttribute Cliente cliente,
            RedirectAttributes redirectAttributes) {
        try {
            clienteService.atualizarCliente(id, cliente);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente atualizado com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/clientes";
    }
   //FATURA
    @DeleteMapping("/faturas/remover/{faturaId}") 
    public String removerFatura(@PathVariable Long faturaId, RedirectAttributes redirectAttributes) {
        Long clienteId = null;
        try {
           
            faturaService.buscarFaturaPorId(faturaId).ifPresent(fatura -> {
                redirectAttributes.addAttribute("clienteId", fatura.getCliente().getId());
            });

            faturaService.removerFatura(faturaId);
            redirectAttributes.addFlashAttribute("successMessage", "Fatura removida com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao remover fatura: " + e.getMessage());
            e.printStackTrace();

           
            return faturaService.buscarFaturaPorId(faturaId)
                    .map(f -> "redirect:/faturas/cliente/" + f.getCliente().getId())
                    .orElse("redirect:/clientes");
        }
      
        return "redirect:/faturas/cliente/{clienteId}";
    }
    //CLIENTE
    @DeleteMapping("/clientes/remover/{id}")
    public String removerCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.removerCliente(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao remover cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/clientes";
    }

    @GetMapping("/faturas/cliente/{clienteId}")
    public String listarFaturasPorCliente(@PathVariable Long clienteId, Model model) {
        clienteService.buscarClientePorId(clienteId).ifPresentOrElse(
                cliente -> {
                    model.addAttribute("cliente", cliente);
                    model.addAttribute("faturas", faturaService.listarFaturasPorCliente(clienteId));

                    YearMonth now = YearMonth.now();
                    model.addAttribute("mesAnoAtual", now.format(DateTimeFormatter.ofPattern("MM/yyyy")));
                },
                () -> model.addAttribute("errorMessage", "Cliente não encontrado!"));
        return "faturas-cliente";
    }

    @PostMapping("/faturas/gerar-para-cliente/{clienteId}")
    public String gerarFaturaParaCliente(@PathVariable Long clienteId,
                                         @RequestParam String mesAnoReferencia,
                                         RedirectAttributes redirectAttributes) {
        try {

            faturaService.gerarFaturaParaCliente(clienteId, mesAnoReferencia);
            redirectAttributes.addFlashAttribute("successMessage", "Fatura gerada com sucesso!");

            redirectAttributes.addAttribute("clienteId", clienteId);
            return "redirect:/faturas/cliente/{clienteId}";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage" + e.getMessage());
            redirectAttributes.addAttribute("clienteId", clienteId);
            return "redirect:/faturas/cliente/{clienteId}";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("Erro inesperado ao gerar fatura: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addAttribute("clienteId", clienteId);
            return "redirect:/faturas/cliente/{clienteId}";
        }
    }

    @PostMapping("/faturas/pagar/{faturaId}")
    public String registrarPagamento(@PathVariable Long faturaId, RedirectAttributes redirectAttributes) {
        try {
            Fatura fatura = faturaService.registrarPagamento(faturaId);
            redirectAttributes.addFlashAttribute("successMessage", "Fatura paga com sucesso!");
            return "redirect:/faturas/cliente/" + fatura.getCliente().getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao registrar pagamento: " + e.getMessage());
            e.printStackTrace();

            return faturaService.buscarFaturaPorId(faturaId)
                    .map(f -> "redirect:/faturas/cliente/" + f.getCliente().getId())
                    .orElse("redirect:/clientes");
        }
    }

    @PostMapping("/faturas/cancelar/{faturaId}")
    public String cancelarFatura(@PathVariable Long faturaId, RedirectAttributes redirectAttributes) {
        try {
            Fatura fatura = faturaService.cancelarFatura(faturaId);
            redirectAttributes.addFlashAttribute("successMessage", "Fatura cancelada com sucesso!");
            return "redirect:/faturas/cliente/" + fatura.getCliente().getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cancelar fatura: " + e.getMessage());
            e.printStackTrace();

            return faturaService.buscarFaturaPorId(faturaId)
                    .map(f -> "redirect:/faturas/cliente/" + f.getCliente().getId())
                    .orElse("redirect:/clientes");
        }
    }
}