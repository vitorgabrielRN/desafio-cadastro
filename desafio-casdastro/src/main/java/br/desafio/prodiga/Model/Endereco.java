package br.desafio.prodiga.Model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Endereco {
    private  String logradouro;
    private  String bairro;
    private  String cep;
    private  String cidade;
    private  String uf;



    public Endereco(DadosEndereco dados){
        this.logradouro = dados.logadouro();
        this.bairro = dados.bairro();
        this.cep = dados.cep();
        this.cidade = dados.cidade();
    }
}
