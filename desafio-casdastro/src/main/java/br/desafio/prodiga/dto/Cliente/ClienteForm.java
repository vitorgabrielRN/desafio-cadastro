package br.desafio.prodiga.dto.Cliente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteForm {
    @NotBlank private String nome;
    @NotBlank private String cpf;
    @NotBlank @Email private String email;
    @NotBlank private String telefone;
    
    
    @NotBlank private String logradouro;
    @NotBlank private String bairro;
    @NotBlank private String cep;
    @NotBlank private String cidade;
    @NotBlank @Size(min = 2, max = 2) private String uf;
}