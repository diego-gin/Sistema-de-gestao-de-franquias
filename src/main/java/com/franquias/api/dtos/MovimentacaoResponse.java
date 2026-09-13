package com.franquias.api.dtos;

import com.franquias.api.models.MovimentacaoEstoque;
import com.franquias.api.models.enums.TipoMovimentacao;

public class MovimentacaoResponse {

    private Long id;
    private Long estoqueId;
    private String unidadeNome;
    private String produtoNome;
    private TipoMovimentacao tipo;
    private Integer quantidade;
    private String dataMovimentacao;
    private String observacao;
    private String fornecedorNome;

    public static MovimentacaoResponse fromEntity(MovimentacaoEstoque m) {
        MovimentacaoResponse dto = new MovimentacaoResponse();
        dto.id = m.getId();
        dto.estoqueId = m.getEstoque().getId();
        dto.unidadeNome = m.getEstoque().getUnidade().getNomeFantasia();
        dto.produtoNome = m.getEstoque().getProdutoServico().getNome();
        dto.tipo = m.getTipo();
        dto.quantidade = m.getQuantidade();
        dto.dataMovimentacao = m.getDataMovimentacao().toString();
        dto.observacao = m.getObservacao();
        dto.fornecedorNome = m.getFornecedor() != null ? m.getFornecedor().getNome() : null;
        return dto;
    }

    public Long getId() { return id; }
    public Long getEstoqueId() { return estoqueId; }
    public String getUnidadeNome() { return unidadeNome; }
    public String getProdutoNome() { return produtoNome; }
    public TipoMovimentacao getTipo() { return tipo; }
    public Integer getQuantidade() { return quantidade; }
    public String getDataMovimentacao() { return dataMovimentacao; }
    public String getObservacao() { return observacao; }
    public String getFornecedorNome() { return fornecedorNome; }
}
