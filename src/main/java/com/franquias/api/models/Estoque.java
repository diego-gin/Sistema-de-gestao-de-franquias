package com.franquias.api.models;

import jakarta.persistence.*;

@Entity
@Table(
    name = "estoque",
    uniqueConstraints = @UniqueConstraint(columnNames = {"unidade_id", "produto_servico_id"})
)
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unidade_id", nullable = false)
    private UnidadeFranqueada unidade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_servico_id", nullable = false)
    private ProdutoServico produtoServico;

    @Column(nullable = false)
    private Integer quantidadeAtual = 0;

    @Column(nullable = false)
    private Integer quantidadeMinima = 0;

    public Estoque() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UnidadeFranqueada getUnidade() {
        return unidade;
    }

    public void setUnidade(UnidadeFranqueada unidade) {
        this.unidade = unidade;
    }

    public ProdutoServico getProdutoServico() {
        return produtoServico;
    }

    public void setProdutoServico(ProdutoServico produtoServico) {
        this.produtoServico = produtoServico;
    }

    public Integer getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(Integer quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public Integer getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(Integer quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    public boolean isAbaixoDoMinimo() {
        return quantidadeAtual < quantidadeMinima;
    }
}
