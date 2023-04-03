package com.cf.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
    @Autowired
    private DataSource dataSource;
     
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
        	.passwordEncoder(new BCryptPasswordEncoder())
            .dataSource(dataSource)
            .usersByUsernameQuery("select email, password, enabled from users where email=?")
            .authoritiesByUsernameQuery("select email, role from users where email=?")
        ;
       
//        auth.use
    }
//    @Bean
//	public PasswordEncoder getPasswordEncoder()
//	{
//		return NoOpPasswordEncoder.getInstance();
//	}
    
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/hr/**").hasAuthority("hr")
            .antMatchers("/commonInterviewerAndhrHead/**").hasAnyAuthority("hrHead","Interviewer")
        	.antMatchers("commonAll/**").hasAnyAuthority("hr","hrHead","Interviewer")
        	.antMatchers("/database/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login").permitAll();
    }
    
    
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//    	//HttpSecurity.httpBasic().disable();
////    	http.formLogin().disable();
//        http.csrf().disable().httpBasic().authenticationEntryPoint(new NoPopupBasicAuthenticationEntryPoint()).and().authorizeRequests()
//           
////    	.antMatchers("/**").hasAuthority("ROLE_ADMIN")
////        .antMatchers(
////                "/index*", "/static/**", "/*.js", "/*.json", "/*.ico")
////                .permitAll()
////                .antMatchers("/login.html").permitAll()
////                .antMatchers("/securityHome","/login","/login2").permitAll()
////        	.antMatchers("/css/**","/js/**","/images/**").permitAll()
//        	.antMatchers("/hr/**").hasAuthority("hr")
//        	.antMatchers("/interviewer/**").hasAuthority("interviewer")
//        	.antMatchers("/hrHead/**").hasAuthority("Hr-Head")
//        	.antMatchers("/commonInterviewerAndhrHead").hasAnyAuthority("hrHead","Interviewer")
//        	.antMatchers("commonAll").hasAnyAuthority("hr","hrHead","Interviewer")
//        	.antMatchers("/database/**").permitAll().anyRequest().authenticated()
//            .and()
//            .formLogin().loginPage("/login").permitAll()//.usernameParameter("email").passwordParameter("password").defaultSuccessUrl("/securityHome").failureUrl("/failureUrl")//
//            //.and()
//            
//            .and()
//            .logout().invalidateHttpSession(true)
//            .clearAuthentication(true).logoutUrl("/logout");//.logoutSuccessUrl("/login").permitAll();
//    }
//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//    @Bean
//	public UserDetailsService userDetailsService() {
//	    return super.userDetailsService();
//	}
    
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/oauth/token");
//        
}
