package com.bing.usermgmt.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bing.usermgmt.entity.UserApp;

public interface UserAppRepository extends JpaRepository<UserApp, String>{
	Optional<UserApp> findByEmail(String email);
}
