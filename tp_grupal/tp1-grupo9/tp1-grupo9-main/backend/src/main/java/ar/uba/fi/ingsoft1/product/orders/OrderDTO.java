package ar.uba.fi.ingsoft1.product.orders;

import ar.uba.fi.ingsoft1.product.orders.states.States;
import ar.uba.fi.ingsoft1.product.products.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
        Long id,
        List<Product> products,
        States state,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        LocalDateTime createdAt,
        @JsonIgnore
        String email,
        String username
) {
    public OrderDTO(Order order) {
        this(order.getId(), order.getProducts(), order.getState(), order.getCreatedAt(), order.getEmail(), order.getUsername());
    }
}