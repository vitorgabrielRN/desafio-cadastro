package br.desafio.prodiga.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.Repository.FaturaRepository;
import jakarta.transaction.Transactional;

@Service

public class FaturaService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private FaturaRepository faturaRepository;

    @Transactional
    public void gerarFaturas(int ano, int mes) {
        List<Cliente> clientesAtivos = clienteRepository.findAll();
        if (clientesAtivos.isEmpty()) {
            throw new IllegalStateException();
        }
        Random random = new Random();
        LocalDate dataGeração = LocalDate.now();
        for (Cliente cliente : clientesAtivos) {
            Fatura fatura = new Fatura();
            fatura.setAno(ano);
            fatura.setMes(mes);
            fatura.setValor(10.0 + (100.0 - 10.0) * random.nextDouble());
            fatura.setSituacao("GERADA");
            fatura.setDataVencimento(dataGeração.plusDays(30));
            fatura.setNumfatura(" ");
            ;
            fatura.setCliente(cliente);
            faturaRepository.save(fatura);
        }
    }

    public List<Fatura> listarFatura() {
        return faturaRepository.findAll();
    }

    public Fatura buscarFaturaPorId(Long id) { 
        return faturaRepository.findById(id).orElse(null);
    }

    public List<Fatura> buscarFaturaPorCliente(Long clienteId) {
        return faturaRepository.findByCliente_Id(clienteId);
    }

    public List<Fatura> listarFaturasPorSituacao(String situacao) {
        return faturaRepository.findBySituacao(situacao);
    }

    @Transactional
    public Fatura pagarFatura(Long id, LocalDate dataPagamento) {
        Fatura fatura = faturaRepository.findById(id).orElse(null);
        if (fatura != null && !fatura.getSituacao().equals("PAGA") && !fatura.getSituacao().equals("CANCELADA")) {
            fatura.setSituacao("PAGA");
            fatura.setDatapPagamento(LocalDateTime.now());
            faturaRepository.save(fatura);
            return fatura;
        }
        return null; 
    }

    @Transactional
    public Fatura cancelarFatura(Long id) {
        Fatura fatura = faturaRepository.findById(id).orElse(null);
        if (fatura != null && !fatura.getSituacao().equals("PAGA") && !fatura.getSituacao().equals("CANCELADA")) {
            fatura.setSituacao("CANCELADA");
            faturaRepository.save(fatura);
            return fatura;
        }
        return null;

}
}