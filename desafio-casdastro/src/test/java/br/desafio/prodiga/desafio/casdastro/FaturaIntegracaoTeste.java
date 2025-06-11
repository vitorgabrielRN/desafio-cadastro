

package br.desafio.prodiga.desafio.casdastro;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.desafio.prodiga.BancoApi.BoletoRequest;
import br.desafio.prodiga.BancoApi.BoletoResponse;
import br.desafio.prodiga.BancoApi.WebhookRequest;
import br.desafio.prodiga.Enums.SituacaoFatura;
import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Model.Fatura;
import br.desafio.prodiga.Repository.ClienteRepository;
import br.desafio.prodiga.Repository.FaturaRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") 
class FaturaIntegracaoTeste {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FaturaRepository faturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @MockitoBean
    private RestTemplate restTemplate; 

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente clienteTeste;

    @BeforeEach
    @Transactional
    void setUp() {
        faturaRepository.deleteAll();
        clienteRepository.deleteAll();

        clienteTeste = new Cliente();
        clienteTeste.setNome("Cliente Teste Integracao");
        clienteTeste.setCpf("12345678900");
        clienteTeste.setEmail("integracao@teste.com");
        clienteTeste.setTelefone("11987654321");
        clienteTeste.setEndereco("Rua dos Testes, 100");
        clienteTeste = clienteRepository.save(clienteTeste);
    
    }

    @Test
    @Transactional
    void deveGerarFaturaEReceberCodigoBoletoDoBanco() throws Exception {
        // --- 1. Configurar o mock do RestTemplate para simular a resposta do banco ---
        String codigoBoletoFicticio = "BOLETO-REGISTRO-ABC123";
        BoletoResponse mockBoletoResponse = new BoletoResponse(
            codigoBoletoFicticio
        );

        when(restTemplate.postForEntity(
            eq("http://localhost:8080/api/boletos"),
                any(BoletoRequest.class),
                eq(BoletoResponse.class)
        )).thenReturn(new ResponseEntity<>(mockBoletoResponse, HttpStatus.OK));

        // --- 2. Chamar o endpoint da sua aplicação para gerar a fatura ---
        String mesAnoReferencia = "2025-08";
        mockMvc.perform(post("/api/faturas/gerarcliente/{clienteId}", clienteTeste.getId())
                .param("mesAnoReferencia", mesAnoReferencia)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().iscreated());

        // --- 3. Verificar o estado da fatura no banco de dados ---
        Optional<Fatura> faturaOptional = faturaRepository.findByClienteIdAndMesAnoReferencia(
                                                    clienteTeste.getId(), mesAnoReferencia);
        assertTrue(faturaOptional.isPresent(), "A fatura deve ser gerada e encontrada.");
        Fatura faturaGerada = faturaOptional.get();
        assertEquals(SituacaoFatura.GERADA, faturaGerada.getSituacao(), "Situação inicial deve ser GERADA.");
        assertEquals(codigoBoletoFicticio, faturaGerada.getCodigoBoleto(), "Código do boleto deve ser o retornado pelo banco mockado.");
        assertNull(faturaGerada.getDataPagamento(), "Data de pagamento deve ser nula para fatura GERADA.");
    }

    

    @Test
    @Transactional
    void deveProcessarCallbackDePagamentoComSucesso() throws Exception {
     
        Fatura faturaExistente = new Fatura();
        faturaExistente.setCliente(clienteTeste);
        faturaExistente.setMesAnoReferencia("2025-09");
        faturaExistente.setValor(150.0);
        faturaExistente.setDataVencimento(LocalDate.now().plusDays(30));
        faturaExistente.setSituacao(SituacaoFatura.GERADA);
        faturaExistente.setNumeroFatura("FAT-PAGAMENTO-999");
        faturaExistente.setCodigoBoleto("BOLETO-PAGO-XYZ");
        faturaExistente = faturaRepository.save(faturaExistente);

        WebhookRequest pagamentoCallback = new WebhookRequest(
                faturaExistente.getId(),
                faturaExistente.getCodigoBoleto(),
                SituacaoFatura.PAGA,
                LocalDate.now()
        );

        mockMvc.perform(post("/api/webhooks/status-boleto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pagamentoCallback)))
                .andExpect(status().isOk());

        // --- 3. Verificar o estado da fatura no banco de dados após o callback ---
        Optional<Fatura> faturaPagaOptional = faturaRepository.findById(faturaExistente.getId());
        assertTrue(faturaPagaOptional.isPresent(), "Fatura deve ser encontrada após callback de pagamento.");
        Fatura faturaPaga = faturaPagaOptional.get();
        assertEquals(SituacaoFatura.PAGA, faturaPaga.getSituacao(), "Situação deve ser PAGA após callback.");
        assertNotNull(faturaPaga.getDataPagamento(), "Data de pagamento não deve ser nula.");
        assertEquals(LocalDate.now(), faturaPaga.getDataPagamento(), "Data de pagamento deve ser a do evento.");
    }



    @Test
    @Transactional
    void deveProcessarCallbackDeCancelamentoComSucesso() throws Exception {
        // --- 1. Preparar uma fatura existente no banco de dados para ser cancelada ---
        Fatura faturaExistente = new Fatura();
        faturaExistente.setCliente(clienteTeste);
        faturaExistente.setMesAnoReferencia("2025-10");
        faturaExistente.setValor(200.0);
        faturaExistente.setDataVencimento(LocalDate.now().plusDays(45));
        faturaExistente.setSituacao(SituacaoFatura.GERADA);
        faturaExistente.setNumeroFatura("FAT-CANCELA-ABC");
        faturaExistente.setCodigoBoleto("BOLETO-CANCELA-XYZ");
        faturaExistente = faturaRepository.save(faturaExistente);

        // --- 2. Simular o callback de cancelamento para o endpoint da sua aplicação ---
        WebhookRequest cancelamentoCallback = new WebhookRequest(
                faturaExistente.getId(),
                faturaExistente.getCodigoBoleto(),
                SituacaoFatura.CANCELADA,
                LocalDate.now()
        );

        mockMvc.perform(post("/api/webhooks/status-boleto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelamentoCallback)))
                .andExpect(status().isOk());

        // --- 3. Verificar o estado da fatura no banco de dados após o callback ---
        Optional<Fatura> faturaCanceladaOptional = faturaRepository.findById(faturaExistente.getId());
        assertTrue(faturaCanceladaOptional.isPresent(), "Fatura deve ser encontrada após callback de cancelamento.");
        Fatura faturaCancelada = faturaCanceladaOptional.get();
        assertEquals(SituacaoFatura.CANCELADA, faturaCancelada.getSituacao(), "Situação deve ser CANCELADA após callback.");
        assertNull(faturaCancelada.getDataPagamento(), "Data de pagamento deve ser nula ao cancelar.");
    }

}