package com.example.vagas.service;

import com.example.vagas.model.Empresa;
import com.example.vagas.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public Empresa createEmpresa(Empresa empresa) {
        return empresaRepository.save(empresa);
    }


    public List<Empresa> listarEmpresas() { return empresaRepository.findAll(); }

    public Optional<Empresa> buscarEmpresaPorId(Long id) { return empresaRepository.findById(id); }

    public Empresa atualizarEmpresa(Long id, Empresa dadosAtualizados) {
        return empresaRepository.findById(id).map(empresa -> {
            empresa.setNome(dadosAtualizados.getNome());
            empresa.setNomeFantasia(dadosAtualizados.getNomeFantasia());
            return empresaRepository.save(empresa);
        }).orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }

    public void deletarEmpresaPorId(Long id) {
        if(!empresaRepository.existsById(id)) {
            throw new RuntimeException("Empresa não encontrada");
        }
        empresaRepository.deleteById(id);
    }
}
