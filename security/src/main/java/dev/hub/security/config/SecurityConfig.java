package dev.hub.security.config;

import dev.hub.security.system.AppFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AppFilter appFilter;

    public SecurityConfig(@Lazy AppFilter appFilter) {
        this.appFilter = appFilter;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("==================== securityFilterChain Info =====================");

        http
                .addFilterBefore(appFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(AntPathRequestMatcher.antMatcher("/h2mw/**")).permitAll()
                                .requestMatchers((AntPathRequestMatcher.antMatcher("/auth/**"))).permitAll()
                                .requestMatchers((AntPathRequestMatcher.antMatcher("/api/join"))).permitAll()
                                .requestMatchers((AntPathRequestMatcher.antMatcher("/api/**"))).hasAnyRole("ADMIN")
                        //.requestMatchers((AntPathRequestMatcher.antMatcher("/admin/**"))).hasRole("ADMIN")
                )
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2mw/**"))
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/auth/**"))
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/api/**"))
                );
        return http.build();
    }
}
