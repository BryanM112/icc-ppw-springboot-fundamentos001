package ec.edu.ups.icc.fundamentos001.security.config;

// imports packages y clases....

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ec.edu.ups.icc.fundamentos001.security.filters.JwtAuthenticationEntryPoint;
import ec.edu.ups.icc.fundamentos001.security.filters.JwtAuthenticationFilter;
import ec.edu.ups.icc.fundamentos001.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                      JwtAuthenticationEntryPoint unauthorizedHandler,
                      JwtAuthenticationFilter jwtAuthenticationFilter,
                      JwtAccessDeniedHandler jwtAccessDeniedHandler) {
    this.userDetailsService = userDetailsService;
    this.unauthorizedHandler = unauthorizedHandler;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * DaoAuthenticationProvider: Proveedor de autenticación que conecta:
     * - UserDetailsService: Carga información del usuario desde BD
     * - PasswordEncoder: Valida la contraseña hasheada
     * 
     * Spring Security usa este provider para autenticar credenciales.
     * El constructor acepta directamente el UserDetailsService en Spring Boot 3.x/4.x
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF (no necesario para APIs REST con JWT)
            .csrf(AbstractHttpConfigurer::disable)

            // Configurar manejo de excepciones de autenticación
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            )

            // Configurar sesiones como stateless (no usar sesiones HTTP)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Configurar autorización de requests
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (sin autenticación)
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/status/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers(

                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()
                .requestMatchers("/actuator/health").permitAll()
                
                
                
                // Todos los demás endpoints requieren autenticación
                .anyRequest().authenticated()
            );

        // Agregar proveedor de autenticación
        http.authenticationProvider(authenticationProvider());

        // Agregar filtro JWT antes del filtro de autenticación estándar
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
