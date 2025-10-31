package com.example.vagas.repository;

import com.example.vagas.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    // CORRIGIDO: O tipo de argumento deve ser UUID para corresponder ao User.id e ao SecurityUtils
    Optional<Perfil> findByUserId(UUID userId); 
}