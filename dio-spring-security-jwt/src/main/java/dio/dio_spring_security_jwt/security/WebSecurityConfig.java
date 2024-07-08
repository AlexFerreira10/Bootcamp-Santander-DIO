package dio.dio_spring_security_jwt.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.h2.server.web.JakartaWebServlet;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    // Configuração do encoder de senhas
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Lista de endpoints do Swagger que são liberados publicamente
    private static final String[] SWAGGER_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
    };

    // Configuração do filtro de segurança HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita CSRF
                .csrf(csrf -> csrf.disable())
                // Configura as regras de autorização
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Permite acesso público aos endpoints do Swagger
                                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                                // Permite acesso ao console do H2
                                .requestMatchers("/h2-console/**").permitAll()
                                // Permite acesso ao endpoint de login (POST)
                                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                                // Permite acesso ao endpoint de criação de usuários (POST)
                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                // Requer autenticação para acessar os dados dos usuários (GET)
                                .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("USERS", "MANAGERS")
                                // Requer papel de "MANAGERS" para acessar o endpoint "/managers"
                                .requestMatchers("/managers").hasAnyRole("MANAGERS")
                                // Qualquer outra requisição requer autenticação
                                .anyRequest().authenticated()
                )
                // Configura o gerenciamento de sessões como STATELESS (sem estado)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Desabilita as opções de frame para evitar ataques de clickjacking
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                )
                // Adiciona o filtro JWT após o filtro padrão de autenticação
                .addFilterAfter(new JWTFilter(), UsernamePasswordAuthenticationFilter.class);

        // Retorna a configuração do filtro de segurança HTTP
        return http.build();
    }

    // Configuração do servlet do console do H2
    @Bean
    public ServletRegistrationBean<JakartaWebServlet> h2servletRegistration(){
        ServletRegistrationBean<JakartaWebServlet> registrationBean = new ServletRegistrationBean<>(new JakartaWebServlet(), "/h2-console/*");
        return registrationBean;
    }
}