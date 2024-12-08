package ar.uba.fi.ingsoft1.product.rules.repository;

import ar.uba.fi.ingsoft1.product.rules.entity.RuleQuantityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleQuantityRepository extends JpaRepository<RuleQuantityEntity, Long> {
    // Additional query methods can be defined here if needed
}
