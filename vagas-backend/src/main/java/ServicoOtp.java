import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ServicoOtp {

    private final RepositorioOtpUsuario repositorioOtpUsuario;
    private final ServicoEmail servicoEmail;

    public ServicoOtp(RepositorioOtpUsuario repositorioOtpUsuario, ServicoEmail servicoEmail) {
        this.repositorioOtpUsuario = repositorioOtpUsuario;
        this.servicoEmail = servicoEmail;
    }

    // Gera c�digo OTP e envia por e-mail
    public void gerarOtp(Usuario usuario) {
        String codigo = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(5);

        OtpUsuario otp = new OtpUsuario(codigo, expiracao, usuario);
        repositorioOtpUsuario.save(otp);

        servicoEmail.enviarEmail(usuario.getEmail(), "C�digo OTP", 
            "Seu c�digo OTP �: " + codigo + "\nValido por 5 minutos.");
    }

    // Valida c�digo OTP
    public boolean validarOtp(String email, String codigo) {
        OtpUsuario otp = repositorioOtpUsuario.findByCodigoAndUsuarioEmail(codigo, email);
        if (otp == null || otp.getExpiracao().isBefore(LocalDateTime.now())) {
            return false;
        }
        // Remove OTP ap�s uso
        repositorioOtpUsuario.delete(otp);
        return true;
    }
}
