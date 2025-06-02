package br.desafio.prodiga.Controller;

import br.desafio.prodiga.Service.FaturaService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/banco")
public class BancoAPIController {
    private final FaturaService faturaService;

    public BancoAPIController(FaturaService faturaService) {
        this.faturaService = faturaService;
    }

    @PostMapping("/pagamento/{faturaId}")
    public String registrarPagamento(@PathVariable Long faturaId) {
        faturaService.atualizarStatusPagamento(faturaId);
        return "Pagamento registrado com sucesso";
    }
}