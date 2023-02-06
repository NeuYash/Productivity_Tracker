package com.cg.tasktracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.tasktracker.entity.AdminEntity;
@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
	
	boolean existsByEmail(String email);
	AdminEntity findByEmail(String email);
}
