package com.tkis.qedbot.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tkis.qedbot.entity.ConsistencyTracking;
import com.tkis.qedbot.service.ConsistencyTrackingService;
import com.tkis.qedbot.service.MasterDeliverableMappingService;
import com.tkis.qedbot.service.RepositoryDetailsService;

@Controller
public class UserController {

	@Autowired
	RepositoryDetailsService repositoryDetailsService;
	
	@Autowired
	MasterDeliverableMappingService masterDeliverableMappingService;
	
	@Autowired
	ConsistencyTrackingService consistencyTrackingService;
	
	
	/*
	@RequestMapping("/user-home")
	public ModelAndView dashboard() {
		ModelAndView modelAndView = new ModelAndView();		
		modelAndView.setViewName("dashboard");
		List<Object> entities = userDao.getAllUserProjects();
		modelAndView.addObject("dashboardData", entities);
		return modelAndView;
	}
	
	@RequestMapping("/user-proTracking")
	public ModelAndView userProTracking() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("projectTracking");
		return modelAndView;
	}
	
	@RequestMapping("/user-viewProjects")
	public ModelAndView viewProjects() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("viewProjectDetails");
		return modelAndView;
	}
	*/
	
	//Added on 15-09-2021 START	
	@RequestMapping("userDashboard")
	public ModelAndView userDashboard(ModelAndView modelAndView,HttpSession session) {
		//ModelAndView modelAndView = new ModelAndView();
		System.out.println("#### userDashboard");
		String userId = checkNull((String)session.getAttribute("userId"));
		if(userId.length() > 0) {
			modelAndView.setViewName("userDashboard");
		}else {
			modelAndView.setViewName("logout");
		}
		
		return modelAndView;
	}
	
	@RequestMapping("viewProjectDetails")
	public ModelAndView viewProjectDetails(ModelAndView modelAndView,HttpSession session) {
		System.out.println("#### viewProjectDetails");
		
		String userId = checkNull((String)session.getAttribute("userId"));
		
		if(userId.length() > 0) {
			try {
				modelAndView.addObject("keyField",repositoryDetailsService.getKeyFieldByProjectId(1));
				
				modelAndView.addObject("projectdata",masterDeliverableMappingService.getJSONMappingDataByProjectId(1));
				
			} catch (Exception e) {
				modelAndView.addObject("message",e.getMessage());
				e.printStackTrace();
			}
			modelAndView.setViewName("viewProjectDetails");
		}else {
			modelAndView.setViewName("logout");
		}
		return modelAndView;
	}
	//Added on 15-09-2021 END
	
	//Added on 01-10-2021 START
	//public String saveConsistency(@RequestBody ConsistencyTracking consistencyJsonData) {
	
	//Saving single json value
	
	@ResponseBody
	@PostMapping("/saveConsistency")	
	public String saveConsistency(@RequestBody ConsistencyTracking consistencyTracking, HttpSession session) {	
		
		//System.out.println("#### saveConsistency....."+consistencyTracking);
		//System.out.println("#### saveConsistency....."+consistencyTracking.toString());
		String userId = checkNull((String)session.getAttribute("userId"));
		if(userId.length() > 0) {
			//consistencyTracking.setFlaggedBy(userId);
			//consistencyTracking.setFlaggedDate(new Timestamp(new Date().getTime()));
			System.out.println("#### saveConsistency....."+consistencyTracking);
			consistencyTrackingService.saveConsistency(consistencyTracking);
		}
		return "success";
	}
	
	
	//Used for json array 
	/*
	@ResponseBody
	@PostMapping("/saveConsistency")	
	public String saveConsistencyList(@RequestBody List<ConsistencyTracking> consistencyTracking, HttpSession session) {	
		
		//System.out.println("#### saveConsistency....."+consistencyTracking);
		//System.out.println("#### saveConsistency....."+consistencyTracking.toString());
		consistencyTrackingService.saveConsistency(consistencyTracking);
		return "success";
	}
	*/
	//Added on 01-10-2021 END
	
	public String checkNull(String input)
    {
		System.out.println("#### Input String ["+input+"]");
        if(input == null || "null".equalsIgnoreCase(input) || "undefined".equalsIgnoreCase(input))
        input = "";
        return input.trim();    
    }
}
