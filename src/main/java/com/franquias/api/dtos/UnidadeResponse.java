package com.franquias.api.dtos;

import com.franquias.api.models.UnidadeFranqueada;
import com.franquias.api.models.enums.SituacaoUnidade;

public class UnidadeResponse {

    private Long id;
    private Long franqueadoraId;
    private String franqueadoraNome;
    private Long franqueadoId;
    private String franqueadoNome;
    private String nomeFantasia;
    private String cnpj;
    private String endereco;
    private String cidade;
    private String estado;
    private String telefone;
    private String email;
    private String dataInicio;
    private SituacaoUnidade situacao;

    public static UnidadeResponse fromEntity(UnidadeFranqueada u) {
        UnidadeResponse dto = new UnidadeResponse();
        dto.id = u.getId();
        dto.franqueadoraId = u.getFranqueadora().getId();
        dto.franqueadoraNome = u.getFranqueadora().getRazaoSocial();
        dto.franqueadoId = u.getFranqueado().getId();
        dto.franqueadoNome = u.getFranqueado().getNome();
        dto.nomeFantasia = u.getNomeFantasia();
        dto.cnpj = u.getCnpj();
        dto.endereco = u.getEndereco();
        dto.cidade = u.getCidade();
        dto.estado = u.getEstado();
        dto.telefone = u.getTelefone();
        dto.email = u.getEmail();
        dto.dataInicio = u.getDataInicio().toString();
        dto.situacao = u.getSituacao();
        return dto;
    }

    public Long getId() { return id; }
    public Long getFranqueadoraId() { return franqueadoraId; }
    public String getFranqueadoraNome() { return franqueadoraNome; }
    public Long getFranqueadoId() { return franqueadoId; }
    public String getFranqueadoNome() { return franqueadoNome; }
    public String getNomeFantasia() { return nomeFantasia; }
    public String getCnpj() { return cnpj; }
    public String getEndereco() { return endereco; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public String getDataInicio() { return dataInicio; }
    public SituacaoUnidade getSituacao() { return situacao; }
}
