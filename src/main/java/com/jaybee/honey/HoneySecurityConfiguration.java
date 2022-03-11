package com.jaybee.honey;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class HoneySecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // GET catalog, GET catalog/id
        http
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/catalog/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .httpBasic()
            .and()
                .csrf().disable();
    }
}
