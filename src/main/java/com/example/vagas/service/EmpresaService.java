// src/main/java/com/example/vagas/service/EmpresaService.java

package com.example.vagas.service;

import com.example.vagas.model.Empresa;
import com.example.vagas.model.User;
import com.example.vagas.repository.EmpresaRepository;
import com.example.vagas.repository.UserRepository;
import com.example.vagas.security.SecurityUtils;
import com.example.vagas.dto.EmpresaDTO;
import com.example.vagas.dto.EmpresaCreateUpdateDTO;
import com.example.vagas.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors; // Necessário para o método findAllEmpresas

@Service
@RequiredArgsConstructor // Injeção de dependência via construtor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final UserRepository userRepository; // Necessário para buscar o User dono
    private final SecurityUtils securityUtils; // Necessário para segurança
    
    // ---------------------------------------------------------------------------------

    @Transactional
    public EmpresaDTO createEmpresa(EmpresaCreateUpdateDTO dto) {
        // 1. Encontrar o usuário logado (Dono da Empresa)
        UUID currentUserId = securityUtils.getCurrentUserId();
        User owner = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário logado não encontrado."));

        // 2. Mapear DTO para Entidade
        Empresa empresa = new Empresa();
        empresa.setNome(dto.getNome());
        empresa.setNomeFantasia(dto.getNomeFantasia());
        empresa.setOwner(owner); // Atribuir o dono
        
        // 3. Salvar
        Empresa newEmpresa = empresaRepository.save(empresa);
        
        // 4. Retornar DTO
        return mapToDTO(newEmpresa);
    }
    
    /**
     * Busca todas as empresas e mapeia para DTOs.
     * Necessário para o GET /api/empresas
     */
    public List<EmpresaDTO> findAllEmpresas() {
        return empresaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca empresa por ID e mapeia para DTO.
     * Necessário para o GET /api/empresas/{id}
     * @throws ResourceNotFoundException se a empresa não for encontrada.
     */
    public EmpresaDTO findEmpresaById(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + id));
        return mapToDTO(empresa);
    }
    
    @Transactional
    public EmpresaDTO updateEmpresa(Long id, EmpresaCreateUpdateDTO dto) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + id));

        // REGRA DE SEGURANÇA: Verificar se o usuário logado é o dono
        // NOTA: O método equals com UUID compara o valor corretamente.
        if (!empresa.getOwner().getId().equals(securityUtils.getCurrentUserId())) {
            throw new SecurityException("Acesso negado. Você não é o dono desta empresa.");
        }

        empresa.setNome(dto.getNome());
        empresa.setNomeFantasia(dto.getNomeFantasia());
        
        return mapToDTO(empresaRepository.save(empresa));
    }

    @Transactional
    public void deletarEmpresaPorId(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com ID: " + id));

        // REGRA DE SEGURANÇA: Verificar se o usuário logado é o dono
        if (!empresa.getOwner().getId().equals(securityUtils.getCurrentUserId())) {
            throw new SecurityException("Acesso negado. Você não é o dono desta empresa.");
        }
        
        // ATENÇÃO: Se a empresa tiver vagas, você precisa tratar a exclusão primeiro (Ex: cascade ou exclusão manual)
        empresaRepository.delete(empresa);
    }
    
    // MÉTODO DE MAPEAMENTO
    private EmpresaDTO mapToDTO(Empresa empresa) {
        return EmpresaDTO.builder()
                .id(empresa.getId())
                .nome(empresa.getNome())
                .nomeFantasia(empresa.getNomeFantasia())
                // O método getId() do User retorna UUID, que é o tipo esperado pelo EmpresaDTO.
                .ownerUserId(empresa.getOwner() != null ? empresa.getOwner().getId() : null)
                .build();
    }
}