package com.franquias.api.dtos;

import com.franquias.api.models.enums.StatusProduto;
import jakarta.validation.constraints.NotNull;

public class ProdutoStatusRequest {

    @NotNull(message = "Status é obrigatório")
    private StatusProduto status;

    public StatusProduto getStatus() { return status; }
    public void setStatus(StatusProduto status) { this.status = status; }
}
