package ar.uba.fi.ingsoft1.product.orders.states;

import ar.uba.fi.ingsoft1.product.orders.OrderDTO;
import ar.uba.fi.ingsoft1.product.products.Product;
import ar.uba.fi.ingsoft1.product.products.ProductRepository;
import ar.uba.fi.ingsoft1.product.users.Role;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ar.uba.fi.ingsoft1.product.orders.states.States.CONFIRMED;
import static ar.uba.fi.ingsoft1.product.orders.states.States.SENT;

public class Processing implements State {
    @Getter
    private static final States status = States.PROCESSING;
    @Override
    public States switchState(OrderDTO order, States state, String role) {

        if (role.equals(Role.USER.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permissions denied");
        }

        if (state.equals(SENT)){
            return SENT;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Processing state is final");
    }

    @Override
    public void stockManager(List<Product> products, ProductRepository productRepository) {}
}
