 package br.desafio.prodiga.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.desafio.prodiga.Model.Cliente;
import br.desafio.prodiga.Service.ClienteServico;

@Controller
@RequestMapping("/clientes")

public class ClienteControle {
      @GetMapping
     public ResponseEntity<List<Cliente>> listarClientes(){ 
        List<Cliente> clientes = ClienteServico.listarClientes();
        return  new ResponseEntity<>(clientes, HttpStatus.OK);
     }

     @PostMapping("/salvar")
     public String salvarCliente(Cliente cliente) {
        clienteServico.cadastrarCliente(cliente.getNome(), cliente.getCpf(), cliente.getEmail(), cliente.getEndereco(), cliente.getTelefone(), cliente.getSenha());
         ClienteServico.salvarCliente(cliente);
         return 
     }

    
     
     @PostMapping("/atualizar")
     public String atualizarCliente(Cliente cliente) {
       ClienteServico.salvarCliente(cliente);
       return cliente.map(c -> new ResponseEntity<>(c, HttpStatus.OK))
           .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
     }
     @GetMapping("/excluir/{id}")
     public String excluirCliente(@PathVariable Long id) {
        ClienteServico.excluirCliente(id);
        return 
     }






}  

 
