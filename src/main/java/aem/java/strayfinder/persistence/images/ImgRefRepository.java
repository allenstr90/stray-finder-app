package aem.java.strayfinder.persistence.images;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgRefRepository extends MongoRepository<ImageRef, String> {
}
