package com.accenture.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration//Spring va regarder dans les classes @Configuration, chercher les @Bean, et créer un instance dans le conteneur de spring à utiliser
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)//????
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth->
                    auth
                            .requestMatchers("/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui/html"
                            ).permitAll()
                            .requestMatchers("/utilisateurs/**").permitAll()
                            //.requestMatchers("/taches/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/taches/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/taches/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/taches/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PATCH, "/taches/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/taches/**").hasAnyRole("ADMIN","SUPERADMIN")
                            .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean//pour chiffrer le password
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsManager userDetailsManager(DataSource dataSource){//dataSource est postgre ici, c'est le seul dans le projet, spring va le trouver
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("select login, password, 1 from utilisateur where login = ?");//1 est l'utilisateur valid==true : il a le droit d'utiliser le site
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select login, role from utilisateur where login = ?");
        return jdbcUserDetailsManager;
    }





}
