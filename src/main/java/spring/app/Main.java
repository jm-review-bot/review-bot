package spring.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import spring.app.configuration.initializator.TestDataInit;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean(initMethod = "init")
    @PostConstruct
    public TestDataInit initTestData() {
        return new TestDataInit();
    }
}
