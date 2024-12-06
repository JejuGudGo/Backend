package com.example.jejugudgo.global.security;

import com.example.jejugudgo.domain.user.account.service.SignUpService;
import com.example.jejugudgo.domain.user.athentication.signIn.validation.SignInValidation;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.global.jwt.filter.JWTAuthenticationFilter;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final TokenUtil tokenUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SignUpService signUpService;
    private final UserRepository userRepository;
    private final SignInValidation signInValidation;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/v1/signup/**", "/api/v1/signin/**", "/api/v1/auth/**", "/api/v1/find/**").permitAll()
                        .requestMatchers("/favicon.ico", "/static/**", "/resources/**", "/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter(tokenUtil), UsernamePasswordAuthenticationFilter.class) // JWT 필터를 먼저 실행
                .addFilterAt(customAuthenticationFilter(signUpService, userRepository), UsernamePasswordAuthenticationFilter.class) // 인증 필터를 다음으로 실행
                .authenticationProvider(customAuthenticationProvider());

        return httpSecurity.build();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(TokenUtil tokenUtil) {
        return new JWTAuthenticationFilter(tokenUtil);
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(SignUpService signUpService, UserRepository userRepository) throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(
                authenticationManager(authenticationConfiguration),
                signUpService,
                userRepository,
                signInValidation
        );
        filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        return filter;
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(customUserDetailsService, passwordEncoder, userRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}