package br.desafio.prodiga.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Service.ClienteServico;
import br.desafio.prodiga.Service.FaturaService;
import br.desafio.prodiga.dto.Fatura.DadosFatura;
import br.desafio.prodiga.dto.Fatura.DadosGerarFatura;
import br.desafio.prodiga.dto.Fatura.DadosPagamentoFatura;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/faturas")
public class FaturaRestController {
    
    @Autowired
    private ClienteServico clienteServico;
    
    @Autowired
    private FaturaService faturaService;

    @GetMapping
    public ResponseEntity<List<DadosFatura>> listarFaturas() {
        List<DadosFatura> faturas = faturaService.listarFatura()
                .stream()
                .map(DadosFatura::new)
                .toList();
        return ResponseEntity.ok(faturas);
    }

    @PostMapping("/gerar")
    public ResponseEntity<?> gerarFaturas(
            @RequestBody @Valid DadosGerarFatura dados,
            BindingResult result) {
        
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        try {
            List<Fatura> faturasGeradas = faturaService.gerarFaturas(dados);
            List<DadosFatura> response = faturasGeradas.stream()
                    .map(DadosFatura::new)
                    .toList();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao gerar faturas: " + e.getMessage());
        }
    }

     @GetMapping("/{id}")
    public ResponseEntity<?> buscarFaturaPorId(@PathVariable Long id) {
        try {
            Fatura fatura = faturaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Fatura não encontrada"));
            return ResponseEntity.ok(new DadosFatura(fatura));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar fatura");
        }
    }
    //TODO reajustar isso pra atualizar o pagamento e atualizar no bano de dados
    //TODO Verificar se o transactional é valido aqui ou nõo
     @PutMapping("/{id}/pagar")
    @Transactional
    public ResponseEntity<?> pagarFatura(
            @PathVariable Long id,
            @RequestBody @Valid DadosPagamentoFatura dados) {
        
        try {
            Fatura fatura = faturaService.pagarFatura(id, dados.dataPagamento());
            return ResponseEntity.ok(new DadosFatura(fatura));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Erro ao processar pagamento: " + e.getMessage());
        }
    }

    //TODO Verificar se o transactional é valido aqui ou nõo
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarFatura(@PathVariable Long id) {
        try {
            Fatura fatura = faturaService.cancelarFatura(id);
            return ResponseEntity.ok(new DadosFatura(fatura));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao cancelar fatura: " + e.getMessage());
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> listarFaturasPorCliente(@PathVariable Long clienteId) {
        try {
            List<DadosFatura> faturas = faturaService.listarFaturasPorCliente(clienteId)
                    .stream()
                    .map(DadosFatura::new)
                    .toList();
            return ResponseEntity.ok(faturas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao listar faturas: " + e.getMessage());
        }
    }
}