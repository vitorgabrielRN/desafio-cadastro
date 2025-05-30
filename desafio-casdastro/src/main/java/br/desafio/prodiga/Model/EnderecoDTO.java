package br.desafio.prodiga.Model;

public record EnderecoDTO(
    String logradouro,
    String bairro,
    String cep,
    String cidade,
    String uf
) {
    public EnderecoDTO(Endereco endereco) {
        this(
            endereco.getLogradouro(),
            endereco.getBairro(),
            endereco.getCep(),
            endereco.getCidade(),
            endereco.getUf()
        );
    }
}