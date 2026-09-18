package com.franquias.api.dtos;

import com.franquias.api.models.enums.PrioridadeChamado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChamadoCreateRequest {

    @NotNull(message = "unidadeId é obrigatório")
    private Long unidadeId;

    @NotBlank(message = "categoria é obrigatória")
    private String categoria;

    @NotNull(message = "prioridade é obrigatória")
    private PrioridadeChamado prioridade;

    @NotBlank(message = "descricao é obrigatória")
    private String descricao;

    public Long getUnidadeId() { return unidadeId; }
    public void setUnidadeId(Long unidadeId) { this.unidadeId = unidadeId; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public PrioridadeChamado getPrioridade() { return prioridade; }
    public void setPrioridade(PrioridadeChamado prioridade) { this.prioridade = prioridade; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
