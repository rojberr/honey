package com.jaybee.honey.security;

import com.jaybee.honey.user.db.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@AllArgsConstructor
public class HoneyUserDetailService implements UserDetailsService {

    private final UserEntityRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsernameIgnoreCase(username)
                .map(UserEntityDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
