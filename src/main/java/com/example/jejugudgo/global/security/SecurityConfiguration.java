package com.example.jejugudgo.global.security;

import com.example.jejugudgo.global.jwt.filter.JWTAuthenticationFilter;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import com.example.jejugudgo.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
//    private final CustomOAuth2UserService customOAuth2UserService;
//    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .authorizeHttpRequests((authorizeRequest) ->
                        authorizeRequest
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/api/v1/auth/signup").permitAll()
                                .requestMatchers("/api/v1/auth/login").permitAll()
                                .requestMatchers("/api/v1/auth/oauth/signup").permitAll()
                                .requestMatchers("/api/v1/auth/oauth/login").permitAll()
                                .requestMatchers("/api/v1/auth/logout").permitAll()
                                .requestMatchers("/api/v1/auth/sms/**").permitAll()
                                .requestMatchers("/api/v1/auth/check/id").permitAll()
                                .requestMatchers("/api/v1/auth/check/email").permitAll()
                                .requestMatchers("/api/v1/auth/find/id").permitAll()
                                .requestMatchers("/api/v1/auth/authentication/send/**").permitAll()
                                .requestMatchers("api/v1/auth/authentication/check/**").permitAll()
                                .requestMatchers("/api/v1/auth/user/**").permitAll()
                                .requestMatchers("/api/v1/courses/tour/spots/**").permitAll()
                                .requestMatchers("/oauth/**").permitAll()
                                .requestMatchers("/api/v1/oauth/**").permitAll()
                                .requestMatchers("/api/v1/auth/token/**").permitAll()
                                .requestMatchers("/ws/**").permitAll()
                                .requestMatchers("/favicon.ico/**").permitAll()
                                .requestMatchers("/static/**").permitAll()
                                .requestMatchers("/index.html").permitAll()
                                .requestMatchers("/openapi.yml").permitAll()
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling((exception) ->
                        exception
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .addFilterBefore(jwtAuthenticationFilter(cookieUtil, tokenUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customAuthenticationFilter(), JWTAuthenticationFilter.class);

//        httpSecurity
//                .oauth2Login(oauth2 -> oauth2
//                        .authorizationEndpoint(authorization -> authorization
//                                .baseUri("/api/v1/oauth/authorize")
//                        )
//                        .redirectionEndpoint(redirection -> redirection
//                                .baseUri("/oauth/callback")
//                        )
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userService(customOAuth2UserService)
//                        )
//                        .successHandler(oAuth2AuthenticationSuccessHandler)
//                );

        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addExposedHeader("Authorization");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(customUserDetailsService, bCryptPasswordEncoder());
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(CookieUtil cookieUtil, TokenUtil tokenUtil) {
        return new JWTAuthenticationFilter(cookieUtil, tokenUtil);
    }
}