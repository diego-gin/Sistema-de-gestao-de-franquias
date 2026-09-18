package com.franquias.api.dtos;

import com.franquias.api.models.Venda;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class VendaResponse {

    private Long id;
    private Long unidadeId;
    private String unidadeNome;
    private Long usuarioId;
    private String usuarioNome;
    private String dataVenda;
    private BigDecimal valorTotal;
    private List<ItemVendaResponse> itens;

    public static VendaResponse fromEntity(Venda v) {
        VendaResponse dto = new VendaResponse();
        dto.id = v.getId();
        dto.unidadeId = v.getUnidade().getId();
        dto.unidadeNome = v.getUnidade().getNomeFantasia();
        dto.usuarioId = v.getUsuario().getId();
        dto.usuarioNome = v.getUsuario().getNome();
        dto.dataVenda = v.getDataVenda().toString();
        dto.valorTotal = v.getValorTotal();
        dto.itens = v.getItens().stream()
                .map(ItemVendaResponse::fromEntity)
                .collect(Collectors.toList());
        return dto;
    }

    public Long getId() { return id; }
    public Long getUnidadeId() { return unidadeId; }
    public String getUnidadeNome() { return unidadeNome; }
    public Long getUsuarioId() { return usuarioId; }
    public String getUsuarioNome() { return usuarioNome; }
    public String getDataVenda() { return dataVenda; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public List<ItemVendaResponse> getItens() { return itens; }
}
