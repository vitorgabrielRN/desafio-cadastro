package br.desafio.prodiga.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.FaturaRepository;

@Service
public class FaturaService {

    private FaturaRepository faturaRepository;
    
    @Autowired
    private ClienteServico clienteServico;

    public FaturaService(FaturaRepository faturaRepository) {
        this.faturaRepository = faturaRepository;
    }
    
    public List<Fatura> listarFaturasPorCliente(Long clienteId) {
        return faturaRepository.findByClienteId(clienteId);
    }
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
    
    public void gerarFaturas(int ano, int mes, Long clienteId){
        Cliente cliente = clienteServico.buscarClienteId(clienteId).orElseThrow(() -> new RuntimeException("CLIENTE NÃO ENCONTRADO"));
        
        Fatura fatura =  new Fatura();
        fatura.setCliente(cliente);
        fatura.setAno(ano);
        fatura.setMes(mes);
        fatura.setValor(100.0);
        fatura.setSituacao("GERADA");
        fatura.setDataVencimento(LocalDate.now().plusDays(30));
        fatura.GerarNumFatura();
        try{
            faturaRepository.save(fatura);   
    }catch(Exception e){
        System.out.println("ERRO:" + e.getMessage());
        e.printStackTrace();
    }   }
}