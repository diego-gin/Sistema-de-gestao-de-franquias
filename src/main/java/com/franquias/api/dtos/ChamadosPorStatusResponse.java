package com.franquias.api.dtos;

import com.franquias.api.models.enums.StatusChamado;

public class ChamadosPorStatusResponse {

    private StatusChamado status;
    private Long quantidade;

    public ChamadosPorStatusResponse(StatusChamado status, Long quantidade) {
        this.status = status;
        this.quantidade = quantidade;
    }

    public StatusChamado getStatus() { return status; }
    public Long getQuantidade() { return quantidade; }
}
