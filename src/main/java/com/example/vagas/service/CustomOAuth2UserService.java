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
        OAuth2User oauth2User = super.loadUser(userRequest);

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name"); 
        
        Optional<User> userOptional = userRepository.findByUsername(email);
        
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            
            if (name != null) {
                user.setName(name);
            }
        } else {
            user = new User();
            user.setId(UUID.randomUUID()); 
            user.setUsername(email);
            
            if (name != null) {
                user.setName(name);
            }
            
            user.setPassword("OAUTH2_USER_NO_PASSWORD"); 
        }

        
        userRepository.save(user);

        return oauth2User; 
    }
}