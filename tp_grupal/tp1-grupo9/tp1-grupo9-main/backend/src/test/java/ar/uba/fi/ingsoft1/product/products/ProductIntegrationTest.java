/*
package ar.uba.fi.ingsoft1.product.products;

import ar.uba.fi.ingsoft1.product.brands.Brand;
import ar.uba.fi.ingsoft1.product.brands.BrandRepository;
import ar.uba.fi.ingsoft1.products.ProductCreateDTO;
import ar.uba.fi.ingsoft1.products.ProductService;
import ar.uba.fi.ingsoft1.products.ProductUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class ProductIntegrationTest {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    private static final Brand BRAND = new Brand(1L, "Brand", List.of());

    @BeforeEach
    void setup() {
        brandRepository.save(BRAND);
    }

    @Test
    void getAllReturnsCreatedProduct() throws Exception {
        var name1 = "Name";
        var description = "Description";

        productService.createProduct(
                new ProductCreateDTO(name1, description, BRAND.getId())
        );
        var request = get("/products").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpectAll(
                status().isOk(),
                jsonPath("$[0].id").isNumber(),
                jsonPath("$[0].name").value("Name"),
                jsonPath("$[0].description").value("Description")
        );
    }

    @Test
    void getReturnsLatestUpdate() {
        var name1 = "First Name";
        var name2 = "Second Name";
        var description = "Description";

        var id = productService.createProduct(
                new ProductCreateDTO(name1, description, BRAND.getId())
        ).id();
        productService.updateProduct(
                id,
                new ProductUpdateDTO(Optional.of(name2), Optional.empty())
        );
        var response = productService.getProductById(id);

        assertEquals(name2, response.orElseThrow().name());
        assertEquals(description, response.orElseThrow().description());
    }
}
*/
