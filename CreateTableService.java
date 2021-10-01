package com.tkis.qedbot.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public interface CreateTableService 
{

	public List<String> getProjectList();
	public String upload(MultipartFile file, String projectName, String deliverableTypeName, String tabletype, String userId, boolean isSave, int projectId) throws Exception;
}
