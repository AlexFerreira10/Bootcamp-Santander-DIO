package com.dio.dio_spring_security_jwt.applications;

import com.dio.dio_spring_security_jwt.entities.User;
import com.dio.dio_spring_security_jwt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StartApplication implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injeção do BCryptPasswordEncoder

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        // Verifica se o usuário admin já existe
        User user = repository.findByUsername("admin");

        if(user == null){
            user = new User();
            user.setName("ADMIN");
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin123")); // Codifica a senha
            user.getRoles().add("MANAGERS");
            repository.save(user);
        }

        // Verifica se o usuário user já existe
        user = repository.findByUsername("user");

        if(user == null){
            user = new User();
            user.setName("USER");
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123")); // Codifica a senha
            user.getRoles().add("USERS");
            repository.save(user);
        }
    }
}