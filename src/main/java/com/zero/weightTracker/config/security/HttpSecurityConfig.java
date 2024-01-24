package com.zero.weightTracker.config.security;

import com.zero.weightTracker.config.security.filter.JwtAuthenticationFilter;
import com.zero.weightTracker.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter authenticacionFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(crsfConfig -> crsfConfig.disable())
                .sessionManagement(sessionMagConfig -> sessionMagConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authenticacionFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests( authConfig -> {
                    // Public
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/auth/verification**").permitAll();
                    // Private
                    authConfig.requestMatchers(HttpMethod.GET, "/private/allUsers").hasAuthority("ROLE_" + Role.USER.name());
                    authConfig.requestMatchers(HttpMethod.POST, "/api/v1/weightTracker/createNewGoal").hasAuthority("ROLE_" + Role.USER.name());
                    authConfig.requestMatchers(HttpMethod.GET, "/api/v1/weightTracker/listAllGoalsForUser**").hasAuthority("ROLE_" + Role.USER.name());
                    authConfig.requestMatchers(HttpMethod.DELETE, "/api/v1/weightTracker/deleteGoal/**").hasAuthority("ROLE_" + Role.USER.name());
                    authConfig.requestMatchers(HttpMethod.PUT, "/api/v1/weightTracker/editGoal/**").hasAuthority("ROLE_" + Role.USER.name());
                    authConfig.requestMatchers(HttpMethod.POST, "/api/v1/weightTracker/createNewWeightRecord/**").hasAuthority("ROLE_" + Role.USER.name());
                    // Others
                    authConfig.anyRequest().denyAll();
                });

        return http.build();
    }

}
