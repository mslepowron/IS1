package ar.uba.fi.ingsoft1.product.products.utils;

import java.util.List;

import ar.uba.fi.ingsoft1.product.products.Product;

public class ProductUtils {

    // Ver si se puede reemplazar por el ProductRepository o crear en service el método
    // productRepository.findById(id)
    public static Product findById(List<Product> products, Long id) {
        return products.stream()
                .filter(producto -> producto.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
