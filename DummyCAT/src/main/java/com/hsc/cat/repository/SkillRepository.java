package com.hsc.cat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hsc.cat.entity.EmployeeDetails;
import com.hsc.cat.entity.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer>{

	Skill findBySkillName(String skillName);
}
