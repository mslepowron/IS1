package ar.uba.fi.ingsoft1.product.orders;

import ar.uba.fi.ingsoft1.product.orders.states.States;
import ar.uba.fi.ingsoft1.product.products.Product;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

import java.util.List;

record OrderCreateDTO(
        @NonNull @NotBlank List<Long> ids
) {
    public Order asOrder(List<Product> products, States state, String email, String username) {
        return new Order(products, state, email, username);
    }
}
