package com.franquias.api.dtos;

import com.franquias.api.models.Franqueado;

public class FranqueadoResponse {

    private Long id;
    private String nome;
    private String cpfCnpj;
    private String email;
    private String telefone;

    public static FranqueadoResponse fromEntity(Franqueado f) {
        FranqueadoResponse dto = new FranqueadoResponse();
        dto.id = f.getId();
        dto.nome = f.getNome();
        dto.cpfCnpj = f.getCpfCnpj();
        dto.email = f.getEmail();
        dto.telefone = f.getTelefone();
        return dto;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpfCnpj() { return cpfCnpj; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
}
