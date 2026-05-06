package com.lagom.config;

import com.lagom.jwt.JwtFilter;
import com.lagom.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // REST API는 CSRF 공격 위험 없으므로 비활성화
                .csrf(csrf -> csrf.disable())

                // JWT 사용하므로 서버에 세션 저장 안 함
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 로그인 API는 토큰 없어도 접근 허용
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers("/accounts/**").permitAll()
                        .requestMatchers("/transactions/**").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        // 나머지 API는 JWT 토큰 필수
                        .anyRequest().authenticated()
                )

                // UsernamePasswordAuthenticationFilter 이전에 JwtFilter 실행
                .addFilterBefore(new JwtFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}