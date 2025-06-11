package br.desafio.prodiga.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.desafio.prodiga.BancoApi.BoletoService;
import br.desafio.prodiga.BancoApi.WebhookRequest;
import br.desafio.prodiga.Enums.SituacaoFatura;
import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.Repository.FaturaRepository;
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

    @Transactional // TODO Espero que assim funcione
    public  Fatura gerarFaturaParaCliente(Cliente cliente, String mesAnoReferencia) {
        Fatura fatura = new Fatura();
        fatura.setCliente(cliente);
        fatura.setMesAnoReferencia(mesAnoReferencia);
        fatura.setValor(Fatura.gerarValorAleatorio());
        fatura.setDataVencimento(LocalDate.now().plusDays(30));
        fatura.setSituacao(SituacaoFatura.GERADA);
        fatura.setNumeroFatura("FAT-" + System.currentTimeMillis());
        fatura = faturaRepository.save(fatura);
        String codigoBoletoGerado = boletoService.registrarBoleto(fatura);
        fatura.setCodigoBoleto(codigoBoletoGerado);
        faturaRepository.save(fatura);

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
    public void webhook(WebhookRequest request){
        Fatura fatura = faturaRepository.findById(request.idFatura())
        .orElseThrow(() -> new RuntimeException("Fatura não encontrada " + request.idFatura()));

        if(fatura.getCodigoBoleto() == null || !fatura.getCodigoBoleto().equals(request.codigoBoleto())){
            System.err.println("Não achou o ID pra fatura " + request.idFatura());
        }
        fatura.setSituacao(request.novaSituacao());
        if(request.novaSituacao() == SituacaoFatura.PAGA){
            fatura.setDataPagamento(request.dataEvento());
        } else  {
            fatura.setDataPagamento(null);
        }
        faturaRepository.save(fatura);


        //TODO vale a pena fazer o endPoint manual?
        //acho que vou deixar ele comentado  Outra tem que criar
        // ENDPOINT do pro manual 

        ///
        /// @Transactional
        /// public Fatura pagManual(Long faturaId){
        /// Fatura fatura = faturaRepository.findById(faturaId)
        /// .orElseThrow(()-> new RuntimeExeception())}
        /// faz a aquele if pra situação e depois salva.
        /// if()

 
    }
}