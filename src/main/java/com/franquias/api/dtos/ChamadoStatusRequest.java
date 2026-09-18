package com.franquias.api.dtos;

import com.franquias.api.models.enums.StatusChamado;
import jakarta.validation.constraints.NotNull;

public class ChamadoStatusRequest {

    @NotNull(message = "status é obrigatório")
    private StatusChamado status;

    public StatusChamado getStatus() { return status; }
    public void setStatus(StatusChamado status) { this.status = status; }
}
