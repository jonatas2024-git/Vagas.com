import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioTokenRecuperacao extends JpaRepository<TokenRecuperacaoSenha, Long> {
    TokenRecuperacaoSenha findByToken(String token);
}
