package ar.uba.fi.ingsoft1.product.orders;

import ar.uba.fi.ingsoft1.product.orders.states.States;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByEmail(String email);
    List<Order> findAllByState(States state);
}
