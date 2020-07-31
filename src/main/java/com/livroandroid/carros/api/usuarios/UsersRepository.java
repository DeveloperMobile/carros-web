package com.livroandroid.carros.api.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByLogin(String login);
}
