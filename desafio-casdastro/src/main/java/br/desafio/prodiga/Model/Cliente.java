package br.desafio.prodiga.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
@Table(name = "clientes")
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;
    private String email;
    private String endereco;
    private String telefone;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Fatura> faturas;

    public Cliente() {

    }
}