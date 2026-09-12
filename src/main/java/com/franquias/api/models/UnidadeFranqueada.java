package com.franquias.api.models;

import com.franquias.api.models.enums.SituacaoUnidade;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "unidade_franqueada")
public class UnidadeFranqueada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "franqueadora_id", nullable = false)
    private Franqueadora franqueadora;

    @ManyToOne(optional = false)
    @JoinColumn(name = "franqueado_id", nullable = false)
    private Franqueado franqueado;

    @Column(nullable = false, length = 150)
    private String nomeFantasia;

    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(length = 200)
    private String endereco;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @Column(length = 20)
    private String telefone;

    @Column(length = 150)
    private String email;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SituacaoUnidade situacao = SituacaoUnidade.ATIVA;

    public UnidadeFranqueada() {
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Franqueadora getFranqueadora() {
        return franqueadora;
    }

    public void setFranqueadora(Franqueadora franqueadora) {
        this.franqueadora = franqueadora;
    }

    public Franqueado getFranqueado() {
        return franqueado;
    }

    public void setFranqueado(Franqueado franqueado) {
        this.franqueado = franqueado;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public SituacaoUnidade getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoUnidade situacao) {
        this.situacao = situacao;
    }

    /**
     * Regra de negócio: uma unidade inativa não pode registrar novas vendas.
     */
    public boolean podeRegistrarVenda() {
        return this.situacao == SituacaoUnidade.ATIVA;
    }
}
