package cz.landspa.statsapp2.util;


import cz.landspa.statsapp2.model.entity.user.User;
import cz.landspa.statsapp2.repository.UserRepository;
import cz.landspa.statsapp2.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.*;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("dev")
public class DevAutoLoginFilter implements Filter {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    public DevAutoLoginFilter(UserRepository userRepository, UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByUsernameOrEmail("landy")
                    .orElseThrow(() -> new RuntimeException("User 'landy' not found in DB"));

            // Nebo použij UserDetails pro získání authorities
            UserDetails userDetails = userDetailsService.loadUserByUsername("landy");

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities() // authorities z UserDetails
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}

