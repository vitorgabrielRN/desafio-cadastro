package br.desafio.prodiga.dto.Fatura;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaturaForm {
    @NotNull private Long clienteId;
    @Min(1) @Max(12) private int mes;
    @Min(2023) private int ano;
}