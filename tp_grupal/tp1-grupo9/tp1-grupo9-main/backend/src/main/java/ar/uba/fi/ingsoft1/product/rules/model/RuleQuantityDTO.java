package ar.uba.fi.ingsoft1.product.rules.model;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RuleQuantityDTO {
    private Long id; // Opcional si usas IDs únicos en la tabla

    @Min(1)
    private Integer maxQuantityPerProduct;

    @Min(1)
    private Integer maxQuantityPerOrder;

    public RuleQuantityDTO() {
    }

    public RuleQuantityDTO(Integer maxQuantityPerProduct, Integer maxQuantityPerOrder) {
        this.id = 1L;
        this.maxQuantityPerProduct = maxQuantityPerProduct;
        this.maxQuantityPerOrder = maxQuantityPerOrder;
    }
}