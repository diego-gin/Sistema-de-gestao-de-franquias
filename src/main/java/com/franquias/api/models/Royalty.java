package com.franquias.api.models;

import com.franquias.api.models.enums.SituacaoPagamento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(
    name = "royalty",
    uniqueConstraints = @UniqueConstraint(columnNames = {"unidade_id", "periodo_referencia"})
)
public class Royalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unidade_id", nullable = false)
    private UnidadeFranqueada unidade;

    // formato "YYYY-MM"
    @Column(name = "periodo_referencia", nullable = false, length = 7)
    private String periodoReferencia;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualAplicado;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal faturamentoBase;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorCalculado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SituacaoPagamento situacaoPagamento = SituacaoPagamento.PENDENTE;

    private LocalDate dataPagamento;

    public Royalty() {
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

    public String getPeriodoReferencia() {
        return periodoReferencia;
    }

    public void setPeriodoReferencia(String periodoReferencia) {
        this.periodoReferencia = periodoReferencia;
    }

    public void setPeriodoReferencia(YearMonth periodo) {
        this.periodoReferencia = periodo.toString();
    }

    public BigDecimal getPercentualAplicado() {
        return percentualAplicado;
    }

    public void setPercentualAplicado(BigDecimal percentualAplicado) {
        this.percentualAplicado = percentualAplicado;
    }

    public BigDecimal getFaturamentoBase() {
        return faturamentoBase;
    }

    public void setFaturamentoBase(BigDecimal faturamentoBase) {
        this.faturamentoBase = faturamentoBase;
    }

    public BigDecimal getValorCalculado() {
        return valorCalculado;
    }

    public void setValorCalculado(BigDecimal valorCalculado) {
        this.valorCalculado = valorCalculado;
    }

    public SituacaoPagamento getSituacaoPagamento() {
        return situacaoPagamento;
    }

    public void setSituacaoPagamento(SituacaoPagamento situacaoPagamento) {
        this.situacaoPagamento = situacaoPagamento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public void calcularValor() {
        this.valorCalculado = faturamentoBase
                .multiply(percentualAplicado)
                .divide(BigDecimal.valueOf(100));
    }
}
