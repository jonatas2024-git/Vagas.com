import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ServicoOAuth2Google implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final ProvedorJwt provedorJwt = new ProvedorJwt();
    private final RepositorioUsuario repositorioUsuario;

    public ServicoOAuth2Google(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest requisicao) {
        OAuth2User usuarioGoogle = new org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService()
                .loadUser(requisicao);

        String email = usuarioGoogle.getAttribute("email");
        String nome = usuarioGoogle.getAttribute("name");

        // Verifica se o usuário já existe
        Usuario usuario = repositorioUsuario.findByEmail(email);
        if (usuario == null) {
            // Cria novo usuário
            usuario = new Usuario(nome, email, "", "GOOGLE");
            repositorioUsuario.save(usuario);
        }

        // Gera tokens
        String token = provedorJwt.gerarToken(email);
        String refreshToken = provedorJwt.gerarRefreshToken(email);

        Map<String, Object> atributos = new HashMap<>(usuarioGoogle.getAttributes());
        atributos.put("token", token);
        atributos.put("refreshToken", refreshToken);

        return new DefaultOAuth2User(
                Collections.emptyList(),
                atributos,
                "email"
        );
    }
}
