package ar.uba.fi.ingsoft1.product.rules.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rules_quantity")
public class RuleQuantityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer maxQuantityPerProduct;

    @Column(nullable = false)
    private Integer maxQuantityPerOrder;

    public RuleQuantityEntity() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMaxQuantityPerProduct() {
        return maxQuantityPerProduct;
    }

    public void setMaxQuantityPerProduct(Integer maxQuantityPerProduct) {
        this.maxQuantityPerProduct = maxQuantityPerProduct;
    }

    public Integer getMaxQuantityPerOrder() {
        return maxQuantityPerOrder;
    }

    public void setMaxQuantityPerOrder(Integer maxQuantityPerOrder) {
        this.maxQuantityPerOrder = maxQuantityPerOrder;
    }

}
