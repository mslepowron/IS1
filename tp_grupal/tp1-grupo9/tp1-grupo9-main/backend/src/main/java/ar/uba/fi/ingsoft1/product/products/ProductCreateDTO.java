package ar.uba.fi.ingsoft1.product.products;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

import java.util.Map;

record ProductCreateDTO(
        @NonNull @NotBlank String name,
        @NonNull @NotBlank String description,
        @NotBlank int stock,
        @NonNull @NotBlank Map<String, String> attributes
) {
    public Product asProduct() {
        return new Product(name, description, stock, attributes);
    }
}
