package com.example.vagas.service;

import com.example.vagas.model.Vaga;
import com.example.vagas.model.Empresa;
import com.example.vagas.repository.VagaRepository;
import com.example.vagas.repository.EmpresaRepository; 
import com.example.vagas.security.SecurityUtils;     
import com.example.vagas.dto.VagaDTO;                
import com.example.vagas.dto.VagaCreateUpdateDTO;    
import com.example.vagas.exception.ResourceNotFoundException; 
//import com.example.vagas.exception.DuplicateEntryException; // Não usado.

// Imports para Filtros Avançados (Item 9)
import com.example.vagas.dto.VagaFilterDTO;
import com.example.vagas.repository.specifications.VagaSpecification; 

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import java.util.List; // Não usado.
import java.util.UUID;
//import java.util.stream.Collectors; // Não Usado.

@Service
@RequiredArgsConstructor 
public class VagaService {

    private final VagaRepository vagaRepository;
    private final EmpresaRepository empresaRepository; 
    private final SecurityUtils securityUtils;
    private final VagaMetricsService vagaMetricsService; // INJEÇÃO PARA ITEM 10
    
    // =========================================================================
    // 1. CRIAÇÃO (POST) - Com Segurança (Item 13)
    // =========================================================================
    @Transactional
    public VagaDTO createVaga(VagaCreateUpdateDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + dto.getEmpresaId()));
        
        // REGRA DE SEGURANÇA: Verificar se o usuário logado é o dono da empresa
        UUID currentUserId = securityUtils.getCurrentUserId();
        if (!empresa.getOwner().getId().equals(currentUserId)) {
            throw new SecurityException("Acesso negado. Você não é o dono desta empresa para criar vagas.");
        }

        // Mapear DTO para Entidade
        Vaga vaga = mapCreateDtoToEntity(dto, empresa); 
        
        return mapToDTO(vagaRepository.save(vaga));
    }

    // =========================================================================
    // 2. ATUALIZAÇÃO (PUT) - Com Segurança (Item 13)
    // =========================================================================
    @Transactional
    public VagaDTO updateVaga(Long id, VagaCreateUpdateDTO dto) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada com ID: " + id));

        // REGRA DE SEGURANÇA: Verificar se o usuário logado é o dono da empresa
        UUID currentUserId = securityUtils.getCurrentUserId();
        if (!vaga.getEmpresa().getOwner().getId().equals(currentUserId)) {
            throw new SecurityException("Acesso negado. Você não é o dono da empresa desta vaga.");
        }
        
        // Atualiza os dados
        mapUpdateDtoToEntity(dto, vaga); 
        
        return mapToDTO(vagaRepository.save(vaga));
    }

    // =========================================================================
    // 3. DELEÇÃO (DELETE) - Com Segurança (Item 13)
    // =========================================================================
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
    
    // =========================================================================
    // 4. BUSCA (GET) - Com visualização (Item 10)
    // =========================================================================
    
    @Transactional
    public VagaDTO buscarPorId(Long id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaga não encontrada com ID: " + id));
        
        // ITEM 10: Registra a visualização
        vagaMetricsService.recordVagaView(id); 

        return mapToDTO(vaga);
    }

    public Page<VagaDTO> listarTodasVagasPaginadas(Pageable pageable) {
        return vagaRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    // Busca unificada com filtros avançados (Item 9)
    public Page<VagaDTO> searchVagasByFilter(VagaFilterDTO filters, Pageable pageable) {
        VagaSpecification spec = new VagaSpecification(filters);
        return vagaRepository.findAll(spec, pageable)
                .map(this::mapToDTO);
    }
    
    // =========================================================================
    // 5. MÉTODOS DE MAPEAMENTO (Vaga -> VagaDTO)
    // =========================================================================
    
    private VagaDTO mapToDTO(Vaga vaga) {
        return VagaDTO.builder()
                .id(vaga.getId())
                .titulo(vaga.getTitulo())
                .empresaId(vaga.getEmpresa() != null ? vaga.getEmpresa().getId() : null) 
                .empresaNomeFantasia(vaga.getEmpresa() != null ? vaga.getEmpresa().getNomeFantasia() : null)
                // ... incluir todos os outros campos mapeados
                .build();
    }
    
    private Vaga mapCreateDtoToEntity(VagaCreateUpdateDTO dto, Empresa empresa) {
        Vaga vaga = new Vaga();
        vaga.setTitulo(dto.getTitulo());
        // Assumindo que os setters corretos existem na Vaga
        vaga.setEmpresa(empresa);
        return vaga;
    }
    
    private Vaga mapUpdateDtoToEntity(VagaCreateUpdateDTO dto, Vaga vaga) {
        vaga.setTitulo(dto.getTitulo());
        // Assumindo que os setters corretos existem na Vaga
        return vaga;
    }
}