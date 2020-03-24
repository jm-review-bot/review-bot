package spring.app.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
//@EnableWebMvc
@ComponentScan("spring.app")
@EnableTransactionManagement
public class ApplicationContextConfig {

	/*@Bean(initMethod = "init")
	@Autowired
	public TestDataInit initTestData(UserService userService, RoleService roleService) {
		return new TestDataInit(userService, roleService);
	}*/
}
