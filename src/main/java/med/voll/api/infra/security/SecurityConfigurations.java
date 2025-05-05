package med.voll.api.infra.security;
//Classe de configuração anotação @Configuration
//Anotação @EnableWebSecurity para configurações personalizadas
//O objeto SecurityFilterChain do Spring é usado para configurar o processo de autenticação e de autorização.
// http.csrf().disable(). Serve para desabilitarmos proteção contra-ataques do tipo CSRF (Cross-Site Request Forgery).
//Desabilitado pra utilizar autenticação via tokens
//sessionManagement() método será responsável por configurar a autenticação para ser stateless.
//método sessionCreationPolicy(), qual a política de criação da sessão.
//Passando um objeto SessionCreationPolicy.STATELESS
//Precisa adicionar a anotação @Bean para exibir o retorno do método
//A anotação @Bean serve para exportar uma classe para o Spring, fazendo com que ele consiga carregá-la e realizar a sua injeção de dependência em outras classes.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/login").permitAll()
                   .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();
                    req.anyRequest().authenticated();

                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    //Classe pra que o spring injete o AuthenticationManager manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //Metodo pra usa o hashing BCrypt de senha.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
