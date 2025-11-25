package Product_Service.SpringSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable());
//            .httpBasic(httpBasic -> httpBasic.disable())
//            .formLogin(form -> form.disable())
//            .logout(logout -> logout.disable())
//            .sessionManagement(session -> session.disable())
//            .securityContext(securityContext -> securityContext.disable())
//            .requestCache(requestCache -> requestCache.disable())
//            .anonymous(anonymous -> anonymous.disable())
//            .servletApi(servletApi -> servletApi.disable())
//            .headers(headers -> headers.disable());
            
        return http.build();
    }
}
