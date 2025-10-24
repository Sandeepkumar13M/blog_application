//package com.blog_application.security;
//import com.blog_application.service.CustomUserDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
//import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        // Public pages
//                        .requestMatchers("/register", "/login", "/", "/post/**",
//                                "/css/**", "/js/**", "/comment/new").permitAll()
//
//                        // Web UI endpoints
//                        .requestMatchers("/newpost", "/post/*/edit", "/post/*/delete",
//                                "/comment/*/edit", "/comment/*/delete", "/comment/*/update")
//                        .hasAnyRole("ADMIN", "AUTHOR")
//
//                        // Public API endpoints
//                        .requestMatchers("/api/posts", "/api/posts/post/*",
//                                "/api/comments/post/*", "/api/users/register", "/api/users/login", "/api/comment/new")
//                        .permitAll()
//
//                        // Protected API endpoints
//                        .requestMatchers("/api/posts/newpost", "/api/users/profile")
//                        .hasAnyRole("ADMIN", "AUTHOR")
//
//                        // Final fallback rule
//                        .anyRequest().hasRole("ADMIN")
//                )
//                // Form login for web UI
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/", true)
//                        .permitAll()
//                )
//                // Basic auth for REST API
//                .httpBasic(withDefaults())
//                // Support both stateless API and session-based web UI
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                )
//                .logout(logout -> logout
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                        .logoutSuccessUrl("/")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//                )
//                // Disable CSRF for API endpoints only
//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers("/api/**")
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // Note: In production, use a secure password encoder like BCryptPasswordEncoder
//        return NoOpPasswordEncoder.getInstance(); // Keep temporarily
//    }
//
//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
//        hierarchy.setHierarchy("ADMIN > AUTHOR"); // Match database roles
//        return hierarchy;
//    }
//
//    @Bean
//    public UserDetailsService users(CustomUserDetailsService userDetailsService) {
//        return userDetailsService;
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider(
//            CustomUserDetailsService userDetailsService,
//            PasswordEncoder passwordEncoder
//    ) {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder);
//        return authProvider;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authenticationConfiguration
//    ) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//}

package com.blog_application.security;

import com.blog_application.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public pages
                        .requestMatchers("/register", "/login", "/", "/post/**",
                                "/css/**", "/js/**", "/comment/new").permitAll()

                        // Web UI endpoints
                        .requestMatchers("/newpost", "/post/*/edit", "/post/*/delete",
                                "/comment/*/edit", "/comment/*/delete", "/comment/*/update")
                        .hasAnyRole("ADMIN", "AUTHOR")

                        // Public API endpoints
                        .requestMatchers("/api/posts", "/api/posts/**",
                                "/api/comments/post/*", "/api/users/register", "/api/users/login", "/api/comment/new")
                        .permitAll()

                        // Protected API endpoints
                        .requestMatchers("/api/posts/newpost", "/api/users/profile")
                        .hasAnyRole("ADMIN", "AUTHOR")

                        // Final fallback rule
                        .anyRequest().hasRole("ADMIN")
                )
                // Form login for web UI
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                // Basic auth for REST API
                .httpBasic(withDefaults())
                // Support both stateless API and session-based web UI
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                // Disable CSRF for API endpoints only
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Note: In production, use a secure password encoder like BCryptPasswordEncoder
        return NoOpPasswordEncoder.getInstance(); // Keep temporarily
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ADMIN > AUTHOR"); // Match database roles
        return hierarchy;
    }

    @Bean
    public UserDetailsService users(CustomUserDetailsService userDetailsService) {
        return userDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}