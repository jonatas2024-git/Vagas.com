package com.example.vagas.security;

import com.example.vagas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Injeta o repositório para buscar o usuário no banco
    @Autowired
    private UserRepository userRepository;

    /**
     * Carrega o usuário a partir do username (neste caso, o email) para o Spring Security.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // 1. Busca o usuário no banco de dados
        com.example.vagas.model.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> 
                    // Lança a exceção padrão do Spring Security se o usuário não for encontrado
                    new UsernameNotFoundException("Usuário não encontrado com o email: " + username)
                );

        // 2. Retorna um objeto UserDetails do Spring Security
        // Nota: Assumindo que seu modelo 'User' tenha os métodos getUsername() e getPassword()
        // e que a senha esteja criptografada no banco.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // Email/Username
                user.getPassword(), // Senha Criptografada
                Collections.emptyList() // Autoridades/Roles (adicione se você tiver roles de usuário)
        );
    }
}