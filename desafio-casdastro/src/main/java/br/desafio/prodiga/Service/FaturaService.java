package br.desafio.prodiga.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Model.Situacao;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.Repository.FaturaRepository;
import br.desafio.prodiga.dto.DataFaturas;

@Service
public class FaturaService {

    @Autowired
    private FaturaRepository faturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public void gerarFaturas(DataFaturas dataFaturas, Long id) {
        Cliente clienteFatura = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CLIENTE NAO ENCONTRADO"));

        Fatura fatura = new Fatura();
        fatura.setAno(dataFaturas.ano());
        fatura.setMes(dataFaturas.mes());
        fatura.setValor(100.0);
        fatura.setSituacao(Situacao.GERADA);
        fatura.setCliente(clienteFatura);
        faturaRepository.save(fatura);
    }

   

    public void salvarFatura(Fatura fatura) {
        faturaRepository.save(fatura);
    }

    public List<Fatura> listarFatura() {
        return faturaRepository.findAll();
    }

    public Optional<Fatura> listaPorId(Long id) {
        return faturaRepository.findById(id);
    }

    public List<Fatura> listarFaturasPorSituacao(Situacao situacao) {
        return faturaRepository.findBySituacao(situacao);
    }

    public Fatura pagarFatura(Long id, DataFaturas dataFaturas) {
        Optional<Fatura> faturaOptional = faturaRepository.findById(id);
        if (faturaOptional.isPresent()) {
            Fatura fatura = faturaOptional.get();
            fatura.setDataPagamento(dataFaturas.dataPagamento());
            fatura.setSituacao(Situacao.PAGA);
            return faturaRepository.save(fatura);
        } else {
            throw new RuntimeException("ERRO AO CRIAR FATURA");
        }
    }

    public Fatura cancelFatura(Long id) {
        Optional<Fatura> faturaCancelada = faturaRepository.findById(id);
        if (faturaCancelada.isPresent()) {
            Fatura fatura = faturaCancelada.get();
            fatura.setSituacao(Situacao.CANCELADA);
            return faturaRepository.save(fatura);
        } else {
            throw new RuntimeException("ERRO AO CANCELAR FATURA");
        }
    }
}