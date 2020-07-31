package com.livroandroid.carros.api.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {
    @Autowired
    private UsersRepository rep;

    public List<UsersDTO> getUsers() {
        return rep.findAll().stream().map(UsersDTO::create).collect(Collectors.toList());
    }
}
