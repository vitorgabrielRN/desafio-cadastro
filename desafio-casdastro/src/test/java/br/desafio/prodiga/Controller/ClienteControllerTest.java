package br.desafio.prodiga.Controller;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Service.ClienteServico;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class ClienteControllerTest {



        @InjectMocks
        private ClienteController clienteController;

        @Mock
        private ClienteServico clienteServico;

        @Mock
        private BindingResult bindingResult;

        @Mock
        private RedirectAttributes redirectAttributes;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testAtualizarCliente_Success() {
            Long id = 1L;
            Cliente cliente = new Cliente();
            when(bindingResult.hasErrors()).thenReturn(false);

            String view = clienteController.atualizarCliente(id, cliente, bindingResult, redirectAttributes);

            assertEquals("redirect:/faturas/clientes", view);
            assertEquals(id, cliente.getId());
            verify(clienteServico).atualizar(cliente);
            verify(redirectAttributes).addFlashAttribute("mensagem", "Cliente atualizado com sucesso");
        }

        @Test
        void testAtualizarCliente_HasErrors() {
            Long id = 2L;
            Cliente cliente = new Cliente();
            when(bindingResult.hasErrors()).thenReturn(true);

            String view = clienteController.atualizarCliente(id, cliente, bindingResult, redirectAttributes);

            assertEquals("redirect:/faturas/clientes/" + id + "/editar", view);
            verify(redirectAttributes).addFlashAttribute("org.springframework.validation.BindingResult.cliente", bindingResult);
            verify(redirectAttributes).addFlashAttribute("cliente", cliente);
            verifyNoInteractions(clienteServico);
        }

        @Test
        void testAtualizarCliente_Exception() {
            Long id = 3L;
            Cliente cliente = new Cliente();
            when(bindingResult.hasErrors()).thenReturn(false);
            doThrow(new RuntimeException("Erro de atualização")).when(clienteServico).atualizar(any(Cliente.class));

            String view = clienteController.atualizarCliente(id, cliente, bindingResult, redirectAttributes);

            assertEquals("redirect:/faturas/clientes/" + id + "/editar", view);
            verify(redirectAttributes).addFlashAttribute(eq("erro"), contains("Erro ao atualizar cliente: Erro de atualização"));
        }

    }

