package ar.uba.fi.ingsoft1.product.users;

import lombok.NonNull;

public record UserLoginDTO(
        @NonNull String password,
        @NonNull String email
) {
}
