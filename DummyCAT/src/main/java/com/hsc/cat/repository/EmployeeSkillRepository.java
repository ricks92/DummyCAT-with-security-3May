package com.hsc.cat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hsc.cat.entity.EmployeeSkillEntity;

@Repository
public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkillEntity, Integer>{

	//@Query("select e from EmployeeSkillEntity e where e.empId:=empId AND e.skillId:=skillId AND e.weekNumber:=weekNumber AND e.ratingDoneBy:=ratingDoneBy")
	EmployeeSkillEntity findByEmpIdAndSkillIdAndWeekNumberAndRatingDoneBy(String empId,int skillId,int weekNumber,String ratingDoneBy);
}
