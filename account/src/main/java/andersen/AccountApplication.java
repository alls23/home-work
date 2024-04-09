package andersen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
//@EnableR2dbcRepositories
//@EnableDiscoveryClient
public class AccountApplication {

    public static void main(String[] args) {
        ReactorDebugAgent.init();
        SpringApplication.run(AccountApplication.class, args);
    }
}