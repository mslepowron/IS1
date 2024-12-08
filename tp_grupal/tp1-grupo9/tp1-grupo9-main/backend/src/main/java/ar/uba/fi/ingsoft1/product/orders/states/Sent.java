package ar.uba.fi.ingsoft1.product.orders.states;

import ar.uba.fi.ingsoft1.product.orders.OrderDTO;
import ar.uba.fi.ingsoft1.product.products.Product;
import ar.uba.fi.ingsoft1.product.products.ProductRepository;
import ar.uba.fi.ingsoft1.product.users.Role;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class Sent implements State {
    @Getter
    private static final States status = States.SENT;
    @Override
    public States switchState(OrderDTO order, States state, String role) {

        if (role.equals(Role.USER.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permissions denied");
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sent state is final");
    }

    @Override
    public void stockManager(List<Product> products, ProductRepository productRepository) {}
}
