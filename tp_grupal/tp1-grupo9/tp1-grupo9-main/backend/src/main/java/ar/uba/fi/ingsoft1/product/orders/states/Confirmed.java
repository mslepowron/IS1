package ar.uba.fi.ingsoft1.product.orders.states;

import ar.uba.fi.ingsoft1.product.orders.OrderDTO;
import ar.uba.fi.ingsoft1.product.products.Product;
import ar.uba.fi.ingsoft1.product.products.ProductRepository;
import ar.uba.fi.ingsoft1.product.users.Role;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static ar.uba.fi.ingsoft1.product.orders.states.States.*;

@Component
public class Confirmed implements State {
    @Getter
    private static final States status = CONFIRMED;

    @Override
    public States switchState(OrderDTO order, States state, String role) {

        if(state.equals(CONFIRMED)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This order is confirmed");
        }

        if(state.equals(CANCELED)) {
            if(LocalDateTime.now().isBefore(order.createdAt().plusHours(24))) {
                return CANCELED;
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It cannot be canceled after 24 hours of creating the order");
        }

        if (role.equals(Role.ROLE_ADMIN.toString())) {
            return state;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permissions denied");

    }

    @Override
    public void stockManager(List<Product> products, ProductRepository productRepository) {
        products.forEach(productOrder -> productRepository.findById(productOrder.getId()).ifPresent(product -> {
            product.subtractStock();
            productRepository.save(product);
        }));
    }

}
