// src/main/java/com/example/vagas/service/VagaService.java

package com.example.vagas.service;

import com.example.vagas.model.Vaga;
import com.example.vagas.model.Empresa;
import com.example.vagas.repository.VagaRepository;
import com.example.vagas.repository.EmpresaRepository; // NOVO
import com.example.vagas.security.SecurityUtils;     // NOVO
import com.example.vagas.dto.VagaDTO;                // NOVO
import com.example.vagas.dto.VagaCreateUpdateDTO;    // NOVO
import com.example.vagas.exception.ResourceNotFoundException; // NOVO

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Injeção via construtor
public class VagaService {

    private final VagaRepository vagaRepository;
    private final EmpresaRepository empresaRepository; // Injeção de EmpresaRepository
    private final SecurityUtils securityUtils;       // Injeção de SecurityUtils

    // ---------------------------------------------------------------------------------
    // 1. CRIAÇÃO (POST) - Com Segurança e DTOs
    // ---------------------------------------------------------------------------------
    @Transactional
    public VagaDTO createVaga(VagaCreateUpdateDTO dto) {
        // 1. Validar se a empresa existe
        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + dto.getEmpresaId()));
        
        // 2. REGRA DE SEGURANÇA: Verificar se o usuário logado é o dono da empresa
        UUID currentUserId = securityUtils.getCurrentUserId();
        if (!empresa.getOwner().getId().equals(currentUserId)) {
            throw new SecurityException("Acesso negado. Você não é o dono desta empresa para criar vagas.");
        }

        // 3. Mapear DTO para Entidade
        Vaga vaga = new Vaga();
        vaga.setTitulo(dto.getTitulo());
        vaga.setEmpresa(empresa);

        // 4. Salvar e Retornar DTO
        return mapToDTO(vagaRepository.save(vaga));
    }

    // ---------------------------------------------------------------------------------
    // 2. ATUALIZAÇÃO (PUT) - Com Segurança e DTOs
    // ---------------------------------------------------------------------------------
    @Transactional
    public VagaDTO updateVaga(Long id, VagaCreateUpdateDTO dto) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada com ID: " + id));

        // 1. REGRA DE SEGURANÇA: Verificar se o usuário logado é o dono da empresa
        UUID currentUserId = securityUtils.getCurrentUserId();
        if (!vaga.getEmpresa().getOwner().getId().equals(currentUserId)) {
            throw new SecurityException("Acesso negado. Você não é o dono da empresa desta vaga.");
        }
        
        // 2. Atualiza os dados
        vaga.setTitulo(dto.getTitulo());
        // Se a empresaId mudou no DTO, você pode buscar a nova empresa e setá-la, mas
        // por segurança, geralmente o 'dono' da vaga não pode ser trocado facilmente.
        
        return mapToDTO(vagaRepository.save(vaga));
    }

    // ---------------------------------------------------------------------------------
    // 3. DELEÇÃO (DELETE) - Com Segurança
    // ---------------------------------------------------------------------------------
    @Transactional
    public void deletarVaga(Long id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada com ID: " + id));
        
        // REGRA DE SEGURANÇA: Verificar se o usuário logado é o dono da empresa
        UUID currentUserId = securityUtils.getCurrentUserId();
        if (!vaga.getEmpresa().getOwner().getId().equals(currentUserId)) {
            throw new SecurityException("Acesso negado. Você não pode deletar vagas de outras empresas.");
        }
        
        vagaRepository.delete(vaga);
    }
    
    // ---------------------------------------------------------------------------------
    // 4. BUSCA (GET) - Com DTOs
    // ---------------------------------------------------------------------------------
    
    // 4a. Buscar por ID (público)
    public VagaDTO buscarPorId(Long id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada com ID: " + id));
        return mapToDTO(vaga);
    }

    // 4b. Listar com paginação (público)
    public Page<VagaDTO> listarTodasVagasPaginadas(Pageable pageable) {
        return vagaRepository.findAll(pageable)
                .map(this::mapToDTO); // Mapeia a página de Entidades para a página de DTOs
    }

    // 4c. Busca por termo (público)
    public List<VagaDTO> buscarVagasPorTermo(String termoBusca) {
        return vagaRepository.buscarPorTermo(termoBusca).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------------------------
    // 5. MÉTODO DE MAPEAMENTO (Vaga -> VagaDTO)
    // ---------------------------------------------------------------------------------
    private VagaDTO mapToDTO(Vaga vaga) {
        return VagaDTO.builder()
                .id(vaga.getId())
                .titulo(vaga.getTitulo())
                // Garante que a Empresa está carregada antes de acessar o ID/Nome
                .empresaId(vaga.getEmpresa() != null ? vaga.getEmpresa().getId() : null) 
                .empresaNomeFantasia(vaga.getEmpresa() != null ? vaga.getEmpresa().getNomeFantasia() : null)
                .build();
    }
}