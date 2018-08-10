package com.hsc.cat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hsc.cat.TO.UpdateSkillTO;
import com.hsc.cat.TO.ViewSkillListTO;
import com.hsc.cat.VO.UpdateSkillsListVO;
import com.hsc.cat.service.EmployeeSkillService;
import com.hsc.cat.utilities.JSONOutputEnum;
import com.hsc.cat.utilities.JSONOutputModel;
import com.hsc.cat.utilities.RESTURLConstants;
import com.hsc.cat.utilities.Roles;

@RestController
@RequestMapping("/cat/secure")
public class EmployeeSkillController {

	@Autowired
	private EmployeeSkillService updateSkillService;
	
	@ResponseBody
	@RequestMapping(value=RESTURLConstants.UPDATE_SKILL,method=RequestMethod.POST,produces = "application/json",consumes="application/json")
	@CrossOrigin
	@PreAuthorize("hasAnyRole('"+Roles.EMPLOYEE+"','"+Roles.MANAGER+"')")
	public JSONOutputModel updateSkill(@RequestBody UpdateSkillsListVO updateSkillsListVO) {
		JSONOutputModel output = new JSONOutputModel();
		
		List<UpdateSkillTO> updateSkillTOList=updateSkillService.updateSkill(updateSkillsListVO);
		if(updateSkillTOList!=null && !updateSkillTOList.isEmpty() && updateSkillTOList.size()!=0) {
			output.setData(updateSkillTOList);
			output.setStatus(JSONOutputEnum.SUCCESS.getValue());
			output.setMessage("Skills updated successfully");
		}
		
		else {
			output.setData(updateSkillTOList);
			output.setStatus(JSONOutputEnum.FAILURE.getValue());
			output.setMessage("Skills could not be updated");
		}
		
		
		return output;
		
	}
	
	
	@ResponseBody
	@RequestMapping(value=RESTURLConstants.VIEW_SKILL,method=RequestMethod.GET,produces = "application/json",consumes="application/json")
	@CrossOrigin
	@PreAuthorize("hasAnyRole('"+Roles.EMPLOYEE+"','"+Roles.MANAGER+"')")
	public JSONOutputModel viewSkills(@PathVariable ("empId") String empid) {
		JSONOutputModel output = new JSONOutputModel();
		ViewSkillListTO viewSkillListTO = updateSkillService.viewSkills(empid);
		if(viewSkillListTO!=null) {
			output.setData(viewSkillListTO);
			output.setStatus(JSONOutputEnum.SUCCESS.getValue());
			output.setMessage("Employee skills fetched successfully");
		}
		else {
			output.setData(viewSkillListTO);
			output.setStatus(JSONOutputEnum.FAILURE.getValue());
			output.setMessage("Employee skills entry not there");
		}
		
		
		
		return output;
	}
}
