package com.luigi.pizza.web.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil  jwtUtil;
    private final UserDetailsService  userDetailsService;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil,  UserDetailsService userDetailsService) {
        this.jwtUtil =  jwtUtil;
        this.userDetailsService =  userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. Validar que sea un Header Authorization válido
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Validar que el JWT sea válido
//        String jwt = authHeader.split(" ")[1].trim();
        String jwt = authHeader.substring(7).trim(); // Más eficiente que split

        if (!this.jwtUtil.isValid(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Cargar el usuario del UserDetailsService
        String username = this.jwtUtil.getUsername(jwt);
        User user = (User) this.userDetailsService.loadUserByUsername(username);

        // 4. Cargar al usuario en el contexto de seguridad SecurityContext, con 3 parámetros
        // IMPORTANTE: Pasamos null (en vez de user.getPassword()) en credentials porque el JWT ya fue validado
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), null, user.getAuthorities()
        );
        // Añadir detalles de la petición (IP, Sesión) necesarios para la Auditoría
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
        // Establecer la autenticación de forma global para este hilo (ThreadLocal)
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
