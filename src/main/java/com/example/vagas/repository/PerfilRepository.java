package com.example.vagas.repository;

import com.example.vagas.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    // O id da entidade Perfil é Long -> por isso JpaRepository<Perfil, Long>
    // O id do User (relacionamento) pode ser UUID, então este método usa UUID.
    Optional<Perfil> findByUserId(UUID userId);
}
