package br.desafio.prodiga.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.desafio.prodiga.Enums.SituacaoFatura;
import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.Repository.FaturaRepository;
import jakarta.transaction.Transactional;

@Service
public class FaturaService {
    //Espero que esteja correto

    @Autowired
    private FaturaRepository faturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;
    //sera se vou precisar! pelo menos está ai.
    @Transactional
    public List<Fatura> gerarFaturasParaTodosClientes(String mesAnoReferencia) {
        List<Cliente> clientes = clienteRepository.findAll();
        List<Fatura> faturasGeradas = clientes.stream()
                .map(cliente -> gerarFaturaParaCliente(cliente, mesAnoReferencia))
                .toList();
        return faturaRepository.saveAll(faturasGeradas);
    }

    @Transactional
    public Fatura gerarFaturaParaCliente(Long clienteId, String mesAnoReferencia) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + clienteId));
        return gerarFaturaParaCliente(cliente, mesAnoReferencia);
    }
   //TODO espero que funcione as coisas 
    private Fatura gerarFaturaParaCliente(Cliente cliente, String mesAnoReferencia) {
        Fatura fatura = new Fatura();
        fatura.setCliente(cliente);
        fatura.setMesAnoReferencia(mesAnoReferencia);
        fatura.setValor(Fatura.gerarValorAleatorio()); 
        fatura.setDataVencimento(LocalDate.now().plusDays(30));
        fatura.setSituacao(SituacaoFatura.GERADA);
        fatura.setCodigoBoleto(Fatura.gerarCodigoBoleto()); 
        fatura.setNumeroFatura("FAT-" + System.currentTimeMillis()); 

        
        if (cliente.getFaturas() != null) {
            cliente.getFaturas().add(fatura);
        } else {
            cliente.setFaturas(List.of(fatura));
        }

        return fatura;
    }

    public List<Fatura> listarFaturasPorCliente(Long clienteId) {
        return faturaRepository.findByClienteId(clienteId);
    }

    public Optional<Fatura> buscarFaturaPorId(Long id) {
        return faturaRepository.findById(id);
    }

    public Fatura atualizarFatura(Long id, Fatura faturaAtualizada) {
        return faturaRepository.findById(id).map(faturaExistente -> {
            faturaExistente.setMesAnoReferencia(faturaAtualizada.getMesAnoReferencia());
            faturaExistente.setValor(faturaAtualizada.getValor());
            faturaExistente.setDataVencimento(faturaAtualizada.getDataVencimento());
            faturaExistente.setSituacao(faturaAtualizada.getSituacao());
            faturaExistente.setCodigoBoleto(faturaAtualizada.getCodigoBoleto());
            faturaExistente.setDataPagamento(faturaAtualizada.getDataPagamento());
            return faturaRepository.save(faturaExistente);
        }).orElseThrow(() -> new RuntimeException("Fatura não encontrada com o ID: " + id));
    }

    public void removerFatura(Long id) {
        faturaRepository.deleteById(id);
    }

    public Fatura registrarPagamento(Long faturaId) {
        return faturaRepository.findById(faturaId)
                .map(fatura -> {
                    if (fatura.getSituacao() == SituacaoFatura.GERADA) {
                        fatura.setSituacao(SituacaoFatura.PAGA);
                        fatura.setDataPagamento(LocalDate.now());
                        return faturaRepository.save(fatura);
        } else {
                        throw new IllegalStateException("A fatura não pode ser paga no estado atual: " + fatura.getSituacao());
                    }
                })
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada com o ID: " + faturaId));
    }

    public Fatura cancelarFatura(Long faturaId) {
        return faturaRepository.findById(faturaId)
                .map(fatura -> {
                    if (fatura.getSituacao() == SituacaoFatura.GERADA) {
                        fatura.setSituacao(SituacaoFatura.CANCELADA);
                        return faturaRepository.save(fatura);
                    } else {
                        throw new IllegalStateException("A fatura não pode ser cancelada no estado atual: " + fatura.getSituacao());
                    }
                })
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada com o ID: " + faturaId));
    }
}