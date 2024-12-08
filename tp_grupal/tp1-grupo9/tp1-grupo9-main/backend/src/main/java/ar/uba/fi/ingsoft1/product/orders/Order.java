package ar.uba.fi.ingsoft1.product.orders;

import ar.uba.fi.ingsoft1.product.orders.states.States;
import ar.uba.fi.ingsoft1.product.products.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "`order`")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "products_json", columnDefinition = "TEXT")
        private String productsJson;

        @Transient
        private List<Product> products;

        @CreationTimestamp
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(nullable = false, name = "state")
        @Enumerated(EnumType.STRING)
        private States state;

        @Column(nullable = false, name = "email")
        private String email;

        @Column(nullable = false, name = "username")
        private String username;

        private static final ObjectMapper objectMapper = new ObjectMapper();

        public Order(List<Product> products, States state, String email, String username) {
                setProducts(products);
                setState(state);
                setEmail(email);
                setUsername(username);
        }

        public List<Product> getProducts() {
                try {
                        return objectMapper.readValue(productsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));
                } catch (IOException e) {
                        throw new IllegalStateException("Error deserializing products from JSON", e);
                }
        }

        public void setProducts(List<Product> products) {
                this.products = products;
                try {
                        this.productsJson = objectMapper.writeValueAsString(products);
                } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException("Error serializing products to JSON", e);
                }
        }
}