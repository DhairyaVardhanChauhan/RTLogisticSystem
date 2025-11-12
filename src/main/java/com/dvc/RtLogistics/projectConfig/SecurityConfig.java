package com.dvc.RtLogistics.projectConfig;


import com.dvc.RtLogistics.filters.JwtFilter;
import com.dvc.RtLogistics.service.authServices.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtFilter jwtFilter;


    @Bean
    public RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_USER");
        return  roleHierarchy;
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy){
        DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        defaultMethodSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
        return defaultMethodSecurityExpressionHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {});
        http.authorizeHttpRequests(
                (auth)->auth
                        .requestMatchers("/auth/v1/login","/auth/v1/refreshToken","/auth/v1/signup","/actuator/**","/ws/**","/topic/**", "/app/**")
                        .permitAll()
                        .anyRequest().authenticated()
        );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;


    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
