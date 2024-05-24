package performanceservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;



@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClient() {
        return WebClient.builder();
    }

//    @Bean
//    @Primary
//    public DataSource dataSource() {
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5051/postgres");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("postgres");
//        return dataSource;
//    }
}
