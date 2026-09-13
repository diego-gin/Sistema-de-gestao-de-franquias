package com.franquias.api.dtos;

import com.franquias.api.models.Estoque;

public class EstoqueResponse {

    private Long id;
    private Long unidadeId;
    private String unidadeNome;
    private Long produtoServicoId;
    private String produtoNome;
    private Integer quantidadeAtual;
    private Integer quantidadeMinima;
    private boolean abaixoDoMinimo;

    public static EstoqueResponse fromEntity(Estoque e) {
        EstoqueResponse dto = new EstoqueResponse();
        dto.id = e.getId();
        dto.unidadeId = e.getUnidade().getId();
        dto.unidadeNome = e.getUnidade().getNomeFantasia();
        dto.produtoServicoId = e.getProdutoServico().getId();
        dto.produtoNome = e.getProdutoServico().getNome();
        dto.quantidadeAtual = e.getQuantidadeAtual();
        dto.quantidadeMinima = e.getQuantidadeMinima();
        dto.abaixoDoMinimo = e.isAbaixoDoMinimo();
        return dto;
    }

    public Long getId() { return id; }
    public Long getUnidadeId() { return unidadeId; }
    public String getUnidadeNome() { return unidadeNome; }
    public Long getProdutoServicoId() { return produtoServicoId; }
    public String getProdutoNome() { return produtoNome; }
    public Integer getQuantidadeAtual() { return quantidadeAtual; }
    public Integer getQuantidadeMinima() { return quantidadeMinima; }
    public boolean isAbaixoDoMinimo() { return abaixoDoMinimo; }
}
