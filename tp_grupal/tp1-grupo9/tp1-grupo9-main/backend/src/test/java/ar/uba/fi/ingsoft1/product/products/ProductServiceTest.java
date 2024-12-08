/*
package ar.uba.fi.ingsoft1.product.products;

import ar.uba.fi.ingsoft1.product.brands.Brand;
import ar.uba.fi.ingsoft1.product.brands.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private ProductRepository productRepository;
    private BrandRepository brandRepository;

    private ProductService productService;

    private static final long ID = 1;
    private static final String NAME = "Product Name";
    private static final String DESCRIPTION = "Product Description";
    private static final Brand BRAND = new Brand(1L, "Brand", List.of());

    @BeforeEach
    void setup() {
        productRepository = mock();
        when(productRepository.save(new Product(null, NAME, DESCRIPTION, BRAND)))
                .thenReturn(new Product(ID, NAME, DESCRIPTION, BRAND));

        brandRepository = mock();
        when(brandRepository.getReferenceById(BRAND.getId())).thenReturn(BRAND);

        productService = new ProductService(productRepository, brandRepository);
    }

    @Test
    void createWritesToDatabase() {
        var newProduct = new ProductCreateDTO(NAME, DESCRIPTION, BRAND.getId());
        productService.createProduct(newProduct);
        verify(productRepository).save(new Product(NAME, DESCRIPTION, BRAND));
    }

    @Test
    void createReturnsCreatedProduct() {
        var newProduct = new ProductCreateDTO(NAME, DESCRIPTION, BRAND.getId());
        var response = productService.createProduct(newProduct);
        assertEquals(response, new ProductDTO(ID, NAME, DESCRIPTION));
    }
}*/
