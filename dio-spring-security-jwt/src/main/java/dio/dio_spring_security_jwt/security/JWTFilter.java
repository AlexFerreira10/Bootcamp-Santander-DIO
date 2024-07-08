package dio.dio_spring_security_jwt.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * Filtro para interceptar requisições e validar tokens JWT para autenticação.
 * Extende OncePerRequestFilter para garantir que o filtro seja executado uma vez por requisição.
 */
public class JWTFilter extends OncePerRequestFilter {

    // Método para processar a requisição e validar o token JWT.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extrai o token JWT do cabeçalho Authorization da requisição
        String token = request.getHeader(JWTCreator.HEADER_AUTHORIZATION);

        try {
            // Verifica se o token existe e começa com o prefixo definido em SecurityConfig
            if (token != null && token.startsWith(SecurityConfig.PREFIX)) {
                // Cria um objeto JWTObject a partir do token JWT recebido
                JWTObject tokenObject = JWTCreator.create(token, SecurityConfig.PREFIX, SecurityConfig.KEY);

                // Converte as roles do token em autorizações do Spring Security
                List<SimpleGrantedAuthority> authorities = tokenObject.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Cria um objeto de autenticação para armazenar no contexto de segurança
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(tokenObject.getSubject(), null, authorities);

                // Define o objeto de autenticação no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                // Limpa o contexto de segurança caso o token não seja válido
                SecurityContextHolder.clearContext();
            }

            // Continua o processamento da requisição
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                 | SignatureException | IllegalArgumentException e) {
            // Em caso de exceção na validação do token, retorna status 403 (Forbidden)
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }
    }


    // Converte uma lista de roles em uma lista de autorizações do Spring Security.
    private List<SimpleGrantedAuthority> authorities(List<String> roles){
        return roles.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
