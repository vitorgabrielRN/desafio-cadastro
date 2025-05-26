package br.desafio.prodiga.Model;

import java.io.Serializable;


import br.desafio.prodiga.dto.DadosClientes;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor




public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    private String nome;
    
    @Email
    private String email;
    
     @Embedded
    private Endereco endereco;
    
   // @Column(unique = true)
    private String cpf;
    
    
    private String telefone;
    
    private Fatura fatura;

    public Cliente(DadosClientes dados){
        
        this.nome = dados.nome();
        this.email = dados.email();
        this.cpf = dados.cpf();
        this.telefone = dados.telefone();
        this.endereco = new Endereco();
    }
}
