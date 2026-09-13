package com.franquias.api.dtos;

import com.franquias.api.models.Franqueadora;

public class FranqueadoraResponse {

    private Long id;
    private String razaoSocial;
    private String cnpj;
    private String dataFundacao;
    private boolean ativa;

    public static FranqueadoraResponse fromEntity(Franqueadora f) {
        FranqueadoraResponse dto = new FranqueadoraResponse();
        dto.id = f.getId();
        dto.razaoSocial = f.getRazaoSocial();
        dto.cnpj = f.getCnpj();
        dto.dataFundacao = f.getDataFundacao() != null ? f.getDataFundacao().toString() : null;
        dto.ativa = f.isAtiva();
        return dto;
    }

    public Long getId() { return id; }
    public String getRazaoSocial() { return razaoSocial; }
    public String getCnpj() { return cnpj; }
    public String getDataFundacao() { return dataFundacao; }
    public boolean isAtiva() { return ativa; }
}
