package br.desafio.prodiga.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import br.desafio.prodiga.model.Fatura;
import br.desafio.prodiga.service.FaturaService;

@RestController
@RequestMapping("/api/faturas")
public class FaturaController {

    @Autowired
    private FaturaService faturaService;


    @PostMapping("/gerarcliente/{clienteId}")
    public ResponseEntity<Fatura> gerarFaturaParaCliente(
                                               @PathVariable Long clienteId,
                                               @RequestParam String mesAnoReferencia) {
        try {
            Fatura fatura = faturaService.gerarFaturaParaCliente(clienteId, mesAnoReferencia);
           
            return new ResponseEntity<>(fatura, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Fatura>> listarFaturasPorCliente(@PathVariable Long clienteId) {
        List<Fatura> faturas = faturaService.listarFaturasPorCliente(clienteId);
        return new ResponseEntity<>(faturas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fatura> buscarFaturaPorId(@PathVariable Long id) {
        return faturaService.buscarFaturaPorId(id)
                .map(fatura -> new ResponseEntity<>(fatura, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fatura> atualizarFatura(@PathVariable Long id, @RequestBody Fatura fatura) {
        try {
            Fatura faturaAtualizada = faturaService.atualizarFatura(id, fatura);
            return new ResponseEntity<>(faturaAtualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerFatura(@PathVariable Long id) {
        faturaService.removerFatura(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<Fatura> registrarPagamento(@PathVariable Long id) {
        try {
            Fatura faturaPaga = faturaService.registrarPagamento(id);
            return new ResponseEntity<>(faturaPaga, HttpStatus.OK);
        } catch (Exception  e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Fatura> cancelarFatura(@PathVariable Long id) {
        try {
            Fatura faturaCancelada = faturaService.cancelarFatura(id);
            return new ResponseEntity<>(faturaCancelada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

 
}