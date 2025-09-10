import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TokenRecuperacaoSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expiracao;

    @ManyToOne
    private Usuario usuario;

    public TokenRecuperacaoSenha() {}

    public TokenRecuperacaoSenha(String token, LocalDateTime expiracao, Usuario usuario) {
        this.token = token;
        this.expiracao = expiracao;
        this.usuario = usuario;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public LocalDateTime getExpiracao() { return expiracao; }
    public void setExpiracao(LocalDateTime expiracao) { this.expiracao = expiracao; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
