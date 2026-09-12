package com.franquias.api.dtos;

import com.franquias.api.models.Usuario;
import com.franquias.api.models.enums.Perfil;

/**
 * DTO de saída — nunca expõe o hash da senha, apenas dados seguros de exibir.
 */
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private Perfil perfil;
    private Long unidadeId;
    private String unidadeNome;
    private boolean ativo;
    private String criadoEm;

    public static UsuarioResponse fromEntity(Usuario usuario) {
        UsuarioResponse dto = new UsuarioResponse();
        dto.id = usuario.getId();
        dto.nome = usuario.getNome();
        dto.email = usuario.getEmail();
        dto.perfil = usuario.getPerfil();
        dto.ativo = usuario.isAtivo();
        dto.criadoEm = usuario.getCriadoEm().toString();

        if (usuario.getUnidade() != null) {
            dto.unidadeId = usuario.getUnidade().getId();
            dto.unidadeNome = usuario.getUnidade().getNomeFantasia();
        }

        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public Long getUnidadeId() {
        return unidadeId;
    }

    public String getUnidadeNome() {
        return unidadeNome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public String getCriadoEm() {
        return criadoEm;
    }
}
