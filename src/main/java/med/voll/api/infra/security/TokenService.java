package med.voll.api.infra.security;
//Classe de serviço relacionado com TOKEN
/* algoritmo padrão por HMAC256
 *  gerar a validade chamando o método .withExpiresAt(), passando como parâmetro dataExpiracao()
 * "AutenticacaoController.java" e, logo abaixo do atributo AuthenticationManager, passaremos @Autowired, e, logo abaixo, private TokenService.
 *
 * */

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    //Informando ao spring pra ler pelo aplication properties o atributo secret
    @Value("${api.security.token.secret}")
    private String secret;


    //Classe que gera o token
    public String gerarToken(Usuario usuario) {

        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Voll.med")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }
    }

    //Classe pra configurar a expiração do token
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    //Classe para pegar um token JWT e verificar se é ativa ou não
    public String getsubject(String tokenJWT){
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API Voll.med")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }
}
