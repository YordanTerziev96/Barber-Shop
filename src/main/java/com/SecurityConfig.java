package com;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebSecurity
@EnableSwagger2
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
    private DataSource dataSource;
	
	private AuthenticationManager authManager;
	
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
		 .antMatchers("/register").permitAll()
		 .antMatchers("/login").permitAll()
		 .antMatchers("/create").hasAnyAuthority("USER", "ADMIN")
		 .antMatchers("/css/**").permitAll()
		 .antMatchers("/user/**").hasAuthority("ADMIN")
		 .anyRequest().authenticated()
		 .and()
		 .formLogin().loginPage("/login")
		 .defaultSuccessUrl("/home")
		 .permitAll()
		 .and()
		 .logout()
		 .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		 .logoutSuccessUrl("/login")
		 .permitAll();
		
		 }
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/css/**", "/scripts/**");
	}
	
	public boolean autoLogin( String username, String password, HttpServletRequest request) {
	       
	        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
	 
	        Authentication authentication = authManager.authenticate(token);
	 
	        SecurityContextHolder.getContext().setAuthentication(authentication );
	 
	        //this step is important, otherwise the new login is not in session which is required by Spring Security
	        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
	        
	        
	        return true;
	    }

}