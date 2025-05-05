package med.voll.api.controller;
/*O processo de autenticação está na classe AutenticacaoService
*Precisamos chamar o método loadUserByUsername, já que é ele que usa o repository para efetuar o select no banco de dados.
*Na classe Authentication Manager do Spring, responsável por disparar o processo de autenticação.
* DTO do proprio Spring var token = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
* Temos o nosso DTO e o Spring contém um próprio, também. O método authenticate(token) recebe o DTO do Spring.
* Por isso, precisamos converter para UsernamePasswordAuthenticationToken - como se fosse um DTO do próprio Spring.
*
* */

import jakarta.validation.Valid;
import med.voll.api.domain.usuario.DadosAutenticacao;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.infra.security.DadosTokenJWT;
import med.voll.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados){

        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());

        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));

    }

}
