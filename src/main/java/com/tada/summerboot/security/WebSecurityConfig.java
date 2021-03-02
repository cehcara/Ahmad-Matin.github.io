package com.tada.summerboot.security;

import com.tada.summerboot.component.SuccessHandler;
import com.tada.summerboot.service.CustomUserDetailsService;
import com.tada.summerboot.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	UserServiceImpl user_service_implementation;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	public void configure(WebSecurity web) {
		//DO NOT EDIT
		//do not authenticate these APIs
		web.ignoring()
				.antMatchers("/assets/**")
				.antMatchers("/about-us")
//				.antMatchers("/user/new")
//				.antMatchers("/user-photos/**")
//				.antMatchers("/products/**")
//				.antMatchers("/posts/**")
//				.antMatchers("/products/json/**") //is this necessary?
				.antMatchers("/users/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//DO NOT EDIT
		http
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/product").hasRole("ADMIN")
				.antMatchers("/admin").hasRole("ADMIN")
				.antMatchers("/order").permitAll()
				.antMatchers("/checkout").hasRole("USER")
				.antMatchers("/order-confirmed").hasRole("USER")
				.antMatchers("/guest-order").permitAll()
				.antMatchers("/register").permitAll()
				.antMatchers(HttpMethod.POST, "/user/new").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.permitAll()
				.successHandler(new SuccessHandler())
				.and()
				.logout()
				.permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());

	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
}

//	@Bean
//	@Override
//	public UserDetailsService userDetailsService() {
//		// Get all the users in database
//		// Use the service instead of repo.
//		List<com.tada.summerboot.model.User> users = user_service_implementation.getAllUsers();
//
//		// Prepare an ArrayList for the InMemoryUserDetailsManager method at the end of this function
//		ArrayList<UserDetails> list = new ArrayList<UserDetails>();
//
//		// Iterate (go through one by one) and build a UserDetails for this app
//		for(int i = 0; i < users.size(); i++) {
//			if(users.get(i).getUserType() != null){ // check if UserType is null
//				list = set_user_type(list, users.get(i));
//
//				// you must make sure there is only two possibilities
//			} else {
//				// Create a UserDetails instance but set it based on the user in database
//				UserDetails user = User.withDefaultPasswordEncoder()
//						.username(users.get(i).getUsername())
//						.password(users.get(i).getPassword())
//						.roles("ADMIN")
//						.build();
//
//				// Add that instance to the list
//				list.add(user);
//			}
//		}
//
//		list = adding_super_user(list);
//		//Have at least one admin user for developer to login
//		return new InMemoryUserDetailsManager(list);
//	}
//	//
//	private ArrayList<UserDetails>  set_user_type(ArrayList<UserDetails> list, com.tada.summerboot.model.User this_user) {
//		UserDetails user = User.withDefaultPasswordEncoder()
//				.username(this_user.getUsername())
//				.password(this_user.getPassword())
//				.roles(this_user.getUserType())
//				.build(); // This assumes that getUserType is either ADMIN or USER
//
//		list.add(user);
//		return list;
//	}
//
//	private ArrayList<UserDetails> adding_super_user(ArrayList<UserDetails> list) {
//		UserDetails admin =
//				User.withDefaultPasswordEncoder()
//						.username("admin")
//						.password("admin")
//						.roles("ADMIN")
//						.build();
//
//		list.add(admin);
//		return list;
//	}

//}
//	@Bean
//	@Override
//	public UserDetailsService userDetailsService() {
//
//		// Get all the users in database
//		// Use the service instead of repo.
//		List<com.tada.summerboot.model.User> users = user_service_implementation.getAllUsers();
//
//		// Prepare an ArrayList for the InMemoryUserDetailsManager method at the end of this function
//		ArrayList<UserDetails> list = new ArrayList<UserDetails>();
//
//		// Iterate (go through one by one) and build a UserDetails for this app
//		for (int i = 0; i < users.size(); i++) {
////			if (users.get(i).getUserType() == "USER") {
//				// Create a UserDetails instance but set it based on the user in database
//				UserDetails user =
//						User.withDefaultPasswordEncoder()
//								.username(users.get(i).getUsername())
//								.password(users.get(i).getPassword())
//								.roles("ADMIN")
//								.build();
//				// Add that instance to the list
//				list.add(user);
////			} else {
////				UserDetails user =
////						User.withDefaultPasswordEncoder()
////								.username(users.get(i).getUsername())
////								.password(users.get(i).getPassword())
////								.roles("ADMIN")
////								.build();
//////				// Add that instance to the list
////				list.add(user);
//			}
//
////			//Have at least one admin user for developer to login
////			UserDetails admin =
////					User.withDefaultPasswordEncoder()
////							.username("admin")
////							.password("admin")
////							.roles("ADMIN")
////							.build();
////			list.add(admin);
//			list = adding_super_user(list);
////		System.out.println(list);
//			return new InMemoryUserDetailsManager(list);
//
//	}
//		private ArrayList<UserDetails> adding_super_user (ArrayList < UserDetails > list) {
//			UserDetails admin =
//					User.withDefaultPasswordEncoder()
//							.username("admin")
//							.password("admin")
//							.roles("ADMIN")
//							.build();
//
//			list.add(admin);
//			return list;
//		}
//}