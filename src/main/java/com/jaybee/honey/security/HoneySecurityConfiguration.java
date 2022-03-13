package com.jaybee.honey.security;


import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class HoneySecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    User systemUser() {
        return new User("systemUser", "", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // GET catalog, GET catalog/id
        http
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/catalog/**", "/uploads/**", "/manufacturers/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/orders", "/login").permitAll()
                .anyRequest().authenticated()
            .and()
                .httpBasic()
            .and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable();
    }

    @SneakyThrows
    private JsonUsernameAuthenticationFilter authenticationFilter() {
        JsonUsernameAuthenticationFilter filter = new JsonUsernameAuthenticationFilter();
        filter.setAuthenticationManager(super.authenticationManager());
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user@test.test")
                .password("{noop}secret")
                .roles("USER")
                .and()
                .withUser("admin")
                .password("{noop}admin")
                .roles("ADMIN");
    }
}
