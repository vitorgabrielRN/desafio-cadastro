package br.desafio.prodiga.dto.Fatura;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DadosGerarFatura(
        @NotNull @Min(2000) @Max(2100) int ano,
        @NotNull @Min(1) @Max(12) int mes,
        Long clienteId) {
}