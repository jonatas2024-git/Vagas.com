package com.example.vagas.repository;

import com.example.vagas.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {
   
    @Query("SELECT v FROM Vaga v WHERE " +
           "LOWER(v.titulo) LIKE LOWER(CONCAT('%', :termoBusca, '%')) OR " +
           "LOWER(v.empresa.nome) LIKE LOWER(CONCAT('%', :termoBusca, '%'))")
    List<Vaga> buscarPorTermo(String termoBusca);
}