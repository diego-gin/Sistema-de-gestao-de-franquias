package com.franquias.api.dtos;

import com.franquias.api.models.Categoria;

public class CategoriaResponse {

    private Long id;
    private String nome;
    private String descricao;

    public static CategoriaResponse fromEntity(Categoria c) {
        CategoriaResponse dto = new CategoriaResponse();
        dto.id = c.getId();
        dto.nome = c.getNome();
        dto.descricao = c.getDescricao();
        return dto;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
}
