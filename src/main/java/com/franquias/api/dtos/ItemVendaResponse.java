package com.franquias.api.dtos;

import com.franquias.api.models.ItemVenda;

import java.math.BigDecimal;

public class ItemVendaResponse {

    private Long produtoServicoId;
    private String produtoNome;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    public static ItemVendaResponse fromEntity(ItemVenda i) {
        ItemVendaResponse dto = new ItemVendaResponse();
        dto.produtoServicoId = i.getProdutoServico().getId();
        dto.produtoNome = i.getProdutoServico().getNome();
        dto.quantidade = i.getQuantidade();
        dto.precoUnitario = i.getPrecoUnitario();
        dto.subtotal = i.getSubtotal();
        return dto;
    }

    public Long getProdutoServicoId() { return produtoServicoId; }
    public String getProdutoNome() { return produtoNome; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
}
