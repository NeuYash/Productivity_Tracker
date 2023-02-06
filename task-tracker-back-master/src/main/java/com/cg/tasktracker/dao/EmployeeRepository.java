package com.cg.tasktracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.tasktracker.entity.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
	boolean existsByEmail(String email);

	EmployeeEntity findByEmail(String email);

	EmployeeEntity findByEmpId(String empId);

}
