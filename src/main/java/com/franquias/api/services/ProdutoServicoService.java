package com.franquias.api.services;

import com.franquias.api.dtos.ProdutoCreateRequest;
import com.franquias.api.dtos.ProdutoUpdateRequest;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.Categoria;
import com.franquias.api.models.ProdutoServico;
import com.franquias.api.models.enums.StatusProduto;
import com.franquias.api.repositories.CategoriaRepository;
import com.franquias.api.repositories.ProdutoServicoRepository;
import com.franquias.api.validation.ValidationUtil;

import java.util.List;

public class ProdutoServicoService {

    private final ProdutoServicoRepository produtoRepository = new ProdutoServicoRepository();
    private final CategoriaRepository categoriaRepository = new CategoriaRepository();

    public ProdutoServico cadastrar(ProdutoCreateRequest dto) {
        ValidationUtil.validar(dto);

        Categoria categoria = categoriaRepository.buscarPorId(dto.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria informada não existe."));

        ProdutoServico produto = new ProdutoServico();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setCategoria(categoria);
        produto.setPrecoBase(dto.getPrecoBase());
        produto.setStatus(StatusProduto.ATIVO);

        return produtoRepository.salvar(produto);
    }

    public ProdutoServico atualizar(Long id, ProdutoUpdateRequest dto) {
        ValidationUtil.validar(dto);
        ProdutoServico produto = buscarPorId(id);

        Categoria categoria = categoriaRepository.buscarPorId(dto.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria informada não existe."));

        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setCategoria(categoria);
        produto.setPrecoBase(dto.getPrecoBase());

        return produtoRepository.salvar(produto);
    }

    public ProdutoServico alterarStatus(Long id, StatusProduto status) {
        ProdutoServico produto = buscarPorId(id);
        produto.setStatus(status);
        return produtoRepository.salvar(produto);
    }

    public ProdutoServico buscarPorId(Long id) {
        return produtoRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Produto/serviço não encontrado."));
    }

    public List<ProdutoServico> buscar(String nome, Long categoriaId, StatusProduto status) {
        return produtoRepository.buscar(nome, categoriaId, status);
    }
}
