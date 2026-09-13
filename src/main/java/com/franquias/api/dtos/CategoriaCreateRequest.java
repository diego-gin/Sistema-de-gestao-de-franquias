package com.franquias.api.dtos;

import jakarta.validation.constraints.NotBlank;

public class CategoriaCreateRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String descricao;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
