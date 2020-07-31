package com.livroandroid.carros.api.usuarios;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersDTO {
    private String login;
    private String nome;
    private String email;

    // token jwt
    private String token;

    private List<String> roles;

    public static UsersDTO create(Users users) {
        ModelMapper modelMapper = new ModelMapper();
        UsersDTO dto = modelMapper.map(users, UsersDTO.class);

        dto.roles = users.getRoles().stream()
                .map(Role::getNome)
                .collect(Collectors.toList());

        return dto;
    }

    public static UsersDTO create(Users users, String token) {
        UsersDTO dto = create(users);
        dto.token = token;
        return dto;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        return m.writeValueAsString(this);
    }
}
