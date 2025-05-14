package br.desafio.prodiga.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.FaturaRepository;

@Service
public class FaturaService {

    @Autowired
    private FaturaRepository faturaRepository;

    public List<Fatura> listarFatura() {
        return faturaRepository.findAll();
    }

    public Fatura buscarFaturaPorId(Long id) {
        Optional<Fatura> fatura = faturaRepository.findById(id);
        return fatura.orElse(null);
    }

    public Fatura pagarFatura(Long id, LocalDate dataPagamento) {
        Optional<Fatura> faturaOptional = faturaRepository.findById(id);
        if (faturaOptional.isPresent()) {
            Fatura fatura = faturaOptional.get();
            fatura.setDatapPagamento(dataPagamento);
            fatura.setSituacao("PAGA");
            return faturaRepository.save(fatura);
        }
        return null;
    }

    public Fatura cancelarFatura(Long id) {
        Optional<Fatura> faturaOptional = faturaRepository.findById(id);
        if (faturaOptional.isPresent()) {
            Fatura fatura = faturaOptional.get();
            fatura.setSituacao("CANCELADA");
            return faturaRepository.save(fatura);
        }
        return null;
    }

    public void salvarFatura(Fatura fatura) {
        faturaRepository.save(fatura);
    }

    public List<Fatura> listarFaturasPorSituacao(String situacao) {
        return faturaRepository.findBySituacao(situacao);
    }

    public List<Fatura> listarFaturasPorCliente(Cliente cliente) {
        return faturaRepository.findByCliente(cliente);
    }

    public void gerarFaturas(int ano, int mes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gerarFaturas'");
    }
}