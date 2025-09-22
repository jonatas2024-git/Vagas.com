import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class FiltroJwt extends OncePerRequestFilter {

    private final ProvedorJwt provedorJwt = new ProvedorJwt();

    @Override
    protected void doFilterInternal(HttpServletRequest requisicao,
                                    HttpServletResponse resposta,
                                    FilterChain chain) throws ServletException, IOException {

        String cabecalho = requisicao.getHeader("Authorization");

        if (cabecalho != null && cabecalho.startsWith("Bearer ")) {
            String token = cabecalho.substring(7);
            if (provedorJwt.validarToken(token)) {
                String email = provedorJwt.pegarEmailDoToken(token);

                UsernamePasswordAuthenticationToken autenticacao =
                        new UsernamePasswordAuthenticationToken(
                                new User(email, "", Collections.emptyList()),
                                null,
                                Collections.emptyList()
                        );

                autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(requisicao));

                SecurityContextHolder.getContext().setAuthentication(autenticacao);
            }
        }

        chain.doFilter(requisicao, resposta);
    }
}
