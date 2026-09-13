package com.franquias.api.dtos;

import com.franquias.api.models.enums.TipoMovimentacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MovimentacaoRequest {

    @NotNull(message = "unidadeId é obrigatório")
    private Long unidadeId;

    @NotNull(message = "produtoServicoId é obrigatório")
    private Long produtoServicoId;

    @NotNull(message = "tipo é obrigatório (ENTRADA ou SAIDA)")
    private TipoMovimentacao tipo;

    @NotNull(message = "quantidade é obrigatória")
    @Min(value = 1, message = "quantidade deve ser maior que zero")
    private Integer quantidade;

    private String observacao;

    /** Opcional — usado normalmente em movimentações de ENTRADA. */
    private Long fornecedorId;

    public Long getUnidadeId() { return unidadeId; }
    public void setUnidadeId(Long unidadeId) { this.unidadeId = unidadeId; }
    public Long getProdutoServicoId() { return produtoServicoId; }
    public void setProdutoServicoId(Long produtoServicoId) { this.produtoServicoId = produtoServicoId; }
    public TipoMovimentacao getTipo() { return tipo; }
    public void setTipo(TipoMovimentacao tipo) { this.tipo = tipo; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public Long getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(Long fornecedorId) { this.fornecedorId = fornecedorId; }
}
