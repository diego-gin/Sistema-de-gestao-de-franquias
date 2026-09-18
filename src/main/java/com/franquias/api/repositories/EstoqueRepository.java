package com.franquias.api.repositories;

import com.franquias.api.data.JpaUtil;
import com.franquias.api.exceptions.BadRequestException;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.Estoque;
import com.franquias.api.models.Fornecedor;
import com.franquias.api.models.MovimentacaoEstoque;
import com.franquias.api.models.ProdutoServico;
import com.franquias.api.models.UnidadeFranqueada;
import com.franquias.api.models.enums.TipoMovimentacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EstoqueRepository {

    public Optional<Estoque> buscarPorId(Long id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Estoque.class, id));
        } finally {
            em.close();
        }
    }

    // abaixoMinimo compara duas colunas da própria linha (quantidadeAtual < quantidadeMinima)
    public List<Estoque> buscar(Long unidadeId, Long produtoId, Boolean abaixoMinimo) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Estoque> cq = cb.createQuery(Estoque.class);
            Root<Estoque> raiz = cq.from(Estoque.class);

            List<Predicate> predicados = new ArrayList<>();
            if (unidadeId != null) {
                predicados.add(cb.equal(raiz.get("unidade").get("id"), unidadeId));
            }
            if (produtoId != null) {
                predicados.add(cb.equal(raiz.get("produtoServico").get("id"), produtoId));
            }
            if (Boolean.TRUE.equals(abaixoMinimo)) {
                predicados.add(cb.lessThan(raiz.get("quantidadeAtual"), raiz.get("quantidadeMinima")));
            }

            cq.select(raiz);
            if (!predicados.isEmpty()) {
                cq.where(predicados.toArray(new Predicate[0]));
            }
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    public Estoque atualizarMinimo(Long estoqueId, int minimo) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            Estoque estoque = em.find(Estoque.class, estoqueId);
            if (estoque == null) {
                throw new NotFoundException("Registro de estoque não encontrado.");
            }
            estoque.setQuantidadeMinima(minimo);
            em.getTransaction().commit();
            return estoque;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // cria o estoque com saldo 0 se ainda não existir; saldo nunca fica negativo
    public MovimentacaoEstoque registrarMovimentacao(Long unidadeId, Long produtoId, TipoMovimentacao tipo,
                                                       int quantidade, String observacao, Long fornecedorId) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();

            Estoque estoque = em.createQuery(
                            "SELECT e FROM Estoque e WHERE e.unidade.id = :unidadeId AND e.produtoServico.id = :produtoId",
                            Estoque.class)
                    .setParameter("unidadeId", unidadeId)
                    .setParameter("produtoId", produtoId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (estoque == null) {
                UnidadeFranqueada unidade = em.find(UnidadeFranqueada.class, unidadeId);
                ProdutoServico produto = em.find(ProdutoServico.class, produtoId);

                estoque = new Estoque();
                estoque.setUnidade(unidade);
                estoque.setProdutoServico(produto);
                estoque.setQuantidadeAtual(0);
                estoque.setQuantidadeMinima(0);
                em.persist(estoque);
            }

            int novoSaldo = tipo == TipoMovimentacao.ENTRADA
                    ? estoque.getQuantidadeAtual() + quantidade
                    : estoque.getQuantidadeAtual() - quantidade;

            if (novoSaldo < 0) {
                throw new BadRequestException(
                        "Saldo insuficiente para saída: estoque atual é " + estoque.getQuantidadeAtual()
                                + ", tentativa de saída de " + quantidade + ".");
            }

            estoque.setQuantidadeAtual(novoSaldo);

            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.setEstoque(estoque);
            movimentacao.setTipo(tipo);
            movimentacao.setQuantidade(quantidade);
            movimentacao.setObservacao(observacao);

            if (fornecedorId != null) {
                Fornecedor fornecedor = em.find(Fornecedor.class, fornecedorId);
                if (fornecedor == null) {
                    throw new NotFoundException("Fornecedor informado não existe.");
                }
                movimentacao.setFornecedor(fornecedor);
            }

            em.persist(movimentacao);

            em.getTransaction().commit();
            return movimentacao;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
