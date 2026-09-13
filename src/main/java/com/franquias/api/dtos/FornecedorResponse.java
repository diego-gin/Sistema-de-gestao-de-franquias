package com.franquias.api.dtos;

import com.franquias.api.models.Fornecedor;
import com.franquias.api.models.enums.StatusFornecedor;

import java.util.List;
import java.util.stream.Collectors;

public class FornecedorResponse {

    public static class ProdutoResumo {
        private Long id;
        private String nome;

        public ProdutoResumo(Long id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        public Long getId() { return id; }
        public String getNome() { return nome; }
    }

    private Long id;
    private String nome;
    private String cnpj;
    private String contato;
    private String telefone;
    private String email;
    private StatusFornecedor status;
    private List<ProdutoResumo> produtos;

    public static FornecedorResponse fromEntity(Fornecedor f) {
        FornecedorResponse dto = new FornecedorResponse();
        dto.id = f.getId();
        dto.nome = f.getNome();
        dto.cnpj = f.getCnpj();
        dto.contato = f.getContato();
        dto.telefone = f.getTelefone();
        dto.email = f.getEmail();
        dto.status = f.getStatus();
        dto.produtos = f.getProdutos().stream()
                .map(p -> new ProdutoResumo(p.getId(), p.getNome()))
                .collect(Collectors.toList());
        return dto;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCnpj() { return cnpj; }
    public String getContato() { return contato; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public StatusFornecedor getStatus() { return status; }
    public List<ProdutoResumo> getProdutos() { return produtos; }
}
