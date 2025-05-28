package br.desafio.prodiga.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.Repository.FaturaRepository;
import br.desafio.prodiga.dto.Fatura.DadosGerarFatura;
import br.desafio.prodiga.dto.Fatura.Situacao;
@Service
@Transactional
public class FaturaService {

    private final FaturaRepository faturaRepository;
    private final ClienteRepository clienteRepository;
    private final Random random = new Random();

    public FaturaService(FaturaRepository faturaRepository, ClienteRepository clienteRepository) {
        this.faturaRepository = faturaRepository;
        this.clienteRepository = clienteRepository;
    }

    

  //TODO Verificar novamente esse
    public List<Fatura> gerarFaturas(DadosGerarFatura dados) {
        validarDadosGeracao(dados);
        
        if (dados.clienteId() != null) {
            Cliente cliente = clienteRepository.findById(dados.clienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
            return List.of(criarFatura(dados, cliente));
        } else {
            return clienteRepository.findAll().stream()
                .map(cliente -> criarFatura(dados, cliente))
                .toList();
        }
    }
    //TODO  ajustar os valores que estão quebrados demais
	//TODO dificuldade para atualizar as faturas
	//TODO não está conseguindo pagar as faturas 
    private Fatura criarFatura(DadosGerarFatura dados, Cliente cliente) {
        Fatura fatura = new Fatura();
        fatura.setCliente(cliente);
        fatura.setAno(dados.ano());
        fatura.setMes(dados.mes());
        fatura.setValor(BigDecimal.valueOf(10 + random.nextDouble() * 90)
            .setScale(2, RoundingMode.HALF_UP).doubleValue());
        fatura.setDataVencimento(LocalDate.now().plusDays(30));
        fatura.setSituacao(Situacao.GERADA);
        fatura.setCodigoBoleto(gerarCodigoBoleto());
        fatura.gerarNumFatura();
        
        return faturaRepository.save(fatura);
    }
    //TODO precisa reajustar pra buscar por id e atualizar o dia do pagamento e a situacao no banco de dados "Transacionaç"
    public Fatura pagarFatura(Long id, LocalDate dataPagamento) {
        Fatura fatura = faturaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Fatura não encontrada"));
        
        if (fatura.getSituacao() != Situacao.GERADA) {
            throw new IllegalStateException("Só é possível pagar faturas com situação GERADA");
        }
        
        fatura.setSituacao(Situacao.PAGA);
        fatura.setDataPagamento(dataPagamento.atStartOfDay());
        return faturaRepository.save(fatura);
    }

    //TODO verificar se a validaçao está correta ou não
    private void validarPagamento(Fatura fatura, LocalDate dataPagamento) {
        if (fatura.getSituacao() == Situacao.PAGA) {
            throw new IllegalStateException("Fatura já está paga");
        }

        if (fatura.getSituacao() == Situacao.CANCELADA) {
            throw new IllegalStateException("Não é possível pagar uma fatura cancelada");
        }

        if (dataPagamento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de pagamento não pode ser futura");
        }
    }

    //TODO criar uma versãao pra não apagar os valores mas guardar as canceladas
    public Fatura cancelarFatura(Long id) {
        Fatura fatura = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Fatura não encontrada"));

        validarCancelamento(fatura);

        fatura.setSituacao(Situacao.CANCELADA);
        return faturaRepository.save(fatura);
    }

    private void validarCancelamento(Fatura fatura) {
        if (fatura.getSituacao() == Situacao.CANCELADA) {
            throw new IllegalStateException("Fatura já está cancelada");
        }

        if (fatura.getSituacao() == Situacao.PAGA) {
            throw new IllegalStateException("Não é possível cancelar uma fatura paga");
        }
    }

   
    public Optional<Fatura> buscarPorId(Long id) {
        return faturaRepository.findById(id);
    }

    public List<Fatura> listarFaturasPorCliente(Long clienteId) {
        return faturaRepository.findByClienteId(clienteId);
    }

    public List<Fatura> listarFatura() {
        return faturaRepository.findAll();
    }


    private void validarDadosGeracao(DadosGerarFatura dados) {
        if (dados.mes() < 1 || dados.mes() > 12) {
            throw new IllegalArgumentException("Mês inválido");
        }
        if (dados.ano() < 2000 || dados.ano() > LocalDate.now().getYear() + 1) {
            throw new IllegalArgumentException("Ano inválido");
        }
    }
    private String gerarCodigoBoleto() {
    Random random = new Random();
    StringBuilder codigo = new StringBuilder();
    for (int i = 0; i < 47; i++) {
        codigo.append(random.nextInt(10));
    }
    return codigo.toString();
}
  }
    