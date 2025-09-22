import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class OtpUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    private LocalDateTime expiracao;

    @ManyToOne
    private Usuario usuario;

    public OtpUsuario() {}

    public OtpUsuario(String codigo, LocalDateTime expiracao, Usuario usuario) {
        this.codigo = codigo;
        this.expiracao = expiracao;
        this.usuario = usuario;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public LocalDateTime getExpiracao() { return expiracao; }
    public void setExpiracao(LocalDateTime expiracao) { this.expiracao = expiracao; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
