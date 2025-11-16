package com.example.vagas.service;

import com.example.vagas.dto.VagaFilterDTO;
import com.example.vagas.model.Vaga;
import com.example.vagas.repository.VagaRepository;
import com.example.vagas.repository.specifications.VagaSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VagaService {

    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga criar(Vaga vaga) {
        return vagaRepository.save(vaga);
    }

    @Transactional
    public Vaga atualizar(UUID id, Vaga dto) {
        Vaga atual = vagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        // atualize campos necessários (exemplo)
        atual.setTitulo(dto.getTitulo());
        atual.setDescricao(dto.getDescricao());
        atual.setCidade(dto.getCidade());
        atual.setUf(dto.getUf());
        atual.setEmpresa(dto.getEmpresa());
        atual.setFaixaSalarial(dto.getFaixaSalarial());
        atual.setTipoContrato(dto.getTipoContrato());
        atual.setSenioridade(dto.getSenioridade());
        atual.setModeloTrabalho(dto.getModeloTrabalho());
        atual.setSkillsObrigatorias(dto.getSkillsObrigatorias());
        atual.setSkillsDesejaveis(dto.getSkillsDesejaveis());
        return vagaRepository.save(atual);
    }

    public void deletar(UUID id) {
        vagaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorId(UUID id) {
        return vagaRepository.findById(id).orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
    }

    @Transactional(readOnly = true)
    public Page<Vaga> buscarComFiltros(VagaFilterDTO filters, Pageable pageable) {
        VagaSpecification spec = new VagaSpecification(filters);
        return vagaRepository.findAll(spec, pageable);
    }
}
