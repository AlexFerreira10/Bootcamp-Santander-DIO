package dio.dio_spring_security_jwt.controller;

import dio.dio_spring_security_jwt.dto.Login;
import dio.dio_spring_security_jwt.dto.Sessao;
import dio.dio_spring_security_jwt.model.User;
import dio.dio_spring_security_jwt.repository.UserRepository;
import dio.dio_spring_security_jwt.security.JWTCreator;
import dio.dio_spring_security_jwt.security.JWTObject;
import dio.dio_spring_security_jwt.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

@RestController
public class LoginController {

    @Autowired
    private PasswordEncoder encoder; // Injeção de dependência do encoder para criptografia de senhas

    @Autowired
    private SecurityConfig securityConfig; // Injeção de dependência da configuração de segurança

    @Autowired
    private UserRepository repository; // Injeção de dependência do repositório de usuários

    public LoginController(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public Sessao logar(@RequestBody Login login){
        // Busca o usuário pelo nome de usuário fornecido no login
        User user = repository.findByUsername(login.getUsername());

        // Verifica se o usuário existe
        if(user != null) {
            // Verifica se a senha fornecida corresponde à senha armazenada no banco de dados
            boolean passwordOk = encoder.matches(login.getPassword(), user.getPassword());
            if (!passwordOk) {
                throw new RuntimeException("Senha inválida para o login: " + login.getUsername());
            }

            // Cria um objeto Sessao para retornar informações do usuário logado
            Sessao sessao = new Sessao();
            sessao.setLogin(user.getUsername());

            // Cria um objeto JWT para gerar o token JWT com informações do usuário
            JWTObject jwtObject = new JWTObject();
            jwtObject.setIssuedAt(new Date(System.currentTimeMillis())); // Data de emissão do token
            jwtObject.setExpiration(new Date(System.currentTimeMillis() + SecurityConfig.EXPIRATION)); // Data de expiração do token
            jwtObject.setRoles(user.getRoles()); // Define os papéis (roles) do usuário no token JWT

            // Cria o token JWT utilizando o JWTCreator com o prefixo e chave configurados
            sessao.setToken(JWTCreator.create(SecurityConfig.PREFIX, SecurityConfig.KEY, jwtObject));

            return sessao; // Retorna o objeto Sessao contendo o token JWT
        } else {
            throw new RuntimeException("Usuário não encontrado: " + login.getUsername());
        }
    }
}