java
Copiar
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioOtpUsuario extends JpaRepository<OtpUsuario, Long> {
    OtpUsuario findByCodigoAndUsuarioEmail(String codigo, String email);
}
