package com.example.vagas.repository.specifications;

import com.example.vagas.dto.VagaFilterDTO;
import com.example.vagas.model.Vaga;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import org.springframework.lang.NonNull; 
// IMPORT NECESSÁRIO
import org.springframework.lang.Nullable; 

import java.util.List;

public class VagaSpecification implements Specification<Vaga> {

    private final VagaFilterDTO filters;

    public VagaSpecification(VagaFilterDTO filters) {
        this.filters = filters;
    }

    @Override
    // CORRIGIDO: CQ foi alterado para @Nullable, pois a interface Specification exige.
    public Predicate toPredicate(@NonNull Root<Vaga> root,
                                 @Nullable CriteriaQuery<?> cq, // <--- CORREÇÃO AQUI
                                 @NonNull CriteriaBuilder cb) {

        Predicate p = cb.conjunction();

        if (filters == null) return p;

        /** -------------------------- BUSCA FULLTEXT BÁSICA -------------------------- */
        if (StringUtils.hasText(filters.getQuery())) {
            String like = "%" + filters.getQuery().toLowerCase() + "%";

            Predicate byTitulo = cb.like(cb.lower(root.get("titulo")), like);

            Predicate byEmpresa = cb.like(
                    cb.lower(root.get("empresa").get("nomeFantasia")),
                    like
            );

            Predicate byCidade = cb.like(cb.lower(root.get("cidade")), like);

            p = cb.and(p, cb.or(byTitulo, byEmpresa, byCidade));
        }

        if (StringUtils.hasText(filters.getCidade())) {
            p = cb.and(p,
                    cb.equal(cb.lower(root.get("cidade")),
                            filters.getCidade().toLowerCase())
            );
        }

        if (StringUtils.hasText(filters.getUf())) {
            p = cb.and(p,
                    cb.equal(cb.lower(root.get("uf")),
                            filters.getUf().toLowerCase())
            );
        }

        if (StringUtils.hasText(filters.getTipoContrato())) {
            p = cb.and(p,
                    cb.equal(root.get("tipoContrato"),
                            Vaga.TipoContrato.valueOf(filters.getTipoContrato()))
            );
        }

        if (StringUtils.hasText(filters.getSenioridade())) {
            p = cb.and(p,
                    cb.equal(root.get("senioridade"),
                            Vaga.Senioridade.valueOf(filters.getSenioridade()))
            );
        }

        if (StringUtils.hasText(filters.getModeloTrabalho())) {
            p = cb.and(p,
                    cb.equal(root.get("modeloTrabalho"),
                            Vaga.ModeloTrabalho.valueOf(filters.getModeloTrabalho()))
            );
        }

        /** ------- FILTRO SKILLS (USANDO JSONB @>) ------- */
        List<String> skills = filters.getSkills();
        if (skills != null && !skills.isEmpty()) {

            String jsonArray = skills.toString().replace(" ", "");

            Expression<Boolean> jsonContains = cb.function(
                    "jsonb_contains",
                    Boolean.class,
                    root.get("skillsObrigatorias"),
                    cb.literal(jsonArray)
            );

            p = cb.and(p, jsonContains);
        }

        return p;
    }
}