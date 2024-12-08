package ar.uba.fi.ingsoft1.product.users;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record UserCreateDTO(

        @NonNull @NotBlank String name,
        @NonNull @NotBlank String surname,
        @NonNull @NotBlank String password,
        @NonNull @NotBlank String email,
        @NonNull @NotBlank String profilePicture,
        String gender,
        String age,
        String address
) {}
