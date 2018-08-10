package com.hsc.cat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsc.cat.TO.UpdateSkillTO;
import com.hsc.cat.TO.ViewSkillListTO;
import com.hsc.cat.TO.ViewSkillTO;
import com.hsc.cat.VO.UpdateSkillVO;
import com.hsc.cat.VO.UpdateSkillsListVO;
import com.hsc.cat.entity.EmployeeDetails;
import com.hsc.cat.entity.EmployeeSkillEntity;
import com.hsc.cat.entity.Skill;
import com.hsc.cat.enums.ApprovalStatusEnum;
import com.hsc.cat.enums.LevelsEnum;
import com.hsc.cat.enums.RatingDoneByEnums;
import com.hsc.cat.repository.EmployeeDetailRepository;
import com.hsc.cat.repository.EmployeeSkillRepository;
import com.hsc.cat.repository.SkillRepository;


@Service
public class EmployeeSkillService {

	@Autowired
	private EmployeeSkillRepository employeeSkillRepository;
	@Autowired
	private EmployeeDetailRepository employeeDetailRepository;
	@Autowired
	private SkillRepository skillRepository;
	
	public List<UpdateSkillTO> updateSkill(UpdateSkillsListVO updateSkillsListVO) {
		List<EmployeeSkillEntity> updateSkillEntityList= new ArrayList<>();
		List<UpdateSkillVO> updateSkillVOList=updateSkillsListVO.getListOfEmployeeSkills();
		for(UpdateSkillVO updateSkillVO:updateSkillVOList) {
		EmployeeSkillEntity updateSkillEntity= new EmployeeSkillEntity();
		
		EmployeeSkillEntity recordExists=employeeSkillRepository.findByEmpIdAndSkillIdAndWeekNumberAndRatingDoneBy(updateSkillVO.getEmpId(), updateSkillVO.getSkillId(), updateSkillVO.getWeekNumber(), updateSkillVO.getRatingDoneBy());
		updateSkillEntity.setEmpId(updateSkillVO.getEmpId());
		updateSkillEntity.setSkillId(updateSkillVO.getSkillId());
		updateSkillEntity.setRating(LevelsEnum.getLevelNameFromLevel(updateSkillVO.getRating()));
		updateSkillEntity.setWeekNumber(updateSkillVO.getWeekNumber());
		updateSkillEntity.setComment(updateSkillVO.getComment());
		updateSkillEntity.setRatingDoneBy(updateSkillVO.getRatingDoneBy());
		updateSkillEntity.setRatingDoneByEmpId(updateSkillVO.getRatingDoneByEmpId());
  if(updateSkillVO.getRatingDoneBy().equalsIgnoreCase(RatingDoneByEnums.SELF.getType()) && !updateSkillVO.getEmpId().equals(updateSkillVO.getRatingDoneByEmpId())){
//do nothing when self data incorrect
	  System.out.println("Self data incorrect");
		}
  
  
  else if(updateSkillVO.getRatingDoneBy().equalsIgnoreCase(RatingDoneByEnums.MANAGER.getType())) {
	  EmployeeDetails manager=employeeDetailRepository.findOne(updateSkillVO.getRatingDoneByEmpId());
	  if(manager==null ) {
		  //do nothing when manager does not exist in the table
//		  System.out.println(manager.getManagerId()!=null);
		  System.out.println("manager does not exist in the table");
	  }
	  else if(recordExists==null && manager!=null && manager.getManagerId().length()<2 && !(manager.getApprovalStatus().equals(ApprovalStatusEnum.PENDING.getValue()))) {
		   employeeSkillRepository.save(updateSkillEntity);
			updateSkillEntityList.add(updateSkillEntity);
	  }
  }
  
  else if(updateSkillVO.getRatingDoneBy().equalsIgnoreCase(RatingDoneByEnums.PEER.getType())) {
	  EmployeeDetails peer=employeeDetailRepository.findOne(updateSkillVO.getRatingDoneByEmpId());
	  if(!employeeDetailRepository.exists(peer.getEmpid())) {
		  //do nothing when peer does not exist in the table
		  System.out.println(" peer does not exist in the table");
	  }
	  else {
	if(recordExists==null)
	{ employeeSkillRepository.save(updateSkillEntity);
			updateSkillEntityList.add(updateSkillEntity);
	}
	  }
  }
else
			{
				if (recordExists == null)

				{
					employeeSkillRepository.save(updateSkillEntity);
					updateSkillEntityList.add(updateSkillEntity);

				}
			}
		}
		List<UpdateSkillTO> updateSkillTOList=null;
		if(!updateSkillEntityList.isEmpty() && updateSkillEntityList.size()!=0)
			{
			updateSkillTOList=modelConversion(updateSkillEntityList);
			}
		
		return updateSkillTOList;
	}
	
	
	
	public ViewSkillListTO viewSkills(String empid) {
		ViewSkillListTO skillsList= new ViewSkillListTO();
		
		List<ViewSkillTO> viewSkillTOList=new ArrayList<>();
		List<EmployeeSkillEntity> employeeSkillEntityList=employeeSkillRepository.findAll();
		
		for(EmployeeSkillEntity employeeSkillEntity : employeeSkillEntityList) {
			if(employeeSkillEntity.getEmpId().equals(empid) && employeeSkillEntity.getRating()!=LevelsEnum.CANNOT_ASSESS.getLevelName()) {
				ViewSkillTO viewSkillTO = new ViewSkillTO();
				viewSkillTO.setEmpId(employeeSkillEntity.getEmpId());
				viewSkillTO.setSkillId(employeeSkillEntity.getSkillId());
				viewSkillTO.setRating(LevelsEnum.getLevelFromName(employeeSkillEntity.getRating()));
				viewSkillTO.setRatingDoneBy(employeeSkillEntity.getRatingDoneBy());
				viewSkillTO.setWeekNumber(employeeSkillEntity.getWeekNumber());
				Skill skill=skillRepository.findOne(employeeSkillEntity.getSkillId());
				viewSkillTO.setSkillName(skill.getSkillName());
				viewSkillTO.setRatingDoneByEmpId(employeeSkillEntity.getRatingDoneByEmpId());
				viewSkillTOList.add(viewSkillTO);
			}
			
			
			skillsList.setListOfEmployeeSkills(viewSkillTOList);
		}
		return skillsList;
	}
	
	
	
	public List<UpdateSkillTO> modelConversion(List<EmployeeSkillEntity> updateSkillEntityList) {
		List<UpdateSkillTO> updateSkillTOList= new ArrayList<>();
		for(EmployeeSkillEntity saved: updateSkillEntityList) {
		UpdateSkillTO updateSkillTO = new UpdateSkillTO();
		updateSkillTO.setId(saved.getId());
		updateSkillTO.setEmpId(saved.getEmpId());
		updateSkillTO.setSkillId(saved.getSkillId());
		updateSkillTO.setComment(saved.getComment());
		updateSkillTO.setRating(LevelsEnum.getLevelFromName(saved.getRating()));
		updateSkillTO.setRatingDoneBy(saved.getRatingDoneBy());
		updateSkillTO.setRatingDoneByEmpId(saved.getRatingDoneByEmpId());
		updateSkillTOList.add(updateSkillTO);
		
		}
		
		return updateSkillTOList;
	}
}
