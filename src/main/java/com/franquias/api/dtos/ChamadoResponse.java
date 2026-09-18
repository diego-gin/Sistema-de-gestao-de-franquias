package com.franquias.api.dtos;

import com.franquias.api.models.ChamadoSuporte;
import com.franquias.api.models.enums.PrioridadeChamado;
import com.franquias.api.models.enums.StatusChamado;

public class ChamadoResponse {

    private Long id;
    private Long unidadeId;
    private String unidadeNome;
    private Long usuarioAberturaId;
    private String usuarioAberturaNome;
    private String categoria;
    private PrioridadeChamado prioridade;
    private String descricao;
    private StatusChamado status;
    private String dataAbertura;
    private String dataFechamento;

    public static ChamadoResponse fromEntity(ChamadoSuporte c) {
        ChamadoResponse dto = new ChamadoResponse();
        dto.id = c.getId();
        dto.unidadeId = c.getUnidade().getId();
        dto.unidadeNome = c.getUnidade().getNomeFantasia();
        dto.usuarioAberturaId = c.getUsuarioAbertura().getId();
        dto.usuarioAberturaNome = c.getUsuarioAbertura().getNome();
        dto.categoria = c.getCategoria();
        dto.prioridade = c.getPrioridade();
        dto.descricao = c.getDescricao();
        dto.status = c.getStatus();
        dto.dataAbertura = c.getDataAbertura().toString();
        dto.dataFechamento = c.getDataFechamento() != null ? c.getDataFechamento().toString() : null;
        return dto;
    }

    public Long getId() { return id; }
    public Long getUnidadeId() { return unidadeId; }
    public String getUnidadeNome() { return unidadeNome; }
    public Long getUsuarioAberturaId() { return usuarioAberturaId; }
    public String getUsuarioAberturaNome() { return usuarioAberturaNome; }
    public String getCategoria() { return categoria; }
    public PrioridadeChamado getPrioridade() { return prioridade; }
    public String getDescricao() { return descricao; }
    public StatusChamado getStatus() { return status; }
    public String getDataAbertura() { return dataAbertura; }
    public String getDataFechamento() { return dataFechamento; }
}
