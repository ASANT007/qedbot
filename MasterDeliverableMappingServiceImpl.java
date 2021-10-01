package com.tkis.qedbot.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.tkis.qedbot.entity.MasterDeliverableMapping;
import com.tkis.qedbot.repo.MasterDeliverableMappingRepo;

@Service
public class MasterDeliverableMappingServiceImpl implements MasterDeliverableMappingService {

	@Autowired
	MasterDeliverableMappingRepo masterDeliverableMappingRepo;
	
	@Override
	public String saveMapping(int projectId, String masterTable, String masterField, String deliverableTable,
			String deliverableField, String userId) throws Exception {
		//Saving single entry
		MasterDeliverableMapping masterDeliverableMapping = new MasterDeliverableMapping();
		masterDeliverableMapping.setProjectId(projectId);
		masterDeliverableMapping.setMasterTable(masterTable);
		masterDeliverableMapping.setMasterField(masterField);
		masterDeliverableMapping.setDeliverableTable(deliverableTable);
		masterDeliverableMapping.setDeliverableField(deliverableField);
		  
		masterDeliverableMapping.setStatus("Active");
		masterDeliverableMapping.setCreatedBy(userId);
		masterDeliverableMapping.setCreationDate(new Timestamp(new Date().getTime()));
		  
		masterDeliverableMappingRepo.save(masterDeliverableMapping);
		
		return "Mapping Saved Successfully";
	}

	@Override
	public String getMasterDeliverableMapping(int projectId) throws Exception {
		List<Object[]> mappingList = null;
		mappingList = masterDeliverableMappingRepo.getMasterDeliverableMapping(projectId);
		JSONArray arr = new JSONArray();
		JSONObject obj = null;
		
		for(Object[] mapping : mappingList) {
			int Id = 0;
			String masterTable = "", masterField = "",	deliverableTable = "", deliverableField = "";
			
			Id = (Integer) mapping[0];
			masterTable = (String) mapping[1];
			masterField = (String) mapping[2];
			deliverableTable = (String) mapping[3];
			deliverableField = (String) mapping[4];
			
			obj = new JSONObject();
            obj.put("mdMappingId",Id);
            obj.put("masterTable",masterTable);
            obj.put("masterField",masterField);
            obj.put("deliverableTable",deliverableTable);
            obj.put("deliverableField",deliverableField);
            
            arr.add(obj);
		}
		return arr.toString();
		
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public String deactiveMasterDeliverableMapping(String mappingIdList, String userId) throws Exception {
		String[] mappingIdArray = mappingIdList.split(",");
		for(String mdId : mappingIdArray) {
			System.out.println("### mdId : "+mdId);
			int row = masterDeliverableMappingRepo.updateMasterDeliverableMapping(Integer.valueOf(mdId), userId, new Timestamp(new Date().getTime()));
		}
		return "success";
	}

	@Override
	public boolean isMappingPresent(int projectId, String masterTable, String masterField, String deliverableTable,
			String deliverableField) {
		
		return masterDeliverableMappingRepo.isMappingPresent(projectId,masterTable,masterField,deliverableTable,deliverableField);
	}

	@Override
	public String getJSONMappingDataByProjectId(int projectId) throws Exception {
		
		String response = "";
		List<Object[]> mappingList = null;
		
		mappingList = masterDeliverableMappingRepo.getMasterDeliverableMapping(projectId);
		
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObjectVal = null;
		
		//JSONArray masterJSONArray = null;
		//JSONArray deliverableJSONArray = null;
		
		//JSONObject masterDataJSON = null;
		//JSONObject deliverableDataJSON = null;
		//int Id = 1;	
		//System.out.println("#### Count Will be "+mappingList.size());
		mainLoop :
		for(Object[] mapping : mappingList) {	
			 
			int mappingId = 0; String masterTable = "", masterField = "",	deliverableTable = "", deliverableField = "";
			
			mappingId = (Integer)mapping[0]; masterTable = (String) mapping[1]; masterField = (String) mapping[2]; 
			deliverableTable = (String) mapping[3];deliverableField = (String) mapping[4];
			
			List<String> masterDataValList = getTableColData(masterTable,masterField);
			List<String> deliverbleDataValList = getTableColData(deliverableTable,deliverableField);
			
			int maxLoop = deliverbleDataValList.size();
			
			if(masterDataValList.size() > deliverbleDataValList.size()) {
				maxLoop = masterDataValList.size() ;
			}
			
			for(int i = 0; i< maxLoop; i++) {
			
				String masterData = "", deliberableData = "";
				
				if(masterDataValList.size() > i) {
					masterData = masterDataValList.get(i);	
				}
				if(deliverbleDataValList.size() > i){
					deliberableData = deliverbleDataValList.get(i);
				}
				
				//System.out.println("#### MasterData "+masterData+" #### deliberableData "+deliberableData);
				jsonObjectVal = new JSONObject();
				
				jsonObjectVal.put("MAPPING_ID",mappingId);
				jsonObjectVal.put("KEY_FIELD","ABC");
				jsonObjectVal.put("DELIVERABLE_NAME",deliverableField);
				jsonObjectVal.put("MASTER_DATA", masterData);
				jsonObjectVal.put("DELIVERABLE_DATA",deliberableData);
				
				jsonArray.add(jsonObjectVal);
				
				/*
				 * if(i == 100) { break mainLoop;// temprary }
				 */
				
			}
			System.out.println("#### Size"+jsonArray.size());
			//System.out.println(jsonArray.toString());
			//System.out.println(jsonArray.toJSONString());
			//break mainLoop;// temprary
			
			
		}
		
		jsonObject.put("RETURN_STATUS", "");
		jsonObject.put("MAPPING_DATA", jsonArray);
		
		//System.out.println("#### JSON "+jsonObject.toJSONString());
		
		response = jsonObject.toString();
		//System.out.println("#### response "+response);
		
		return response;
	}
	
	
	@PersistenceContext
	private EntityManager entityManager;

	
	
	public List<String> getTableColData(String tableName, String colName) throws Exception 
	{
		List<String> dataList = null;
		Session session = null;
		int i = 0;
		
		if (entityManager == null || (session = entityManager.unwrap(Session.class)) == null) {
			throw new NullPointerException();
		}
		
		String sql = "select "+colName+" from "+tableName;
		dataList = session.createSQLQuery(sql).getResultList();
		
		return dataList;
	}
	

}
