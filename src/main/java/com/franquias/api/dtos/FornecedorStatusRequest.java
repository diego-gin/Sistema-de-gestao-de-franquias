package com.franquias.api.dtos;

import com.franquias.api.models.enums.StatusFornecedor;
import jakarta.validation.constraints.NotNull;

public class FornecedorStatusRequest {

    @NotNull(message = "Status é obrigatório")
    private StatusFornecedor status;

    public StatusFornecedor getStatus() { return status; }
    public void setStatus(StatusFornecedor status) { this.status = status; }
}
