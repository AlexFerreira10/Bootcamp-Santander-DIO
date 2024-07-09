package com.dio.dio_spring_security.config;

import com.dio.dio_spring_security.entities.User;
import com.dio.dio_spring_security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SecurityDatabaseService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método da interface UserDetailsService para carregar informações do usuário pelo nome de usuário
    @Override
    public UserDetails loadUserByUsername(String username) {

        // Busca o usuário no repositório pelo nome de usuário
        User userEntity = userRepository.findByUsername(username);

        // Lança uma exceção se o usuário não for encontrado
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        // Prepara as autorizações do usuário com base nas roles associadas
        Set<GrantedAuthority> authorities = new HashSet<>();
        userEntity.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        });

        // Retorna um UserDetails com o nome de usuário, senha (já codificada) e autorizações
        return new org.springframework.security.core.userdetails.User(userEntity.getUsername(),
                userEntity.getPassword(),
                authorities);
    }
}