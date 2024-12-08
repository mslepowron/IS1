package ar.uba.fi.ingsoft1.product.orders.states;

import ar.uba.fi.ingsoft1.product.orders.OrderDTO;
import ar.uba.fi.ingsoft1.product.products.Product;
import ar.uba.fi.ingsoft1.product.products.ProductRepository;

import java.util.List;

public interface State {

    States switchState(OrderDTO order, States state, String role);
    void stockManager(List<Product> products, ProductRepository productRepository);
}
