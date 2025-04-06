package com.inghub.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDto(
        @NotBlank
        String name,
        @Size(max = 500 , message = "Categroy description too long ")
        String description) {
}
