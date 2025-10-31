package com.example.vagas.controller;

// Importações para a nova lógica baseada em DTOs e Service seguro
import com.example.vagas.service.PerfilService;
import com.example.vagas.dto.PerfilDTO;
import com.example.vagas.dto.PerfilUpdateDTO;

// Importações do Spring e Lombok
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; 

// O PerfilService é responsável por toda a lógica, incluindo a segurança.
@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor // Uso de Lombok para injeção de dependência via construtor (melhor prática)
public class PerfilController {

    // Injeção de dependência via construtor
    private final PerfilService perfilService;


    /**
     * Obtém o perfil do usuário atualmente autenticado.
     * O ID do usuário é extraído do token pelo SecurityUtils (dentro do Service).
     * Rota: GET /api/perfil
     * Retorna: 200 OK e PerfilDTO ou 404 NOT FOUND (tratado pelo Service/Exception Handler)
     */
    @GetMapping
    public ResponseEntity<PerfilDTO> getMeuPerfil() {
        // Toda a lógica de segurança e busca é delegada ao Service.
        PerfilDTO perfil = perfilService.getPerfilDoUsuarioLogado();
        
        // Se o Service não lançar exceção (ou se for tratada por um @ControllerAdvice),
        // o retorno será 200 OK.
        return ResponseEntity.ok(perfil); 
    }

    /**
     * Atualiza o perfil do usuário logado.
     * Rota: PUT /api/perfil
     * Retorna: 200 OK e PerfilDTO atualizado.
     */
    @PutMapping
    public ResponseEntity<PerfilDTO> updateMeuPerfil(@Valid @RequestBody PerfilUpdateDTO updateDTO) {
        // O Service aplica as regras de negócio, a segurança e a atualização.
        PerfilDTO updatedPerfil = perfilService.updatePerfilDoUsuarioLogado(updateDTO);
        
        return ResponseEntity.ok(updatedPerfil);
    }
    
    /*
    // Opcional: Se quiser adicionar o DELETE para deletar a conta:
    @DeleteMapping
    public ResponseEntity<Void> deleteMinhaConta() {
        // Implementar perfilService.deletePerfilDoUsuarioLogado();
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
    */
}