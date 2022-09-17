package aem.java.strayfinder.persistence.repository;

import aem.java.strayfinder.persistence.model.Stray;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrayRepository extends JpaRepository<Stray, Long> {
}
