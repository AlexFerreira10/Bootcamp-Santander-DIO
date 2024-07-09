package com.dio.dio_spring_security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Habilita a segurança baseada em métodos
public class WebSecurityConfig {

    @Autowired
    private SecurityDatabaseService securityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Configura o AuthenticationManagerBuilder para usar o serviço de segurança personalizado
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityService).passwordEncoder(passwordEncoder);
    }

    // Configura o filtro de segurança HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Define as permissões de acesso para diferentes endpoints
                                .requestMatchers(HttpMethod.GET, "/", "/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("USERS", "MANAGERS")
                                .requestMatchers(HttpMethod.GET, "/managers").hasRole("MANAGERS")
                                .anyRequest().authenticated() // Todos os outros requests precisam de autenticação
                )
                .httpBasic(Customizer.withDefaults()); // Configura autenticação básica HTTP
        return http.build(); // Retorna o objeto SecurityFilterChain configurado
    }
}

    /*
    @Bean // Define um bean para UserDetailsService que será gerenciado pelo Spring.
    public UserDetailsService userDetailsService() {
        // Cria um usuário com o nome de usuário "user", senha "user123" e papel "USERS".
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USERS")
                .build();

        // Cria um administrador com o nome de usuário "admin", senha "admin123" e papel "MANAGERS".
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("MANAGERS")
                .build();

        // Retorna um gerenciador de usuários em memória com os dois usuários criados acima.
        return new InMemoryUserDetailsManager(user, admin);
    }*/
