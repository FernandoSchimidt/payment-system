package com.fernandoschimidt.paymentsystem.dto;

import com.fernandoschimidt.paymentsystem.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotNull(message = "O nome não pode ser vazio.")
        @NotBlank(message = "O nome não pode ser vazio.")
        @Size(min = 3, max = 100)
        String name,
        @NotNull(message = "O email nao pode ser vazio.")
        @NotBlank(message = "O email nao pode ser vazio.")
        @Email(message = "Insira um email valido.")
        String email,
        @NotNull(message = "A senha não pode ser vazia.")
        @NotBlank(message = "A senha não pode ser vazia.")
        @Size(min = 4, max = 10, message = "A Senha deve ter entre 4 e 10 caracteres.")
        String password,
        @NotNull(message = "A role não pode ser vazia.")
        @NotBlank(message = "A role não pode ser vazia.")
        String role
) {

    public User toModel() {
        return new User(name, email, password,role);
    }
}
