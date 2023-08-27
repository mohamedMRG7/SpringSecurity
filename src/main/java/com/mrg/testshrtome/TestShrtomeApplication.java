package com.mrg.testshrtome;

import com.mrg.testshrtome.entities.RoleEntity;
import com.mrg.testshrtome.entities.UserEntity;
import com.mrg.testshrtome.entities.UserRoleEntity;
import com.mrg.testshrtome.exception.ExceptionHndler;
import com.mrg.testshrtome.exception.customexceptions.AuthException;
import com.mrg.testshrtome.repos.RoleRepo;
import com.mrg.testshrtome.repos.UserRepo;
import com.mrg.testshrtome.repos.UserRoleRepo;
import com.mrg.testshrtome.security.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
@Slf4j
@RestController
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "com.*")
public class TestShrtomeApplication implements CommandLineRunner {
	@Autowired
	UserRepo userRepo;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	RoleRepo roleRepo;
	@Autowired
	UserRoleRepo userRoleRepo;
	@Autowired
	UserDetailsService userDetailsService;
	public static void main(String[] args) {
		SpringApplication.run(TestShrtomeApplication.class, args);
	}

	@GetMapping("/auth/signin")
	public ResponseEntity<String> hello(){

		UserEntity userDetails = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String,Object> claims =new HashMap();
		List roles =new ArrayList();
		userDetails.getAuthorities().forEach(grantedAuthority -> roles.add(grantedAuthority.getAuthority()));
		claims.put("roles",roles);
		String token = JWTUtils.generateJWTUserToken(userDetails.getUsername(),claims);
		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	@GetMapping("/auth")
	public String auth(){
		return "auth";
	}

	@Override
	public void run(String... args) throws Exception {
		UserEntity user1 =UserEntity.builder().userName("mrg").enabled("T").password(passwordEncoder.encode("123")).build();
		UserEntity user2 =UserEntity.builder().userName("cdc").enabled("T").password(passwordEncoder.encode("123")).build();
		RoleEntity role =RoleEntity.builder().role("ADMIN").build();

		RoleEntity roleEntity = roleRepo.save(role);
		UserEntity userEntity1 = userRepo.save(user1);
		UserEntity userEntity2 = userRepo.save(user2);
		Set<RoleEntity> roleEntities=new HashSet<>();
		roleEntities.add(roleEntity);
		userEntity1.setRoles(roleEntities);
		userRepo.save(userEntity1);
//		UserRoleEntity userRole =new UserRoleEntity(user1,roleEntity);
//		Set<UserRoleEntity> userRoleEntitySet =new HashSet<>();
//		userRoleEntitySet.add(userRole);
//		userEntity1.setRoles(userRoleEntitySet);
//		roleEntity.setUsers(userRoleEntitySet);
//		userRoleRepo.save(userRole);
//		userRepo.save(userEntity1);
	}
}
