package com.franquias.api.repositories;

import com.franquias.api.data.JpaUtil;
import com.franquias.api.exceptions.BadRequestException;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.Estoque;
import com.franquias.api.models.ItemVenda;
import com.franquias.api.models.MovimentacaoEstoque;
import com.franquias.api.models.ProdutoServico;
import com.franquias.api.models.UnidadeFranqueada;
import com.franquias.api.models.Usuario;
import com.franquias.api.models.Venda;
import com.franquias.api.models.enums.TipoMovimentacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VendaRepository {

    public Optional<Venda> buscarPorId(Long id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Venda.class, id));
        } finally {
            em.close();
        }
    }

    // Busca dinâmica por unidade e/ou intervalo de datas
    public List<Venda> buscar(Long unidadeId, LocalDate dataInicio, LocalDate dataFim) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Venda> cq = cb.createQuery(Venda.class);
            Root<Venda> raiz = cq.from(Venda.class);

            List<Predicate> predicados = new ArrayList<>();
            if (unidadeId != null) {
                predicados.add(cb.equal(raiz.get("unidade").get("id"), unidadeId));
            }
            if (dataInicio != null) {
                predicados.add(cb.greaterThanOrEqualTo(raiz.get("dataVenda"), dataInicio.atStartOfDay()));
            }
            if (dataFim != null) {
                predicados.add(cb.lessThan(raiz.get("dataVenda"), dataFim.plusDays(1).atStartOfDay()));
            }

            cq.select(raiz).orderBy(cb.desc(raiz.get("dataVenda")));
            if (!predicados.isEmpty()) {
                cq.where(predicados.toArray(new Predicate[0]));
            }

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    // Se um item não tiver estoque, a venda é cancelada
    public Venda registrarVenda(Long unidadeId, Long usuarioId, List<ItemVendaInput> itensInput) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();

            UnidadeFranqueada unidade = em.find(UnidadeFranqueada.class, unidadeId);
            if (unidade == null) {
                throw new NotFoundException("Unidade informada não existe.");
            }
            if (!unidade.podeRegistrarVenda()) {
                throw new BadRequestException("Unidade inativa não pode registrar vendas.");
            }

            Usuario usuario = em.find(Usuario.class, usuarioId);

            Venda venda = new Venda();
            venda.setUnidade(unidade);
            venda.setUsuario(usuario);
            venda.setDataVenda(LocalDateTime.now());

            for (ItemVendaInput itemInput : itensInput) {
                ProdutoServico produto = em.find(ProdutoServico.class, itemInput.produtoServicoId());
                if (produto == null) {
                    throw new NotFoundException(
                            "Produto/serviço informado não existe: id " + itemInput.produtoServicoId());
                }

                Estoque estoque = em.createQuery(
                                "SELECT e FROM Estoque e WHERE e.unidade.id = :unidadeId AND e.produtoServico.id = :produtoId",
                                Estoque.class)
                        .setParameter("unidadeId", unidadeId)
                        .setParameter("produtoId", produto.getId())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);

                int disponivel = estoque != null ? estoque.getQuantidadeAtual() : 0;

                if (disponivel < itemInput.quantidade()) {
                    throw new BadRequestException(
                            "Estoque insuficiente para \"" + produto.getNome() + "\": disponível " + disponivel
                                    + ", solicitado " + itemInput.quantidade() + ".");
                }

                estoque.setQuantidadeAtual(disponivel - itemInput.quantidade());

                MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
                movimentacao.setEstoque(estoque);
                movimentacao.setTipo(TipoMovimentacao.SAIDA);
                movimentacao.setQuantidade(itemInput.quantidade());
                movimentacao.setObservacao("Baixa automática por venda");
                em.persist(movimentacao);

                ItemVenda item = new ItemVenda();
                item.setProdutoServico(produto);
                item.setQuantidade(itemInput.quantidade());
                item.setPrecoUnitario(produto.getPrecoBase());
                item.calcularSubtotal();
                venda.adicionarItem(item);
            }

            venda.recalcularValorTotal();
            em.persist(venda);

            em.getTransaction().commit();
            return venda;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
