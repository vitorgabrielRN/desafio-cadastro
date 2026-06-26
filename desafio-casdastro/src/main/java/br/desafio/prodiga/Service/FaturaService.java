package br.desafio.prodiga.Service;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.desafio.prodiga.BancoApi.BoletoService;
import br.desafio.prodiga.Enums.SituacaoFatura;
import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.Repository.FaturaRepository;
import br.desafio.prodiga.dto.BoletoResponse;
import br.desafio.prodiga.util.GeradorAleatorioUtils;
import jakarta.transaction.Transactional;

@Service
public class FaturaService {
    // Espero que esteja correto

    @Autowired
    private FaturaRepository faturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BoletoService boletoService;


    // Lembrar que tu apagou aquele que geraFaturaPraTodosOsClientes!
    // acho que nem vai precisar! kkkk acho mais facil ler assim agora.

    @Transactional // pra não ficar confuso
    public  Fatura gerarFaturaParaCliente(Long clienteId, String mesAnoReferencia) {
        //cliente, posso colocar a verificação dps

        Cliente cliente = clienteRepository.findById(clienteId)
                     .orElseThrow(() ->  new RuntimeException("Cliente nao encontrado" ));
        
        Fatura fatura = new Fatura();
        fatura.setCliente(cliente);
        fatura.setMesAnoReferencia(mesAnoReferencia);

        //valores principais(vi que é de bom tom separar, mesmo achando que não precisa)
        fatura.setValor(GeradorAleatorioUtils.gerarValorAleatorio());
        fatura.setDataVencimento(LocalDate.now().plusDays(30));
        fatura.setDataGeracao(GeradorAleatorioUtils.gerarDataVencimentoAleatoria());

        //fatura
        fatura.setSituacao(SituacaoFatura.GERADA);
        fatura.setNumeroFatura("FAT-" + System.currentTimeMillis());
        fatura = faturaRepository.save(fatura);

        //chamando o BoletoService aqui como o felipe disse   
        BoletoResponse boletoResponse = boletoService.registrarBoleto(fatura);
        fatura.setCodigoBoleto(boletoResponse.getCodigoBoleto());

        return faturaRepository.save(fatura);
    }

    public List<Fatura> listarFaturasPorCliente(Long clienteId) {
        return faturaRepository.findByClienteId(clienteId);
    }

    public Optional<Fatura> buscarFaturaPorId(Long id) {
        return faturaRepository.findById(id);
    }
    //coloquei aqui o datageração auto tbm! pra evitar erro! 
    @Transactional
    public Fatura atualizarFatura(Long id, Fatura faturaAtualizada) {
        return faturaRepository.findById(id).map(faturaExistente -> {
            faturaExistente.setMesAnoReferencia(faturaAtualizada.getMesAnoReferencia());
            faturaExistente.setValor(faturaAtualizada.getValor());
            faturaExistente.setDataVencimento(faturaAtualizada.getDataVencimento());
            faturaExistente.setSituacao(faturaAtualizada.getSituacao());
            faturaExistente.setCodigoBoleto(faturaAtualizada.getCodigoBoleto());
            faturaExistente.setDataGeracao(faturaAtualizada.getDataGeracao());
            faturaExistente.setDataPagamento(faturaAtualizada.getDataPagamento());
            return faturaRepository.save(faturaExistente);
        }).orElseThrow(() -> new RuntimeException("Fatura não encontrada com o ID: " + id));
    }

    @Transactional 
    public void removerFatura(Long id) {
        if (!faturaRepository.existsById(id)) {
            throw new IllegalArgumentException("Fatura com ID " + id + " não encontrada para remoção.");
        }
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
                        throw new IllegalStateException(
                                "A fatura não pode ser paga no estado atual: " + fatura.getSituacao());
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
                        throw new IllegalStateException(
                                "A fatura não pode ser cancelada no estado atual: " + fatura.getSituacao());
                    }
                })
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada com o ID: " + faturaId));
    }

   @Transactional
    public void atualizarFaturaViaWebhook(Long faturaId, SituacaoFatura novaSituacao, LocalDate dataEvento) {
        Fatura fatura = faturaRepository.findById(faturaId)
                .orElseThrow(() -> new IllegalArgumentException("Fatura com ID " + faturaId + " não encontrada para atualização via webhook."));

        System.out.println("Fatura ID " + faturaId + ": Atualizando situação de " + fatura.getSituacao() + " para " + novaSituacao);

        fatura.setSituacao(novaSituacao);
        if (novaSituacao == SituacaoFatura.PAGA) {
            fatura.setDataPagamento(dataEvento != null ? dataEvento : LocalDate.now());
        }


        faturaRepository.save(fatura);
        System.out.println("Fatura ID " + faturaId + " atualizada para: " + novaSituacao);
    }





}