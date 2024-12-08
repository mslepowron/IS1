package ar.uba.fi.ingsoft1.product.products;

import java.util.Map;
import java.util.Optional;

record ProductUpdateDTO(
        Optional<String> name,
        Optional<String> description,
        Optional<Integer> stock,
        Optional<Map<String, String>> attributes
) {
    public Product applyTo(Product product) {
        name.ifPresent(product::setName);
        description.ifPresent(product::setDescription);
        stock.ifPresent(product::setStock);
        attributes.ifPresent(product::setAttributes);
        return product;
    }
}
