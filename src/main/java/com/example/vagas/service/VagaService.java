package com.example.vagas.service;

import com.example.vagas.model.Vaga;
import com.example.vagas.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;

    // 1. Listar todas as vagas (Método original, útil para testes simples, mas evite em produção)
    public List<Vaga> listarTodasVagas() {
        return vagaRepository.findAll();
    }

    // 1b. Listar vagas com paginação (O método preferido para o controller)
    /**
     * Lista vagas com paginação.
     */
    public Page<Vaga> listarTodasVagasPaginadas(Pageable pageable) {
        return vagaRepository.findAll(pageable);
    }

    // 2. Buscar vaga por ID
    public Optional<Vaga> buscarPorId(Long id) {
        return vagaRepository.findById(id);
    }

    // 3. Salvar/Criar nova vaga
    public Vaga salvarVaga(Vaga vaga) {
        return vagaRepository.save(vaga);
    }

    // Atualizar vaga existente - Refazer
    public Optional<Vaga> atualizarVaga(Long id, Vaga detalhesVaga) {
        return vagaRepository.findById(id).map(vaga -> {
            vaga.setTitulo(detalhesVaga.getTitulo());
            if(detalhesVaga.getEmpresa() != null) {
                vaga.setEmpresa(detalhesVaga.getEmpresa());
            }
            return vagaRepository.save(vaga);
        });
    }
    
    // 4. Deletar vaga
    public void deletarVaga(Long id) {
        vagaRepository.deleteById(id);
    }

    /**
     * NOVO: Busca vagas por termo, abrangendo título e nome da empresa.
     * @param termoBusca Termo de busca fornecido pelo usuário.
     * @return Lista de vagas correspondentes.
     */
    public List<Vaga> buscarVagasPorTermo(String termoBusca) {
        return vagaRepository.buscarPorTermo(termoBusca);
    }
}