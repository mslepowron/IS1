package ar.uba.fi.ingsoft1.product.products;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, name = "productname")
        private String name;

        @Column(nullable = false, name = "description")
        private String description;

        @Column(nullable = false, name = "stock")
        private int stock;

        @ElementCollection(fetch = FetchType.EAGER)
        private Map<String, String> attributes;

        public Product(String name, String description, int stock, Map<String, String> attributes) {
                this.name = name;
                this.description = description;
                this.stock = stock;
                this.attributes = attributes;
        }

        public Object getAttribute(String attributeName) {
                return Optional.ofNullable(this.getAttributes())
                        .orElse(new HashMap<>())
                        .get(attributeName);
        }

        public void subtractStock() {
                int updatedStock = this.getStock() - 1;
                if (updatedStock < 0) {
                        throw new IllegalArgumentException("Insufficient stock for product");
                }
                this.stock -= 1;
        }
        public void incrementStock() {
                this.stock += 1;
        }
}
