package com.example.vagas.controller;

// Importações para a nova lógica baseada em DTOs e Service seguro
import com.example.vagas.service.PerfilService;
// NOVO: Import do SearchHistoryService
import com.example.vagas.service.SearchHistoryService; 
import com.example.vagas.dto.PerfilDTO;
import com.example.vagas.dto.PerfilUpdateDTO;
// NOVO: Import da Entidade SearchHistory
import com.example.vagas.model.SearchHistory; 

// Importações do Spring e Lombok
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; 
import java.util.List; // NOVO: Para retornar a lista de histórico

// Importações do Swagger/OpenAPI 3
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;


// O PerfilService é responsável por toda a lógica, incluindo a segurança.
@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor 
@Tag(name = "Perfil", description = "Endpoints para visualização e atualização do perfil do usuário logado. Requer JWT em todos os endpoints.")
@SecurityRequirement(name = "bearerAuth") // Aplica JWT globalmente a este Controller
public class PerfilController {

    private final PerfilService perfilService;
    // NOVO: Injeção do serviço de histórico de buscas
    private final SearchHistoryService searchHistoryService; 


    @Operation(summary = "Obter Perfil",
               description = "Retorna os dados do perfil do usuário atualmente autenticado.")
    @ApiResponse(responseCode = "200", description = "Perfil retornado com sucesso.")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    @ApiResponse(responseCode = "401", description = "Não autenticado.")
    @GetMapping
    public ResponseEntity<PerfilDTO> getMeuPerfil() {
        PerfilDTO perfil = perfilService.getPerfilDoUsuarioLogado();
        return ResponseEntity.ok(perfil); 
    }

    @Operation(summary = "Atualizar Perfil",
               description = "Atualiza os dados do perfil do usuário logado (nome, email, etc.).")
    @RequestBody(content = @Content(schema = @Schema(implementation = PerfilUpdateDTO.class)), required = true, description = "Campos a serem atualizados.")
    @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados de atualização inválidos (ex: email já em uso).")
    @ApiResponse(responseCode = "401", description = "Não autenticado.")
    @PutMapping
    public ResponseEntity<PerfilDTO> updateMeuPerfil(@Valid @RequestBody PerfilUpdateDTO updateDTO) {
        PerfilDTO updatedPerfil = perfilService.updatePerfilDoUsuarioLogado(updateDTO);
        return ResponseEntity.ok(updatedPerfil);
    }
    
    // =========================================================================
    // NOVO: HISTÓRICO DE BUSCAS (Item 13)
    // =========================================================================
    @Operation(summary = "Obter Histórico de Buscas",
               description = "Retorna a lista de termos e filtros que o usuário logado buscou recentemente.")
    @ApiResponse(responseCode = "200", description = "Lista de histórico de buscas retornada com sucesso.")
    @ApiResponse(responseCode = "401", description = "Não autenticado.")
    @GetMapping("/search-history")
    public ResponseEntity<List<SearchHistory>> getMySearchHistory() {
        // O serviço garante que apenas o histórico do usuário logado seja retornado
        List<SearchHistory> history = searchHistoryService.getMySearchHistory();
        return ResponseEntity.ok(history);
    }
    
    /*
    // Opcional: Documentação para o DELETE
    @Operation(summary = "Deletar Conta (Opcional)",
               description = "Deleta a conta do usuário logado e todas as suas entidades associadas.")
    @ApiResponse(responseCode = "204", description = "Conta deletada com sucesso (No Content).")
    @DeleteMapping
    public ResponseEntity<Void> deleteMinhaConta() {
        // perfilService.deletePerfilDoUsuarioLogado();
        return ResponseEntity.noContent().build(); 
    }
    */
}