package aem.java.strayfinder.persistence.repository;

import aem.java.strayfinder.persistence.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Set<Tag> findAllByNameIn(Set<String> tags);
}
