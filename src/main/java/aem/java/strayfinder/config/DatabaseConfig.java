package aem.java.strayfinder.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "aem.java.strayfinder.persistence.stray",
        entityManagerFactoryRef = "strayEntityManagerFactory",
        transactionManagerRef = "strayTransactionManager"
)
@EnableTransactionManagement
public class DatabaseConfig {

    private static final String STRAY_DATASOURCE = "strayDataSource";
    private static final String STRAY_EMF = "strayEntityManagerFactory";

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean strayEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier(STRAY_DATASOURCE) DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("aem.java.strayfinder.persistence.stray")
                .persistenceUnit("stray")
                .build();
    }

    @Bean(name = STRAY_DATASOURCE)
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource strayDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager strayTransactionManager(
            final @Qualifier(STRAY_EMF) EntityManagerFactory emf
    ) {
        return new JpaTransactionManager(emf);
    }
}
