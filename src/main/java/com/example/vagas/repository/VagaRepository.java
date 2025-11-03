package com.example.vagas.repository;

import com.example.vagas.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // NOVO IMPORT
import org.springframework.stereotype.Repository;

@Repository
// ATUALIZAÇÃO: Adiciona JpaSpecificationExecutor<Vaga>
// Removemos a busca JPQL antiga que será substituída pelo Specification
public interface VagaRepository extends JpaRepository<Vaga, Long>, JpaSpecificationExecutor<Vaga> {
   
    // O método 'buscarPorTermo' foi removido, pois a busca avançada (Specification)
    // cobrirá todas as necessidades de filtro.
}