package com.franquias.api.dtos;

import com.franquias.api.models.ProdutoServico;
import com.franquias.api.models.enums.StatusProduto;

import java.math.BigDecimal;

public class ProdutoResponse {

    private Long id;
    private String nome;
    private String descricao;
    private Long categoriaId;
    private String categoriaNome;
    private BigDecimal precoBase;
    private StatusProduto status;

    public static ProdutoResponse fromEntity(ProdutoServico p) {
        ProdutoResponse dto = new ProdutoResponse();
        dto.id = p.getId();
        dto.nome = p.getNome();
        dto.descricao = p.getDescricao();
        dto.categoriaId = p.getCategoria().getId();
        dto.categoriaNome = p.getCategoria().getNome();
        dto.precoBase = p.getPrecoBase();
        dto.status = p.getStatus();
        return dto;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Long getCategoriaId() { return categoriaId; }
    public String getCategoriaNome() { return categoriaNome; }
    public BigDecimal getPrecoBase() { return precoBase; }
    public StatusProduto getStatus() { return status; }
}
