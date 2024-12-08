package ar.uba.fi.ingsoft1.product.products;

import java.util.Map;

public record ProductDTO(
        long id,
        String name,
        String description,
        int stock,
        Map<String, String> attributes) {

    public ProductDTO(Product product) {
        this(product.getId(), product.getName(), product.getDescription(), product.getStock(), product.getAttributes());
    }
}
