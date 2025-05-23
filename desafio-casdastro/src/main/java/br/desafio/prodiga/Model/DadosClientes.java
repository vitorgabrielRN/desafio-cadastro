package br.desafio.prodiga.Model;



public record DadosClientes(
    String nome,
    
    String email,
    
    DadosEndereco endereco,
    
    String cpf,

    String telefone) {

}
