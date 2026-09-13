package com.franquias.api.dtos;

import jakarta.validation.constraints.NotBlank;

public class UnidadeUpdateRequest {

    @NotBlank(message = "Nome fantasia é obrigatório")
    private String nomeFantasia;

    private String endereco;
    private String cidade;
    private String estado;
    private String telefone;
    private String email;

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
