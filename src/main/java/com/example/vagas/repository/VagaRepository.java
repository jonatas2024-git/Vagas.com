package com.example.vagas.repository;

import com.example.vagas.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, UUID>, JpaSpecificationExecutor<Vaga> {
    // métodos custom podem ser adicionados aqui se necessário
}
