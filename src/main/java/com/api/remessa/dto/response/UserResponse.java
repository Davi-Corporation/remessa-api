package com.api.remessa.dto.response;

import com.api.remessa.enums.PersonType;

public record UserResponse(Long id,
                           String fullName,
                           String email,
                           PersonType personType,
                           String cpfCnpj) {
}
