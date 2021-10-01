package com.tkis.qedbot.service;

import java.util.List;

import com.tkis.qedbot.entity.ProjectMaster;


public interface ProjectMasterService 
{

	public List<Object[]> getProjectIdAndName(int deliverableTypeId) throws Exception;
	
	public List<ProjectMaster> getProjectMaster() throws Exception;
	
	
	
}
