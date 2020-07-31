package com.livroandroid.carros.api.infra.security;

import com.livroandroid.carros.api.usuarios.Users;
import com.livroandroid.carros.api.usuarios.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findByLogin(username);
        if (users == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return users;
    }
}
