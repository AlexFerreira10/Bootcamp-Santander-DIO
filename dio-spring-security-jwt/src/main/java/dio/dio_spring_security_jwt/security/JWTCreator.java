package dio.dio_spring_security_jwt.security;

import java.util.List;
import java.util.stream.Collectors;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * Classe utilitária para criação e validação de tokens JWT (JSON Web Token).
 * Permite criar tokens com informações de usuário e validar tokens recebidos para autenticação.
 */
public class JWTCreator {

    // Nome do cabeçalho usado para enviar o token JWT
    public static final String HEADER_AUTHORIZATION = "Authorization";
    // Chave para identificar as autoridades (papéis) no token JWT
    public static final String ROLES_AUTHORITIES = "authorities";

    /**
     * Cria um token JWT com base no objeto JWTObject fornecido.
     *
     * @param prefix Prefixo a ser adicionado ao token JWT (por exemplo, "Bearer")
     * @param key Chave secreta para assinar o token JWT
     * @param jwtObject Objeto contendo os dados a serem incluídos no token JWT
     * @return O token JWT gerado
     */
    public static String create(String prefix, String key, JWTObject jwtObject) {
        String token = Jwts.builder()
                .setSubject(jwtObject.getSubject()) // Define o assunto do token (geralmente o nome do usuário)
                .setIssuedAt(jwtObject.getIssuedAt()) // Define a data de emissão do token
                .setExpiration(jwtObject.getExpiration()) // Define a data de expiração do token
                .claim(ROLES_AUTHORITIES, checkRoles(jwtObject.getRoles())) // Define as autoridades (papéis) do usuário
                .signWith(SignatureAlgorithm.HS512, key) // Assina o token usando o algoritmo HS512 e a chave fornecida
                .compact(); // Compacta o token em uma String
        return prefix + " " + token; // Retorna o token com o prefixo especificado (por exemplo, "Bearer <token>")
    }

    /**
     * Cria um objeto JWTObject com base no token JWT fornecido.
     *
     * @param token Token JWT recebido (com prefixo incluso)
     * @param prefix Prefixo do token JWT (por exemplo, "Bearer")
     * @param key Chave secreta usada para verificar a assinatura do token
     * @return Objeto JWTObject contendo os dados extraídos do token JWT
     * @throws ExpiredJwtException Se o token JWT estiver expirado
     * @throws UnsupportedJwtException Se o token JWT não for suportado
     * @throws MalformedJwtException Se o token JWT estiver malformado
     * @throws SignatureException Se houver um problema com a assinatura do token JWT
     */
    public static JWTObject create(String token, String prefix, String key)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
        JWTObject object = new JWTObject();
        token = token.replace(prefix, ""); // Remove o prefixo do token
        Claims claims = Jwts.parser()
                .setSigningKey(key) // Define a chave secreta para verificar a assinatura
                .parseClaimsJws(token) // Faz o parsing do token JWT para extrair os claims
                .getBody(); // Obtém o corpo (claims) do token
        object.setSubject(claims.getSubject()); // Define o assunto do token no objeto JWTObject
        object.setExpiration(claims.getExpiration()); // Define a data de expiração do token no objeto JWTObject
        object.setIssuedAt(claims.getIssuedAt()); // Define a data de emissão do token no objeto JWTObject
        object.setRoles((List<String>) claims.get(ROLES_AUTHORITIES)); // Define as autoridades (papéis) no objeto JWTObject
        return object; // Retorna o objeto JWTObject preenchido com os dados do token JWT
    }

    /**
     * Converte uma lista de roles para o formato esperado no token JWT (prefixa com "ROLE_").
     *
     * @param roles Lista de roles a serem convertidos
     * @return Lista de roles no formato esperado para o token JWT
     */
    private static List<String> checkRoles(List<String> roles) {
        return roles.stream()
                .map(s -> "ROLE_" + s.replaceAll("ROLE_", "")) // Prefixa cada role com "ROLE_"
                .collect(Collectors.toList()); // Coleta os roles em uma lista
    }
}
