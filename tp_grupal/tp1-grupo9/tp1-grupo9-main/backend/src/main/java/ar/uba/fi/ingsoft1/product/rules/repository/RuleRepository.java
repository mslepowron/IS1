
package ar.uba.fi.ingsoft1.product.rules.repository;

import ar.uba.fi.ingsoft1.product.rules.entity.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<RuleEntity, Long> {
    // Additional query methods can be defined here if needed
    // TODO: investigar patron adapter para desacople de repository
    List<RuleEntity> findByDeletedFalse();
}