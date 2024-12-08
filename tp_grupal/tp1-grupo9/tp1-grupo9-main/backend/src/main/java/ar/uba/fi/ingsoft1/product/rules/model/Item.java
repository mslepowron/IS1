package ar.uba.fi.ingsoft1.product.rules.model;

import ar.uba.fi.ingsoft1.product.products.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * Represents a product and its quantity in a shopping cart.
 */
@Setter
@Getter
public class Item {
    private final Product product;
    private Long quantity;

    public Item(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Calculates the total amount of the numeric attribute of the item.
     * The attribute is multiplied by the quantity of the item.
     * @param attribute The String name of the attribute to calculate the total.
     * @return The total amount of the attribute.
     */
    public Double calculateTotal(String attribute) {
        return Double.parseDouble(Optional.ofNullable(product.getAttribute(attribute)).orElse("0.0").toString()) * quantity;
    }

    /**
     * Returns the value of the attribute of the product.
     * @param attributeName The name of the attribute to get.
     * @return The value of the attribute as an Object.
     */
    public Object getAttribute(String attributeName) {
        return this.product.getAttribute(attributeName);
    }
}