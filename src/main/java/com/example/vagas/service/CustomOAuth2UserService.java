package com.example.vagas.service;

import com.example.vagas.model.User;
import com.example.vagas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Carrega o usuário padrão do Google/OAuth2
        OAuth2User oauth2User = super.loadUser(userRequest);

        // 2. Extrai o e-mail (username)
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        
        // 3. Verifica se o usuário já existe no nosso banco de dados
        Optional<User> userOptional = userRepository.findByUsername(email);
        
        User user;
        if (userOptional.isPresent()) {
            // Usuário existente: Atualizar se necessário (ex: nome, foto)
            user = userOptional.get();
        } else {
            // Novo usuário: Criar um registro no nosso banco
            user = new User();
            user.setId(UUID.randomUUID()); 
            user.setUsername(email);
            // IMPORTANTE: Definimos uma senha aleatória para usuários OAuth2
            // Assim, eles não podem logar via formulário tradicional.
            user.setPassword("OAUTH2_USER_NO_PASSWORD"); 
            
            user = userRepository.save(user);
        }

        // 4. Retorna o OAuth2User padrão (o Spring fará o resto)
        return oauth2User; 
    }
}