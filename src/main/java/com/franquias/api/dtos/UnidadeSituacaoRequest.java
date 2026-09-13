package com.franquias.api.dtos;

import com.franquias.api.models.enums.SituacaoUnidade;
import jakarta.validation.constraints.NotNull;

public class UnidadeSituacaoRequest {

    @NotNull(message = "Situação é obrigatória")
    private SituacaoUnidade situacao;

    public SituacaoUnidade getSituacao() { return situacao; }
    public void setSituacao(SituacaoUnidade situacao) { this.situacao = situacao; }
}
