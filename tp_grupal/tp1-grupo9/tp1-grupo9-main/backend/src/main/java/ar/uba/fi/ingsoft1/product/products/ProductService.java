package ar.uba.fi.ingsoft1.product.products;

import ar.uba.fi.ingsoft1.product.orders.Order;
import ar.uba.fi.ingsoft1.product.services.JwtService;
import ar.uba.fi.ingsoft1.product.users.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final JwtService jwtService;

    public List<ProductDTO> getProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductDTO::new)
                .toList();
    }

    private void validateRole(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);
        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));

        if(role.equals(Role.USER.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permissions denied");
        }
    }

    public ProductDTO createProduct(@RequestHeader("Authorization") String authorizationHeader, ProductCreateDTO data) {

        validateRole(authorizationHeader);
        return new ProductDTO(productRepository.save(data.asProduct()));
    }

    public Optional<ProductDTO> updateProduct(@RequestHeader("Authorization") String authorizationHeader, Long id, ProductUpdateDTO update) {

        validateRole(authorizationHeader);

        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) { throw new IndexOutOfBoundsException(); }

        return productOptional
                .map(update::applyTo)
                .map(productRepository::save)
                .map(ProductDTO::new);
    }
}
