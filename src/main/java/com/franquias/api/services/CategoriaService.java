package com.franquias.api.services;

import com.franquias.api.dtos.CategoriaCreateRequest;
import com.franquias.api.exceptions.ConflictException;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.Categoria;
import com.franquias.api.repositories.CategoriaRepository;
import com.franquias.api.validation.ValidationUtil;

import java.util.List;

public class CategoriaService {

    private final CategoriaRepository categoriaRepository = new CategoriaRepository();

    public Categoria cadastrar(CategoriaCreateRequest dto) {
        ValidationUtil.validar(dto);

        categoriaRepository.buscarPorNome(dto.getNome()).ifPresent(c -> {
            throw new ConflictException("Já existe uma categoria com este nome.");
        });

        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());

        return categoriaRepository.salvar(categoria);
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.listarTodas();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada."));
    }
}
