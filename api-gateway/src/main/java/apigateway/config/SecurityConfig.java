package apigateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {

        return serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(
                        authorizeExchangeSpec ->
                                authorizeExchangeSpec.pathMatchers("/eureka/**")
                                .permitAll()
                                .pathMatchers("/api/auth/**").permitAll()
                                .anyExchange()
                                .authenticated())
                                .oauth2ResourceServer(spec -> spec.jwt(Customizer.withDefaults())).build();

//                                authorizeExchangeSpec.anyExchange().permitAll() all access
//
//        serverHttpSecurity
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(exchange ->
//                        exchange.pathMatchers("/eureka/**")
//                                .permitAll()
//                                .anyExchange()
//                                .authenticated())
//                .oauth2ResourceServer(spec -> spec.jwt(Customizer.withDefaults()));
//        return serverHttpSecurity.build();
    }
}
