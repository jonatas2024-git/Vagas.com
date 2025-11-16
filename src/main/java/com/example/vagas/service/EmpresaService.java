package com.example.vagas.service;

import com.example.vagas.dto.EmpresaCreateUpdateDTO;
import com.example.vagas.model.Empresa;
import com.example.vagas.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
//import java.util.Long;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    // LISTAR TODAS
    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    // BUSCAR POR ID
    public Empresa findEmpresaById(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }

    // CRIAR EMPRESA
    public Empresa createEmpresa(EmpresaCreateUpdateDTO dto) {
        Empresa empresa = new Empresa();
        empresa.setNome(dto.getNome());
        empresa.setCnpj(dto.getCnpj());
        empresa.setDescricao(dto.getDescricao());
        empresa.setEmail(dto.getEmail());
        empresa.setTelefone(dto.getTelefone());

        return empresaRepository.save(empresa);
    }

    // ATUALIZAR EMPRESA
    public Empresa updateEmpresa(Long id, EmpresaCreateUpdateDTO dto) {
        Empresa empresa = findEmpresaById(id);

        empresa.setNome(dto.getNome());
        empresa.setCnpj(dto.getCnpj());
        empresa.setDescricao(dto.getDescricao());
        empresa.setEmail(dto.getEmail());
        empresa.setTelefone(dto.getTelefone());

        return empresaRepository.save(empresa);
    }

    // DELETAR EMPRESA
    public void deletarEmpresaPorId(Long id) {
        empresaRepository.deleteById(id);
    }

    // VERIFICA PROPRIETÁRIO (AQUI APENAS EXISTÊNCIA)
    public void verifyOwner(Long empresaId) {
        if (!empresaRepository.existsById(empresaId)) {
            throw new RuntimeException("Empresa não encontrada para validação");
        }
    }
}
