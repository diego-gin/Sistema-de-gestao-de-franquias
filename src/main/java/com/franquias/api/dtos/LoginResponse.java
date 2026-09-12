package com.franquias.api.dtos;

import com.franquias.api.models.enums.Perfil;

public class LoginResponse {

    private String token;
    private String nome;
    private Perfil perfil;

    public LoginResponse() {
    }

    public LoginResponse(String token, String nome, Perfil perfil) {
        this.token = token;
        this.nome = nome;
        this.perfil = perfil;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
