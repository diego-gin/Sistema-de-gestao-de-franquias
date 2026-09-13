package com.franquias.api.services;

import com.franquias.api.dtos.FornecedorCreateRequest;
import com.franquias.api.dtos.FornecedorUpdateRequest;
import com.franquias.api.exceptions.ConflictException;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.Fornecedor;
import com.franquias.api.models.enums.StatusFornecedor;
import com.franquias.api.repositories.FornecedorRepository;
import com.franquias.api.validation.ValidationUtil;

import java.util.List;

public class FornecedorService {

    private final FornecedorRepository fornecedorRepository = new FornecedorRepository();

    public Fornecedor cadastrar(FornecedorCreateRequest dto) {
        ValidationUtil.validar(dto);

        fornecedorRepository.buscarPorCnpj(dto.getCnpj()).ifPresent(f -> {
            throw new ConflictException("Já existe um fornecedor cadastrado com este CNPJ.");
        });

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(dto.getNome());
        fornecedor.setCnpj(dto.getCnpj());
        fornecedor.setContato(dto.getContato());
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setStatus(StatusFornecedor.ATIVO);

        return fornecedorRepository.salvar(fornecedor);
    }

    public Fornecedor atualizar(Long id, FornecedorUpdateRequest dto) {
        ValidationUtil.validar(dto);
        Fornecedor fornecedor = buscarPorId(id);

        fornecedor.setNome(dto.getNome());
        fornecedor.setContato(dto.getContato());
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setEmail(dto.getEmail());

        return fornecedorRepository.salvar(fornecedor);
    }

    public Fornecedor alterarStatus(Long id, StatusFornecedor status) {
        Fornecedor fornecedor = buscarPorId(id);
        fornecedor.setStatus(status);
        return fornecedorRepository.salvar(fornecedor);
    }

    public Fornecedor buscarPorId(Long id) {
        return fornecedorRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Fornecedor não encontrado."));
    }

    public List<Fornecedor> buscar(String nome, String cnpj, StatusFornecedor status) {
        return fornecedorRepository.buscar(nome, cnpj, status);
    }

    public Fornecedor associarProdutos(Long id, List<Long> produtoIds) {
        return fornecedorRepository.associarProdutos(id, produtoIds);
    }
}
