package com.bbs.demo.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bbs.demo.model.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {
	Optional<Users> findByUsername(String username);  
	
}

  