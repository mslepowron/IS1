/*
package ar.uba.fi.ingsoft1.product.products;

import ar.uba.fi.ingsoft1.products.ProductCreateDTO;
import ar.uba.fi.ingsoft1.products.ProductDTO;
import ar.uba.fi.ingsoft1.products.ProductRestController;
import ar.uba.fi.ingsoft1.products.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getExistingProductById() throws Exception {
        final long id = 1;
        final String name = "Name";
        final String description = "Description";

        var dto = new ProductDTO(id, name, description);
        when(productService.getProductById(id)).thenReturn(Optional.of(dto));

        var request = get("/products/" + id)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(id),
                jsonPath("$.name").value(name),
                jsonPath("$.description").value(description)
        );
    }

    @Test
    void getAbsentProductById() throws Exception {
        final long id = 1;

        when(productService.getProductById(id)).thenReturn(Optional.empty());

        var request = get("/products/" + id)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpectAll(
                status().isNotFound()
        );
    }

    @Test
    void createProductSuccessfully() throws Exception {
        var newProduct = new ProductCreateDTO("Name", "Description", 1);
        var resultProduct = new ProductDTO(1L, "Name", "Description");
        when(productService.createProduct(newProduct)).thenReturn(resultProduct);

        var request = post("/products")
                .content("{\"name\":\"Name\",\"description\":\"Description\",\"brandId\":1}")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpectAll(
                status().isCreated(),
                jsonPath("$.id").value(resultProduct.id()),
                jsonPath("$.name").value(resultProduct.name()),
                jsonPath("$.description").value(resultProduct.description())
        );
    }

    @Test
    void createProductWithMalformedJson() throws Exception {
        var request = post("/products")
                .content("{")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void createProductWithBadJson() throws Exception {
        var request = post("/products")
                .content("{\"error\":1}")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpectAll(
                status().isBadRequest()
        );
    }
}*/
