package com.api.remessa.dto.request;

import com.api.remessa.enums.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(@NotBlank
                                String fullName,
                                @NotBlank
                                @Email
                                String email,
                                String password,
                                PersonType personType,
                                @NotBlank
                                String cpfCnpj) {
}
