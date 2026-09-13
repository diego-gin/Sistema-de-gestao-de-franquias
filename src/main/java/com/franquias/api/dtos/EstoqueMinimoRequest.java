package com.franquias.api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class EstoqueMinimoRequest {

    @NotNull(message = "quantidadeMinima é obrigatória")
    @Min(value = 0, message = "quantidadeMinima não pode ser negativa")
    private Integer quantidadeMinima;

    public Integer getQuantidadeMinima() { return quantidadeMinima; }
    public void setQuantidadeMinima(Integer quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }
}
