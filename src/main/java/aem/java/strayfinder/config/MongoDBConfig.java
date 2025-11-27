package aem.java.strayfinder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "aem.java.strayfinder.persistence.images",
        mongoTemplateRef = "mongoTemplate"
)
public class MongoDBConfig {
}
