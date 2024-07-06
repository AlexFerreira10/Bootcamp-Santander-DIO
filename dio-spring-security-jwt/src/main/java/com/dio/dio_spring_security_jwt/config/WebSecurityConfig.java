package com.dio.dio_spring_security_jwt.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Indica que esta classe contém configurações de beans.
@EnableWebSecurity // Habilita a segurança web.
public class WebSecurityConfig {

    @Bean // Define um bean que será gerenciado pelo Spring.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Permite acesso público ao endpoint "/".
                                .requestMatchers(HttpMethod.GET, "/", "/login").permitAll()
                                // Permite acesso público ao endpoint "/login".
                                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                                // Restringe o acesso ao endpoint "/users" para usuários com os papéis "USERS" ou "MANAGERS".
                                .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("USERS", "MANAGERS")
                                // Restringe o acesso ao endpoint "/managers" para usuários com o papel "MANAGERS".
                                .requestMatchers(HttpMethod.GET, "/managers").hasRole("MANAGERS")
                                // Requer autenticação para qualquer outra solicitação.
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin.permitAll()); // Habilita a página de login padrão do Spring Security.
        return http.build(); // Constrói o objeto SecurityFilterChain.
    }

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
    }

    @Bean // Define um bean para PasswordEncoder que será gerenciado pelo Spring.
    public PasswordEncoder passwordEncoder() {
        // Retorna um codificador de senhas que utiliza o algoritmo BCrypt.
        return new BCryptPasswordEncoder();
    }
}

