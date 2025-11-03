package com.example.vagas.repository.specifications; 

import com.example.vagas.model.Vaga;
import com.example.vagas.dto.VagaFilterDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import org.springframework.lang.NonNull; 
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VagaSpecification implements Specification<Vaga> {

    private final VagaFilterDTO filters;

    public VagaSpecification(VagaFilterDTO filters) {
        this.filters = filters;
    }

    // CORREÇÃO: 'query' deve ser @Nullable para satisfazer a interface
    @Override
    public Predicate toPredicate(@NonNull Root<Vaga> root, @Nullable CriteriaQuery<?> query, @NonNull CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        // 1. FILTRO DE TERMO GERAL (Query - q)
        if (StringUtils.hasText(filters.getQuery())) {
            String queryPattern = "%" + filters.getQuery().toLowerCase() + "%";
            
            // Cria uma cláusula OR para buscar em múltiplos campos
            Predicate termPredicate = builder.or(
                builder.like(builder.lower(root.get("titulo")), queryPattern),
                builder.like(builder.lower(root.get("descricao")), queryPattern),
                builder.like(builder.lower(root.get("empresa").get("nome")), queryPattern)
            );
            predicates.add(termPredicate);
        }

        // 2. FILTRO DE LOCALIZAÇÃO (Location)
        if (StringUtils.hasText(filters.getLocation())) {
            String locationPattern = "%" + filters.getLocation().toLowerCase() + "%";
            predicates.add(builder.like(builder.lower(root.get("localizacao")), locationPattern));
        }

        // 3. FILTRO AVANÇADO: SALÁRIO MÍNIMO (minSalary)
        if (filters.getMinSalary() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("salarioMinimo"), filters.getMinSalary()));
        }

        // 4. FILTRO AVANÇADO: TIPO DE CONTRATO (contractType)
        if (StringUtils.hasText(filters.getContractType())) {
            predicates.add(builder.equal(builder.lower(root.get("tipoContrato")), filters.getContractType().toLowerCase()));
        }

        // 5. FILTRO AVANÇADO: NÍVEL DE EXPERIÊNCIA (experienceLevel)
        if (StringUtils.hasText(filters.getExperienceLevel())) {
            predicates.add(builder.equal(builder.lower(root.get("nivelExperiencia")), filters.getExperienceLevel().toLowerCase()));
        }
        
        // 6. FILTRO AVANÇADO: DATA DE PUBLICAÇÃO (publishedAfter)
        if (filters.getPublishedAfter() != null) {
            LocalDateTime startDateTime = filters.getPublishedAfter().atStartOfDay();
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataPublicacao"), startDateTime));
        }

        // Combina todos os predicados com AND
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}