package spring.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import spring.app.model.Role;
import spring.app.model.User;
import spring.app.service.abstraction.RoleService;
import spring.app.service.abstraction.UserService;

import java.util.Collection;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private PasswordEncoder passwordEncoder;
    private RoleService roleService;
    private UserService userService;

    @Autowired
    public SecurityConfig(@Lazy PasswordEncoder passwordEncoder,
                          RoleService roleService,
                          UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userService = userService;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            User loggedInUser = (User) authentication.getPrincipal();
            Collection<Role> roles = loggedInUser.getAuthorities();
            if (roles.contains(roleService.getRoleByName("ADMIN"))) {
                httpServletResponse.sendRedirect("/admin/theme");
            } else if (roles.contains(roleService.getRoleByName("USER"))) {
                // В текущем состоянии кода нет страницы для пользователя с ролью USER
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//                .authorizeRequests()
//                .antMatchers("api/admin/**")
//                .hasAnyAuthority("ADMIN")
//                    .and()
//                .authorizeRequests()
//                .antMatchers("/admin/**")
//                .hasAnyAuthority("ADMIN")
//                    .and()
//                .authorizeRequests()
//                .antMatchers("/user/**")
//                .hasAnyAuthority("USER")
//                    .and()
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                    .and()
//                .formLogin()
//                .successHandler(authenticationSuccessHandler())
//                    .and()
//                .logout();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
}
