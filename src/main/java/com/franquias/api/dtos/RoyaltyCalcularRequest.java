package com.franquias.api.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class RoyaltyCalcularRequest {

    @NotNull(message = "unidadeId é obrigatório")
    private Long unidadeId;

    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "periodoReferencia deve estar no formato AAAA-MM")
    private String periodoReferencia;

    @NotNull(message = "percentual é obrigatório")
    @DecimalMin(value = "0.0", message = "percentual não pode ser negativo")
    @DecimalMax(value = "100.0", message = "percentual não pode ser maior que 100")
    private BigDecimal percentual;

    public Long getUnidadeId() { return unidadeId; }
    public void setUnidadeId(Long unidadeId) { this.unidadeId = unidadeId; }
    public String getPeriodoReferencia() { return periodoReferencia; }
    public void setPeriodoReferencia(String periodoReferencia) { this.periodoReferencia = periodoReferencia; }
    public BigDecimal getPercentual() { return percentual; }
    public void setPercentual(BigDecimal percentual) { this.percentual = percentual; }
}
