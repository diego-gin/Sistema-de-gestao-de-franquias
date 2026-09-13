package com.franquias.api.services;

import com.franquias.api.dtos.MovimentacaoRequest;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.Estoque;
import com.franquias.api.models.MovimentacaoEstoque;
import com.franquias.api.repositories.EstoqueRepository;
import com.franquias.api.repositories.MovimentacaoEstoqueRepository;
import com.franquias.api.repositories.ProdutoServicoRepository;
import com.franquias.api.repositories.UnidadeFranqueadaRepository;
import com.franquias.api.validation.ValidationUtil;

import java.util.List;

public class EstoqueService {

    private final EstoqueRepository estoqueRepository = new EstoqueRepository();
    private final MovimentacaoEstoqueRepository movimentacaoRepository = new MovimentacaoEstoqueRepository();
    private final UnidadeFranqueadaRepository unidadeRepository = new UnidadeFranqueadaRepository();
    private final ProdutoServicoRepository produtoRepository = new ProdutoServicoRepository();

    public MovimentacaoEstoque registrarMovimentacao(MovimentacaoRequest dto) {
        ValidationUtil.validar(dto);

        // Validações de existência com mensagens claras, antes de entrar na
        // transação atômica do repository.
        unidadeRepository.buscarPorId(dto.getUnidadeId())
                .orElseThrow(() -> new NotFoundException("Unidade informada não existe."));
        produtoRepository.buscarPorId(dto.getProdutoServicoId())
                .orElseThrow(() -> new NotFoundException("Produto/serviço informado não existe."));

        return estoqueRepository.registrarMovimentacao(
                dto.getUnidadeId(),
                dto.getProdutoServicoId(),
                dto.getTipo(),
                dto.getQuantidade(),
                dto.getObservacao(),
                dto.getFornecedorId()
        );
    }

    public Estoque buscarPorId(Long id) {
        return estoqueRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Registro de estoque não encontrado."));
    }

    public List<Estoque> buscar(Long unidadeId, Long produtoId, Boolean abaixoMinimo) {
        return estoqueRepository.buscar(unidadeId, produtoId, abaixoMinimo);
    }

    public Estoque atualizarMinimo(Long estoqueId, int minimo) {
        return estoqueRepository.atualizarMinimo(estoqueId, minimo);
    }

    public List<MovimentacaoEstoque> listarMovimentacoes(Long estoqueId) {
        buscarPorId(estoqueId); // garante que o estoque existe
        return movimentacaoRepository.listarPorEstoque(estoqueId);
    }
}
