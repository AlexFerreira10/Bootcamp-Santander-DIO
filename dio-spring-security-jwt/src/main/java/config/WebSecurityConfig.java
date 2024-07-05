package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // Configuração básica de usuários em memória
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USERS")
                .and()
                .withUser("manager").password("password").roles("MANAGERS");
    }

    // Configuração de autorização (quem pode acessar quais URLs)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll() // Permitir acesso sem autenticação à raiz
                .antMatchers("/users").hasAnyRole("USERS", "MANAGERS") // Apenas usuários com role USERS ou MANAGERS podem acessar /users
                .antMatchers("/managers").hasRole("MANAGERS") // Apenas usuários com role MANAGERS podem acessar /managers
                .anyRequest().authenticated() // Todas as outras URLs requerem autenticação
                .and()
                .httpBasic(); // Usar autenticação básica HTTP
    }

    // Para Spring Boot 2.x, é necessário fornecer um PasswordEncoder explícito
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
