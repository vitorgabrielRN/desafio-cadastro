package br.desafio.prodiga.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.Repository.FaturaRepository;
import br.desafio.prodiga.dto.Fatura.FaturaForm;
import br.desafio.prodiga.dto.Fatura.Situacao;

@Service
public class FaturaService {
    @Autowired
    private FaturaRepository faturaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    

    public void gerarFatura(FaturaForm form) {
        Cliente cliente = clienteRepository.findById(form.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        Fatura fatura = new Fatura();
        fatura.setCliente(cliente);
        fatura.setMes(form.getMes());
        fatura.setAno(form.getAno());
        fatura.setDataGeracao(LocalDateTime.now());
        fatura.setDataVencimento(LocalDate.now().plusDays(30));
        fatura.setNumeroFatura(gerarNumeroFatura());
        fatura.setValor(gerarValorAleatorio());
        fatura.setCodigoBoleto(gerarCodigoBoleto());
        fatura.setSituacao(Situacao.GERADA);

        faturaRepository.save(fatura);
    }

    private String gerarNumeroFatura() {
        return "FAT-" + LocalDateTime.now().getYear() + 
               String.format("%02d", LocalDateTime.now().getMonthValue()) + 
               "-" + new Random().nextInt(10000);
    }

    private Double gerarValorAleatorio() {
        return 10 + (90 * new Random().nextDouble());
    }

    private String gerarCodigoBoleto() {
        return UUID.randomUUID().toString().substring(0, 20).toUpperCase();
    }

    public List<Fatura> listarPorCliente(Long clienteId) {
        return faturaRepository.findByClienteId(clienteId);
    }
    public Optional<Cliente> buscarPorId(Long id) {
    return clienteRepository.findById(id);
}
}