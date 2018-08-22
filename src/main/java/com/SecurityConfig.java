package com;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebSecurity
@EnableSwagger2
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
    private DataSource dataSource;

	@Autowired
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
        .usersByUsernameQuery("select username, password, enabled"
            + " from user where username=?")
        .authoritiesByUsernameQuery("select username, authority "
                + "from authority where username=?")
            .passwordEncoder(new BCryptPasswordEncoder());

	}
	
	protected void configure(HttpSecurity http) throws Exception {

		 http.csrf().disable().sessionManagement().disable()
		 .authorizeRequests()
//		 .antMatchers("/account/findall").hasAuthority("ADMIN")
		 .antMatchers("/user/delete").hasAuthority("ADMIN")
		 .anyRequest().authenticated()
		 .and()
		 .formLogin()
		 .defaultSuccessUrl("/create")	 
		 .permitAll()
		 .and()
		 .logout()
		 .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		 .logoutSuccessUrl("/login")
		 .permitAll();
		
		 }

}
