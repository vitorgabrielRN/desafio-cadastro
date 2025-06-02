package br.desafio.prodiga.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import br.desafio.prodiga.Model.SituacaoFatura;
import org.springframework.stereotype.Service;
import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.FaturaRepository;


@Service
public class FaturaService {
    private final FaturaRepository repository;
    private final BancoAPIService bancoAPIService;

    public FaturaService(FaturaRepository repository, BancoAPIService bancoAPIService) {
        this.repository = repository;
        this.bancoAPIService = bancoAPIService;
    }

    public Fatura gerarFatura(Cliente cliente, String mesReferencia) {
        Fatura fatura = new Fatura();
        fatura.setCliente(cliente);
        fatura.setMesReferencia(mesReferencia);
        fatura.setValor(gerarValorAleatorio());
        fatura.setDataVencimento(LocalDate.now().plusDays(30));

        String codigoBoleto = bancoAPIService.registrarBoleto(
                fatura.getNumeroFatura(),
                fatura.getDataVencimento(),
                fatura.getValor()
        );

        fatura.setCodigoBoleto(codigoBoleto);
        return repository.save(fatura);
    }

    private Double gerarValorAleatorio() {
        return 10 + (90 * new Random().nextDouble());
    }

    public List<Fatura> listarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    public void atualizarStatusPagamento(Long faturaId) {
        Fatura fatura = repository.findById(faturaId).orElseThrow();
        fatura.setSituacao(SituacaoFatura.PAGA);
        fatura.setDataPagamento(LocalDate.now());
        repository.save(fatura);
    }
}