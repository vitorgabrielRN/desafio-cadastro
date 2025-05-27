package br.desafio.prodiga.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Model.Situacao;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.Repository.FaturaRepository;
import br.desafio.prodiga.dto.DadosFatura;
import br.desafio.prodiga.dto.DadosGerarFatura;

@Service
public class FaturaService {

    @Autowired
    private FaturaRepository faturaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    public void gerarFaturas(DadosGerarFatura dados) {
        Cliente cliente = clienteRepository.findById(dados.clienteId())
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        Fatura fatura = new Fatura();
        fatura.setAno(dados.ano());
        fatura.setMes(dados.mes());
        fatura.setValor(100.0); 
        fatura.setSituacao(Situacao.GERADA);
        fatura.setCliente(cliente);
        fatura.gerarNumFatura();
        
        faturaRepository.save(fatura);
    }

    public Fatura pagarFatura(Long id, LocalDate dataPagamento) {
        Fatura fatura = faturaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Fatura não encontrada"));
        fatura.setSituacao(Situacao.PAGA);
        fatura.setDataPagamento(dataPagamento.atStartOfDay());
        return faturaRepository.save(fatura);
    }

    public Fatura cancelarFatura(Long id) {
        Fatura fatura = faturaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Fatura não encontrada"));
        fatura.setSituacao(Situacao.CANCELADA);
        return faturaRepository.save(fatura);
    }

    public Optional<Fatura> buscarPorId(Long id) {
        return faturaRepository.findById(id);
    }

    public List<Fatura> listarFaturasPorCliente(Long clienteId) {
        return faturaRepository.findByClienteId(clienteId);
    }
     public List<Fatura> listarTodos() {
        return faturaRepository.findAll();
    }

     public List<Fatura>listarFatura() {
       return faturaRepository.findAll();
     }
}