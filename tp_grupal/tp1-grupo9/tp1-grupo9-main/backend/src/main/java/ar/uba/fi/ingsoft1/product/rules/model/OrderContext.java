package ar.uba.fi.ingsoft1.product.rules.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * This class contains the context of an order, represented as a list of items.
 * Each item contains a product and its quantity, and rules can operate
 * on either individual items (scope ITEM) or the entire order (scope ORDER).
 */
@Getter
public class OrderContext {

    // Lista de items en la orden
    private final Set<Item> items = new HashSet<>();

    public OrderContext() {
    }

    // Métodos para manejar los items
    public void addItem(Item item) {
        items.add(item);
    }

}
