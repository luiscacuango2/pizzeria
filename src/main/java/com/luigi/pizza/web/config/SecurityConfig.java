package com.luigi.pizza.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitar CSRF (Requerido para APIs Stateless)
                .csrf(AbstractHttpConfigurer::disable)
                // 2. Configurar CORS
                .cors(Customizer.withDefaults())
                // 3. Gestión de Sesión: Stateless (Sin estado para JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 4. Configuración de Autorización (Reglas de acceso)
                .authorizeHttpRequests(auth -> auth
                        // 1. Acceso Público
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll() // Permitir que se vea la interfaz de Swagger sin estar logueado
                        // 2. Pizzas: Clientes ven, Admin gestiona
                        .requestMatchers(HttpMethod.GET, "/api/pizzas/**").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/pizzas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/pizzas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/pizzas/**").hasRole("ADMIN")
                        // 3. Clientes: Lectura general y registro
                        .requestMatchers(HttpMethod.GET, "/api/customers").hasRole("ADMIN") // Ver lista total es de Admin
                        .requestMatchers(HttpMethod.GET, "/api/customers/{id}").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/customers").permitAll() // Permitir registro público si aplica
                        .requestMatchers("/api/customers/search", "/api/customers/phone/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/customers/**").hasRole("ADMIN")
                        // 4. Órdenes: El núcleo de la auditoría
                        .requestMatchers("/api/orders/random").hasAuthority("random_order")
                        .requestMatchers(HttpMethod.GET, "/api/orders/customer/**").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/*/status").hasRole("ADMIN")
                        .requestMatchers("/api/orders/**").hasRole("ADMIN")
                        // auditoria
                        .requestMatchers("/api/audit/**").hasRole("ADMIN")
                        // 5. Cualquier otra ruta requiere estar logueado
                        .anyRequest().authenticated()
                )
                // 5. Agregar el filtro JWT antes del filtro de usuario/password estándar
                .addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        try {
            return configuration.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
