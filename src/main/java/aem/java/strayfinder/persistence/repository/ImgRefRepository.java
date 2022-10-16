package aem.java.strayfinder.persistence.repository;

import aem.java.strayfinder.persistence.model.ImageRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgRefRepository extends JpaRepository<ImageRef, Long> {
}
