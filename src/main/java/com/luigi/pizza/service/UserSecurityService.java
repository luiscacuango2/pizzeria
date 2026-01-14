package com.luigi.pizza.service;

import com.luigi.pizza.persistence.entity.UserEntity;
import com.luigi.pizza.persistence.entity.UserRoleEntity;
import com.luigi.pizza.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

        // Extraemos los roles de la entidad
        String[] roles = userEntity.getRoles().stream()
                .map(UserRoleEntity::getRole)
                .toArray(String[]::new);

        // Retornamos el UserDetails que usará Spring Security y nuestro AuditorAware
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(this.grantedAuthorities(roles)) // permiso específico
                .accountLocked(Boolean.TRUE.equals(userEntity.getLocked())) // Manejo seguro de Nulls
                .disabled(Boolean.TRUE.equals(userEntity.getDisabled()))
                .build();
    }

    private String[] getAuthorities(String role) {
        if ("ADMIN".equals(role) || "CUSTOMER".equals(role)) {
            return new String[]{"random_order"};
        } else {
            return new String[]{};
        }
    }

    private List<GrantedAuthority> grantedAuthorities(String[] roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.length);

        for (String role : roles) {
            // Añadir el rol con prefijo estándar de Spring
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

            // Añadir permisos específicos para este rol
            for (String authority : getAuthorities(role)) {
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        }

        return authorities;
    }

}
