/*
package ar.uba.fi.ingsoft1.product.products;

import ar.uba.fi.ingsoft1.product.brands.Brand;
import ar.uba.fi.ingsoft1.product.brands.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private Product*/
/**//*
Repository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    private static final Brand BRAND = new Brand(1L, "Brand Name", List.of());

    @BeforeEach
    void setup() {
        brandRepository.save(BRAND);
    }

    @Test
    void findByNameContaining() {
        var product1 = new Product("Product 1", "Description 1", BRAND);

        productRepository.save(product1);
        productRepository.save(new Product("Product 2", "Description 2", BRAND));
        var result = productRepository.findByNameContaining("t 1");

        assertEquals(List.of(product1), result);
    }
}*/
