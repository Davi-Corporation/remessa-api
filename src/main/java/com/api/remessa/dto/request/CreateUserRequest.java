package com.api.remessa.dto.request;

import com.api.remessa.enums.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(@NotBlank
                                String fullName,
                                @NotBlank
                                @Email
                                String email,
                                String password,
                                @NotNull
                                PersonType personType,
                                @NotBlank
                                String cpfCnpj) {
}
