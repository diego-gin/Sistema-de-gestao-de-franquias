package com.franquias.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UnidadeCreateRequest {

    @NotNull(message = "franqueadoraId é obrigatório")
    private Long franqueadoraId;

    @NotNull(message = "franqueadoId é obrigatório")
    private Long franqueadoId;

    @NotBlank(message = "Nome fantasia é obrigatório")
    private String nomeFantasia;

    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "^[0-9./-]{14,18}$", message = "CNPJ em formato inválido")
    private String cnpj;

    private String endereco;
    private String cidade;
    private String estado;
    private String telefone;
    private String email;

    /** Formato esperado: AAAA-MM-DD. */
    @NotBlank(message = "Data de início é obrigatória")
    private String dataInicio;

    public Long getFranqueadoraId() { return franqueadoraId; }
    public void setFranqueadoraId(Long franqueadoraId) { this.franqueadoraId = franqueadoraId; }
    public Long getFranqueadoId() { return franqueadoId; }
    public void setFranqueadoId(Long franqueadoId) { this.franqueadoId = franqueadoId; }
    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
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
    public String getDataInicio() { return dataInicio; }
    public void setDataInicio(String dataInicio) { this.dataInicio = dataInicio; }
}
