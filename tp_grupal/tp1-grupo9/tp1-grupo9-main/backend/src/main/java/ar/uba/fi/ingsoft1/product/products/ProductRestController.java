package ar.uba.fi.ingsoft1.product.products;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@Validated
@RequiredArgsConstructor
class ProductRestController {
    private final ProductService productService;

    @GetMapping("/")
    public List<ProductDTO> getProducts() {
        return productService.getProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(
            @RequestHeader("Authorization") String authorizationHeader,
            @NonNull @RequestBody ProductCreateDTO data
    ) {
        return productService.createProduct(authorizationHeader, data);
    }

    @PatchMapping("/")
    public Optional<ProductDTO> updateProduct(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam() Long id,
            @NonNull @RequestBody ProductUpdateDTO data
    ) {
        return productService.updateProduct(authorizationHeader, id, data);
    }
}
