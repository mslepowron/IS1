package ar.uba.fi.ingsoft1.product.rules.service;

import ar.uba.fi.ingsoft1.product.products.Product;
import ar.uba.fi.ingsoft1.product.products.utils.ProductUtils;
import ar.uba.fi.ingsoft1.product.rules.exceptions.QuantityException;
import ar.uba.fi.ingsoft1.product.rules.exceptions.RuleException;
import ar.uba.fi.ingsoft1.product.rules.model.Item;
import ar.uba.fi.ingsoft1.product.rules.model.Rule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RuleValidator {

    private static final Logger log = LoggerFactory.getLogger(RuleValidator.class);

    private final Integer maxQuantityPerProduct;
    private final Integer maxQuantityPerOrder;
    private final Set<Item> cart = new HashSet<>();

    public RuleValidator(List<Product> products, Integer maxQuantityPerProduct, Integer maxQuantityPerOrder) {
        createCart(products);
        // TODO: fetch a DB para los valores de maxQuantityPerProduct y maxQuantityPerOrder
        this.maxQuantityPerProduct = maxQuantityPerProduct;
        this.maxQuantityPerOrder = maxQuantityPerOrder;
    }

    public boolean isValidOrder(List<Rule> rules) throws RuleException, IllegalArgumentException {
        if (!isValidQuantity()) {
            return false;
        }

        for (Rule rule : rules) {
            if (!rule.isSatisfiedBy(cart)) {
                return false;
            }
        }
        return true;
    }

    private void createCart(List<Product> products) {
        Map<Long, Long> counter = products.stream()
                .collect(Collectors.groupingBy(Product::getId, Collectors.counting()));

        counter.forEach((id, quantity) -> cart.add(new Item(ProductUtils.findById(products, id), quantity)));
    }

    private boolean isValidQuantity() throws RuleException {
        long totalQuantity = cart.stream().mapToLong(Item::getQuantity).sum();
        log.info("Validating quantity: {} - maximum: {}", totalQuantity, maxQuantityPerOrder);
        if (totalQuantity > maxQuantityPerOrder) {
            throw new QuantityException(String.format("The total quantity of products (%d) exceeds the maximum allowed: %d", totalQuantity, maxQuantityPerOrder));
        }

        // Valida cada producto
        cart.forEach(item -> validateQuantityPerProduct(item.getQuantity(), item.getProduct().getName())); // Incluye detalles del producto
        return true;
    }

    private void validateQuantityPerProduct(Long quantity, String productName) {
        log.info("Validating quantity per product: {} for product '{}' - maximum: {}", quantity, productName, maxQuantityPerProduct);

        if (quantity > maxQuantityPerProduct) {
            throw new QuantityException(String.format(
                    "The quantity (%d) for product '%s' exceeds the maximum allowed: %d",
                    quantity, productName, maxQuantityPerProduct
            ));
        }
    }
}
