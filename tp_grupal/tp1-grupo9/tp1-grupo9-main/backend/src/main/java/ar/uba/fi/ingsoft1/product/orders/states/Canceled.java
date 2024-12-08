package ar.uba.fi.ingsoft1.product.orders.states;

import ar.uba.fi.ingsoft1.product.orders.OrderDTO;
import ar.uba.fi.ingsoft1.product.products.Product;
import ar.uba.fi.ingsoft1.product.products.ProductRepository;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class Canceled implements State {
    @Getter
    private static final States status = States.CANCELED;

    @Override
    public States switchState(OrderDTO order, States state, String role) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Canceled state is final");
    }

    @Override
    public void stockManager(List<Product> products, ProductRepository productRepository) {
        products.forEach(productOrder -> productRepository.findById(productOrder.getId()).ifPresent(product -> {
            product.incrementStock();
            productRepository.save(product);
        }));
    }
}
