package spring.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import spring.app.configuration.initializator.TestDataInit;
import spring.app.core.StepHolder;
import spring.app.service.abstraction.RoleService;
import spring.app.service.abstraction.UserService;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableCaching
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
