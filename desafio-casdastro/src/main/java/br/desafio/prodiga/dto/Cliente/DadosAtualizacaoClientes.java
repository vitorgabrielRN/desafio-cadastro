package br.desafio.prodiga.dto.Cliente;

import br.desafio.prodiga.dto.DadosEndereco;

public record DadosAtualizacaoClientes( 
    
    String nome,
    String email,
    DadosEndereco endereco,
    String telefone) {}
