import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class ControladorAutenticacao {

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioTokenRecuperacao repositorioTokenRecuperacao;
    private final ProvedorJwt provedorJwt;
    private final PasswordEncoder codificadorSenha;
    private final ServicoOAuth2Google servicoOAuth2Google;
    private final ServicoEmail servicoEmail;
    private final ServicoOtp servicoOtp;

    public ControladorAutenticacao(
            RepositorioUsuario repositorioUsuario,
            RepositorioTokenRecuperacao repositorioTokenRecuperacao,
            ProvedorJwt provedorJwt,
            PasswordEncoder codificadorSenha,
            ServicoOAuth2Google servicoOAuth2Google,
            ServicoEmail servicoEmail,
            ServicoOtp servicoOtp) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioTokenRecuperacao = repositorioTokenRecuperacao;
        this.provedorJwt = provedorJwt;
        this.codificadorSenha = codificadorSenha;
        this.servicoOAuth2Google = servicoOAuth2Google;
        this.servicoEmail = servicoEmail;
        this.servicoOtp = servicoOtp;
    }


    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Map<String, String> dados) {
        String email = dados.get("email");
        String senha = dados.get("senha");
        String nome = dados.get("nome");

        if (repositorioUsuario.findByEmail(email) != null) {
            return ResponseEntity.badRequest().body("Usu�rio j� existe");
        }

        Usuario usuario = new Usuario(nome, email, codificadorSenha.encode(senha), "LOCAL");
        repositorioUsuario.save(usuario);

        return ResponseEntity.ok("Usu�rio cadastrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> dados) {
        String email = dados.get("email");
        String senha = dados.get("senha");

        Usuario usuario = repositorioUsuario.findByEmail(email);
        if (usuario == null || !codificadorSenha.matches(senha, usuario.getSenha())) {
            return ResponseEntity.status(401).body("Email ou senha incorretos");
        }

        String token = provedorJwt.gerarToken(email);
        String refreshToken = provedorJwt.gerarRefreshToken(email);

        Map<String, String> resposta = new HashMap<>();
        resposta.put("token", token);
        resposta.put("refreshToken", refreshToken);

        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> dados) {
        String refreshToken = dados.get("refreshToken");
        if (!provedorJwt.validarToken(refreshToken)) {
            return ResponseEntity.status(401).body("Refresh token inv�lido");
        }

        String email = provedorJwt.pegarEmailDoToken(refreshToken);
        String novoToken = provedorJwt.gerarToken(email);

        Map<String, String> resposta = new HashMap<>();
        resposta.put("token", novoToken);

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/teste")
    public ResponseEntity<?> testeAutenticacao(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (!provedorJwt.validarToken(token)) {
            return ResponseEntity.status(401).body("Token inv�lido");
        }
        String email = provedorJwt.pegarEmailDoToken(token);
        return ResponseEntity.ok("Token v�lido para usu�rio: " + email);
    }


    @PostMapping("/solicitar-recuperacao")
    public ResponseEntity<?> solicitarRecuperacao(@RequestBody Map<String, String> dados) {
        String email = dados.get("email");
        Usuario usuario = repositorioUsuario.findByEmail(email);
        if (usuario == null) {
            return ResponseEntity.badRequest().body("Usu�rio n�o encontrado");
        }

        // Gera token de recupera��o
        String token = UUID.randomUUID().toString();
        LocalDateTime expiracao = LocalDateTime.now().plusHours(1);
        TokenRecuperacaoSenha tokenRecuperacao = new TokenRecuperacaoSenha(token, expiracao, usuario);
        repositorioTokenRecuperacao.save(tokenRecuperacao);

        // Gera OTP de 6 d�gitos
        servicoOtp.gerarOtp(usuario);

        // Envia e-mail com link e instru��es
        String link = "https://localhost:8443/auth/redefinir-senha?token=" + token;
        servicoEmail.enviarEmail(email, "Recupera��o de senha",
                "Clique no link para redefinir sua senha:\n" + link +
                        "\n\nUse tamb�m o c�digo OTP enviado para este e-mail para confirmar a a��o.");

        return ResponseEntity.ok("E-mail de recupera��o enviado com OTP");
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@RequestBody Map<String, String> dados) {
        String token = dados.get("token");
        String novaSenha = dados.get("senha");
        String codigoOtp = dados.get("codigoOtp");

        TokenRecuperacaoSenha tokenRecuperacao = repositorioTokenRecuperacao.findByToken(token);
        if (tokenRecuperacao == null || tokenRecuperacao.getExpiracao().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token inv�lido ou expirado");
        }

        Usuario usuario = tokenRecuperacao.getUsuario();

        // Valida OTP
        if (!servicoOtp.validarOtp(usuario.getEmail(), codigoOtp)) {
            return ResponseEntity.badRequest().body("C�digo OTP inv�lido ou expirado");
        }

        // Atualiza senha
        usuario.setSenha(codificadorSenha.encode(novaSenha));
        repositorioUsuario.save(usuario);

        // Remove token usado
        repositorioTokenRecuperacao.delete(tokenRecuperacao);

        return ResponseEntity.ok("Senha redefinida com sucesso");
    }


    @PostMapping("/acao-critica")
    public ResponseEntity<?> acaoCritica(@RequestBody Map<String, String> dados) {
        String email = dados.get("email");

        Usuario usuario = repositorioUsuario.findByEmail(email);
        if (usuario == null) return ResponseEntity.badRequest().body("Usu�rio n�o encontrado");

        // Gera OTP e envia por e-mail
        servicoOtp.gerarOtp(usuario);

        return ResponseEntity.ok("C�digo OTP enviado por e-mail");
    }

    @PostMapping("/confirmar-acao-critica")
    public ResponseEntity<?> confirmarAcaoCritica(@RequestBody Map<String, String> dados) {
        String email = dados.get("email");
        String codigo = dados.get("codigo");

        if (!servicoOtp.validarOtp(email, codigo)) {
            return ResponseEntity.badRequest().body("C�digo OTP inv�lido ou expirado");
        }
        return ResponseEntity.ok("A��o cr�tica realizada com sucesso");
    }
}
