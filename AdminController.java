package com.tkis.qedbot.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.tkis.qedbot.service.Authentication;
import com.tkis.qedbot.service.CreateTableService;
import com.tkis.qedbot.service.DeliverabletypeMasterService;
import com.tkis.qedbot.service.MasterDeliverableMappingService;
import com.tkis.qedbot.service.ProjectMasterService;
import com.tkis.qedbot.service.RepositoryDetailsService;
import com.tkis.qedbot.service.RuleMasterService;
import com.tkis.qedbot.service.TableModificationService;

@Controller
public class AdminController {

	@Autowired
	CreateTableService createTableService;
	
	@Autowired
	RepositoryDetailsService repositoryDetailsService;
	
	
	@Autowired
	TableModificationService tableModificationService;
	
	@Autowired
	RuleMasterService ruleMasterService;
	
	@Autowired
	ProjectMasterService projectMasterService;
	
	@Autowired
	DeliverabletypeMasterService deliverabletypeMasterService;	
	
	@Autowired
	Authentication authentication;
	
	@Autowired
	MasterDeliverableMappingService masterDeliverableMappingService;
	
	//Show create table page
	@RequestMapping("/createTable")
	public String createTable(Model model) 
	{		
		try 
		{
			model.addAttribute("deliverableType",deliverabletypeMasterService.getDeliverableIdAndShortName());
			
		} catch (Exception e) 
		{
			
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}
		return "createTable";
	}
	
	//Return projectList for deliverable type.
	@ResponseBody
	@RequestMapping("/getProjectsForDeliverableType") 
	public List<Object[]> getProjects(@RequestParam("deliverableTypeId") int deliverableTypeId, Model model){
		
		List<Object[]> projectList = null;
		
		try {
			projectList = projectMasterService.getProjectIdAndName(deliverableTypeId);
			} catch (Exception e) { 
				model.addAttribute("message", e.toString());
				e.printStackTrace(); 
			}
	  
		return projectList;
	}
	 
	 
	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping("/getProjectsForDeliverableType") public
	 * ResponseEntity<Object> getProjects(@RequestParam("deliverableTypeId") int
	 * deliverableTypeId, HttpSession session, HttpServletResponse response){
	 * 
	 * String userId = checkNull((String) session.getAttribute("userId"));
	 * System.out.println("#### getProjects : userId "+userId); try {
	 * if(userId.length() > 0){ return new
	 * ResponseEntity<Object>(projectMasterService.getProjectIdAndName(
	 * deliverableTypeId), HttpStatus.OK) ; //return new ResponseEntity<Object>(
	 * "[[1,\"Project1\"],[2,\"Project2\"],[3,\"Project3\"],[4,\"Project4\"]]",
	 * HttpStatus.OK) ; }else { //String str = new String("timeout"); JSONObject obj
	 * = new JSONObject(); obj.put("timeout","timeout" ); return new
	 * ResponseEntity<Object>(obj, HttpStatus.OK) ;
	 * 
	 * }
	 * 
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * System.out.println("#### getProjects : projectList "+userId); return null;
	 * 
	 * }
	 */
	//Read excel, csv file and return table structure
	@ResponseBody
	@PostMapping("/uploadFile") public String uploadFile( @RequestParam("file") MultipartFile file,
	  @RequestParam("deliverableTypeName") String deliverableType, @RequestParam("projectname") String projectName,
	  @RequestParam("tabletype") String tabletype,  HttpSession session )
	{
		
		String responseStr = "";
		
		String userId = "";
		
		userId = checkNull((String) session.getAttribute("userId")); // Not Set Yet
		
		System.out.println("#### userId "+userId);
		if(userId.length() > 0) {
			
			try {
				responseStr = createTableService.upload(file, projectName, deliverableType, tabletype, userId, false,0);
			} catch (Exception e) {
				responseStr = e.toString();
				e.printStackTrace();
			}
			
			if(checkNull(responseStr).length() == 0) {
				
				responseStr = "<div class='py-3 text-center' style='color:red'> Please Check Excel File </div>";
			}
		}
		
		
		System.out.println("#### Upload Response ["+responseStr+"]");	
		
		return responseStr;
	}
	
	// Create Custom Table from Excel, Csv files End
	@ResponseBody
	@PostMapping("/saveTable") 
	public String createtable(@RequestParam("file") MultipartFile file,
	  @RequestParam("deliverableTypeName") String deliverableType, @RequestParam("projectname") String projectName,
	  @RequestParam("projectId") int projectId, @RequestParam("tabletype") String tabletype, HttpSession session) 
	{
		//String responseStr = "<div class='py-3 text-center' style='color:red'> Please Check Excel File </div>";
		String responseStr = "";
		String userId = "";
		
		userId = checkNull((String) session.getAttribute("userId")); // Not Set Yet
		
		if(userId.length() >0) {
			
			try {
				responseStr = createTableService.upload(file, projectName, deliverableType,tabletype, userId, true, projectId);
			} catch (Exception e) {
				responseStr = "<span style='color:red'>"+e.getMessage()+"</span>";
				System.out.println("#### Exception :: saveTable : "+e);
			}
		}		
		return responseStr;
		
	}
	
	//Returns Modify Table page
	@RequestMapping("/modifyTable")
	public String modifyTable(Model model){		
		
		try 
		{
			model.addAttribute("deliverableType",deliverabletypeMasterService.getDeliverableIdAndShortName());
			
		} catch (Exception e) {
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}
		
		return "modifyTable";
	}	
	
	@ResponseBody		
	@RequestMapping(value = "/getTableStructure", method = RequestMethod.POST)
	public List<String> getTable(@RequestParam ("tableName") String tableName, Model model) {
		
		System.out.println("#### tableName "+tableName); 
		List<String> columnList = null;
		try 
		{
			tableName = URLDecoder.decode(tableName, "UTF-8");
			columnList = tableModificationService.findAllColumns(tableName);
			
		} catch (UnsupportedEncodingException e) {
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}catch(Exception e) {
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}
		
		System.out.print("#### columnList "+columnList);
		
		return columnList;
	}
	
	@ResponseBody
	@RequestMapping("/alterTable")
	public List<String> getAlterTable(@RequestParam ("tableName") String tableName,
			@RequestParam ("columnName") String colName, Model model){
		
		System.out.println("#### tableName "+tableName);
		System.out.println("#### colName "+colName);
		List<String> columnList = null;
		
		try 
		{
			tableName = URLDecoder.decode(tableName, "UTF-8");
			colName = URLDecoder.decode(colName, "UTF-8");
			columnList = tableModificationService.alterTable(tableName, colName);
			
		} catch (UnsupportedEncodingException e) {
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}catch(Exception e) {
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}
		 
		System.out.print("#### columnList "+columnList);
		
		return columnList;
		
	}
	
		
	@ResponseBody
	@RequestMapping("/getFileName")
	public String getFileName( @RequestParam ("repositoryId") int repositoryId, Model model){
		String fileName = "";
		System.out.println("#### repositoryId "+repositoryId);
		try 
		{			
			//int repositoryId = Integer.valueOf(URLDecoder.decode(repoId, "UTF-8"));
			fileName = repositoryDetailsService.getFileName(repositoryId);
			
		} catch (UnsupportedEncodingException e) {
			
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}catch (Exception e) {
			
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}
		
		System.out.println("#### fileName "+fileName);
		return fileName;
		
	}
	
	@ResponseBody
	@RequestMapping("/updateFileName")
	public String updateFileName(@RequestParam ("fileName") String fileName, @RequestParam ("repositoryId") int repositoryId, Model model, HttpSession session){
		
		System.out.println("#### fileName "+fileName);
		System.out.println("#### repositoryId "+repositoryId);
		
		String userId = "";		
		userId = checkNull((String) session.getAttribute("userId"));
		
		try 
		{
			fileName = URLDecoder.decode(fileName, "UTF-8");
			
			if(repositoryDetailsService.updateFileName(fileName, userId, new Timestamp(new Date().getTime()), repositoryId) > 0) {
				
				return fileName;
			}
			
		} catch (UnsupportedEncodingException e) {
			
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}catch (Exception e) {
			
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}
		
		return "";
		
	}
	/* Rule Management Added Create Rule on 10-08-2021 Start*/
	/*
	 * @RequestMapping("/createRule") public String createRule(Model model){
	 * 
	 * List<String> projectList = fileReaderService.getProjectList();
	 * model.addAttribute("projectList", projectList);
	 * System.out.println("projectList "+projectList);
	 * 
	 * 
	 * return "createRule"; }
	 */
	
	//Gives projects id and name 	
	//https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/chapters/query/native/Native.html
	//@RequestMapping("/createRule")
	//public String createRule(@RequestParam("deliverableTypeId") String deliverableTypeId ,Model model){
		
		//List<Object[]> projectMaster = projectMasterRepo.getProjectIdAndName();
		/*
		 * List<Object[]> projectList = null; try {
		 * 
		 * int Id = Integer.valueOf(URLDecoder.decode(deliverableTypeId, "UTF-8"));
		 * projectList = projectMasterService.getProjectIdAndName(Id);
		 * 
		 * } catch (Exception e) {
		 * 
		 * model.addAttribute("message", e.toString()); }
		 */
		
		//return "ruleMgmnt_createRule";
	//}
	
	//Show Creat Rule Page
	@RequestMapping("/createRule")
	public String createRule(Model model) 
	{		
		try 
		{
			model.addAttribute("deliverableType",deliverabletypeMasterService.getDeliverableIdAndShortName());
			
		} catch (Exception e) 
		{
			
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}
		return "ruleMgmnt_createRule";
	}
	
	//Gives tables as per projectId
	/*
	 * @RequestMapping(value = "/getAllTablesByProjectId", method =
	 * RequestMethod.POST)
	 * 
	 * @ResponseBody public List<String>
	 * getAllTablesByProjectId(@RequestParam("projectId") String Id ) {
	 * System.out.println("#### projectId "+Id); List<String> tableList = null;
	 * 
	 * try { int projectId = Integer.valueOf(Id); tableList =
	 * repoDetailsRepo.getAllTablesByProjectId(projectId); } catch
	 * (NumberFormatException e) {
	 * 
	 * e.printStackTrace();
	 * 
	 * }catch(Exception e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * System.out.print("#### tableList"+tableList);
	 * 
	 * return tableList; }
	 */
	
	  @ResponseBody 
	  @RequestMapping(value = "/getAllTablesByProjectId", method = RequestMethod.POST)	
	  public String getAllTablesByProjectId(@RequestParam("projectId") int projectId,Model model) { 
		  System.out.println("#### projectId "+projectId);
		  String response = ""; 
		  try 
		  { 
			  	response = repositoryDetailsService.getAllTablesByProjectId(projectId);
			  	
	  	  }catch(Exception e) {
		  		model.addAttribute("message", e.toString());
		  		e.printStackTrace(); 
		  }
	  
		  System.out.print("#### getAllTablesByProjectId "+response);
		  return response; 
	  }
	 	
	
	/*
	 * @RequestMapping(value = "/getAllTablesByProjectId", method =
	 * RequestMethod.POST)
	 * 
	 * @ResponseBody public ResponseEntity<String>
	 * getAllTablesByProjectId(@RequestParam("projectId") int projectId,Model model,
	 * HttpSession session ) { ResponseEntity<String> responseEntity = null; String
	 * userId = checkNull((String)session.getAttribute("userId"));
	 * 
	 * try { if(userId.length() > 0) { responseEntity = new
	 * ResponseEntity<String>(repositoryDetailsService.getAllTablesByProjectId(
	 * projectId),HttpStatus.OK); }else { JSONObject obj = new JSONObject();
	 * obj.put("timeout","timeout" ); responseEntity = new
	 * ResponseEntity<String>(obj.toString(),HttpStatus.OK); }
	 * 
	 * }catch(Exception e) { model.addAttribute("message", e.toString());
	 * e.printStackTrace(); } return responseEntity; }
	 */
	
	@ResponseBody
	@RequestMapping("/saveRule")
	public String saveRule(@RequestParam ("projectId") int projectId, @RequestParam ("repositoryId") int repositoryId, 
			@RequestParam ("shortDesc") String shortDesc, @RequestParam ("ruleType") String ruleType, HttpSession session)
	{
		System.out.println("#### projectId "+projectId);
		System.out.println("#### shortDesc "+shortDesc);
		
		String response = "";
		String userId = "";
		
		userId = checkNull((String) session.getAttribute("userId"));
		try 
		{
			
			String query = "";
			if(userId.length() > 0) {
				query = URLDecoder.decode(shortDesc, "UTF-8");
				ruleType = URLDecoder.decode(ruleType, "UTF-8");
				response = ruleMasterService.save(projectId, repositoryId, query, ruleType, userId);
			}
			
			
		} catch (UnsupportedEncodingException e) {
			
			return e.toString();
			
		}catch(Exception e) {
			
			return e.toString();
		}
		return response;
	}
	
	// Rule Management Create Rule Added on 10-08-2021 End
	
	//Rule Management Added Execute Rule on 10-08-2021 Start
	@RequestMapping("/executeRule")
	public String executeRule(Model model){
		
		try {
			model.addAttribute("deliverableType",deliverabletypeMasterService.getDeliverableIdAndShortName());
			//model.addAttribute("projectList",projectMasterService.getProjectIdAndName());
			
		} catch (Exception e) {			

			model.addAttribute("message", e.toString());
			e.printStackTrace();
		} 
		
		
		return "ruleMgmnt_executeRule";
	}
	//Rule Management Added Execute Rule on 10-08-2021 End
	
	//Rule Management Added View  Rule on 10-08-2021 Start
	//Show View Rule Page
	@RequestMapping("/viewRulePanel")
	public String viewRule(Model model){		
		
		try 
		{
			model.addAttribute("deliverableType",deliverabletypeMasterService.getDeliverableIdAndShortName());
			
		} catch (NullPointerException e) {
			System.out.println("#### NullPointerException");
			model.addAttribute("message", e.toString());
			
		}catch (Exception e) {
			
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}  
		
		return "ruleMgmnt_viewModifyRule";
	}
	
	/*
	@ResponseBody
	@RequestMapping("/getRules")
	public List<Object[]> getRules(@RequestParam("projectId") String projectName, Model model){
		List<Object[]> ruleList = null;
		try 
		{
			int projectId = Integer.valueOf(URLDecoder.decode(projectName, "UTF-8"));
			ruleList = ruleMasterService.getRuleById(projectId);
			
		} catch (Exception e) 
		{
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}
		return ruleList;
		
	}
	*/
	@ResponseBody
	@RequestMapping("/getRules")
	public String getRules(@RequestParam("projectId") int projectId, Model model){
		String response = "";
		try 
		{			
			response = ruleMasterService.getJSONRuleById(projectId, true);
			
		} catch (Exception e) 
		{
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}
		System.out.println("###### Json Response "+response);
		return response;
		
	}
	
	@ResponseBody
	@RequestMapping("/getTableNameFromRepositoryId")
	public String getTableNameFromRepositoryId(@RequestParam("repositoryId") int repositoryId, Model model){
		String response = "";
	
		try 
		{			
			response = repositoryDetailsService.getTableNameFromRepositoryId(repositoryId);
			
		} catch (Exception e) 
		{
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}
		System.out.println("#### Table Name : "+response);
		return response;
		
	}
	
	@ResponseBody
	@RequestMapping("/updateRuleDesc")
	public String updateRuleDesc(@RequestParam ("ruleId") int ruleId, @RequestParam ("shortDesc") String shortDesc,HttpSession session)
	{
		System.out.println("#### ruleId "+ruleId);
		System.out.println("#### shortDesc "+shortDesc);
		
		String response = "";
		String userId = "";		
		userId = checkNull((String) session.getAttribute("userId"));
		
		try 
		{
			
			String query = "";
			if(userId.length() > 0) {
				query = URLDecoder.decode(shortDesc, "UTF-8");
				response = ruleMasterService.updateRuleDesc(ruleId, query, userId);
			}
			
			
		} catch (UnsupportedEncodingException e) {
			
			return e.toString();
			
		}catch(Exception e) {
			
			return e.toString();
		}
		return response;
	}
	@ResponseBody
	@RequestMapping("/updateRuleStatus")
	public String updateRuleStatus(@RequestParam ("ruleId") int ruleId, @RequestParam ("status") String status,HttpSession session)
	{
		System.out.println("#### ruleId "+ruleId);
		System.out.println("#### status "+status);
		
		String response = "";
		String userId = "";
		
		userId = checkNull((String) session.getAttribute("userId"));
		try 
		{			
			String query = "";
			if(userId.length() > 0) {
				query = URLDecoder.decode(status, "UTF-8");
				response = ruleMasterService.updateRuleStatus(ruleId, status, userId);
			}
			
			
		} catch (UnsupportedEncodingException e) {
			
			return e.toString();
			
		}catch(Exception e) {
			
			return e.toString();
		}
		return response;
	}
	//Rule Management Added View Rule on 10-08-2021 End
	
	//Rule Management Execute Rule on 24-08-2021 START
	
	@ResponseBody
	@RequestMapping("/getrulesforexecute")
	public String getRulesforExecute(@RequestParam("projectId") int projectId, Model model){
		String response = "";
		try 
		{			
			response = ruleMasterService.getJSONRuleById(projectId,false);
			
		} catch (Exception e) 
		{
			model.addAttribute("message", e.toString());
			e.printStackTrace();
		}
		System.out.println("###### Json Response "+response);
		return response;
		
	}
	
	@ResponseBody
	@RequestMapping("/ruleExecution")
	public String ruleExecution(@RequestParam ("ruleArray") String ruleArray) {
	
		String response = "";
		String ruleIdList = "";
		try {
			ruleIdList = URLDecoder.decode(ruleArray, "UTF-8");
			System.out.println("#### ruleArray "+ruleIdList);
			response = ruleMasterService.executeRules(ruleIdList);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
		
	}
	//Rule Management Added View Rule on 24-08-2021 END
	
	//Mapping Management User Project Mapping on 08-09-2021 START
	
	@RequestMapping("/viewUserProjectMappingPage")
	public ModelAndView viewUserProjectMappingPage(ModelAndView modelAndView, HttpSession session) 
	{	
		System.out.println("#### userProject ");
		String userId = checkNull((String)session.getAttribute("userId"));
		try 
		{
			if(userId.length() > 0) {
				modelAndView.addObject("deliverableType",deliverabletypeMasterService.getDeliverableIdAndShortName());
				modelAndView.setViewName("mappingMgmnt_userProject");
			}else {				
				modelAndView.setViewName("logout");
			}
			
			
		} catch (Exception e) 
		{			
			modelAndView.addObject("message", e.toString());
			e.printStackTrace();
		}
		return modelAndView;
	}
	
	@RequestMapping("/viewMasterDeliverableMappingPage")
	public ModelAndView viewMasterDeliverableMappingPage(ModelAndView modelAndView, HttpSession session) 
	{		
		System.out.println("#### masterDeliverable ");
		String userId = checkNull((String)session.getAttribute("userId"));
		try 
		{
			
			if(userId.length() > 0) {
				modelAndView.addObject("deliverableType",deliverabletypeMasterService.getDeliverableIdAndShortName());
				modelAndView.setViewName("mappingMgmnt_addMasterDeliverableMapping");
			}else {
				modelAndView.setViewName("logout");
			}
			
		} catch (Exception e) 
		{			
			modelAndView.addObject("message", e.toString());
			e.printStackTrace();
		}
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping("/isMappingPresent")
	public boolean isMappingPresent(@RequestParam("projectId") int projectId, @RequestParam("masterTable") String masterTable, @RequestParam("masterField") String masterField,
			@RequestParam("deliverableTable") String deliverableTable,@RequestParam("deliverableField") String deliverableField,Model model,HttpSession session) 
	{	
		System.out.println("#### isMappingPresent "+deliverableTable);
		boolean response = false;
		String userId = checkNull((String)session.getAttribute("userId"));
		try 
		{			
			if(userId.length() > 0) {
				response = masterDeliverableMappingService.isMappingPresent(projectId,URLDecoder.decode(masterTable, "UTF-8"),URLDecoder.decode(masterField, "UTF-8"),
						URLDecoder.decode(deliverableTable, "UTF-8"),URLDecoder.decode(deliverableField, "UTF-8"));
			}else {
				
			}
			
		} catch (Exception e) 
		{			
			
			System.out.println("#### Exception :: saveMasterDeliverableMapping : "+response);			
			e.printStackTrace();
		}
		System.out.println("#### isMappingPresent "+response);
		return response;
	}
	
	@ResponseBody
	@RequestMapping("/saveMasterDeliverableMapping")
	public String saveMasterDeliverableMapping(@RequestParam("projectId") int projectId, @RequestParam("masterTable") String masterTable, @RequestParam("masterField") String masterField,
			@RequestParam("deliverableTable") String deliverableTable,@RequestParam("deliverableField") String deliverableField,
			Model model,HttpSession session) 
	{	
		String response = "";
		String userId = checkNull((String)session.getAttribute("userId"));
		try 
		{			
			if(userId.length() > 0) {
				response = masterDeliverableMappingService.saveMapping(projectId,URLDecoder.decode(masterTable, "UTF-8"),URLDecoder.decode(masterField, "UTF-8"),
						URLDecoder.decode(deliverableTable, "UTF-8"),URLDecoder.decode(deliverableField, "UTF-8"),userId);
			}else {
				//("timeout");
			}
			
		} catch (Exception e) 
		{			
			
			System.out.println("#### Exception :: saveMasterDeliverableMapping : "+response);
			response = e.getMessage();
			e.printStackTrace();
		}
		System.out.println("#### response "+response);
		return response;
	}
	@RequestMapping("/viewMappedMasterDeliverablePage")
	public ModelAndView viewMappedMasterDeliverablePage(ModelAndView modelAndView, HttpSession session) 
	{		
		System.out.println("#### masterDeliverable ");
		String userId = checkNull((String)session.getAttribute("userId"));
		try 
		{
			//model.addAttribute("users",authentication.getADUsers());
			if(userId.length() > 0) {
				modelAndView.addObject("deliverableType",deliverabletypeMasterService.getDeliverableIdAndShortName());
				modelAndView.setViewName("mappingMgmnt_viewMasterDeliverableMapping");
			}else {
				modelAndView.setViewName("logout");
			}
			
		} catch (Exception e) 
		{
			
			modelAndView.addObject("message", e.toString());
			e.printStackTrace();
		}
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping("/viewMappedMasterDeliverableData")
	public String viewMappedMasterDeliverableData(@RequestParam("projectId") int projectId, Model model,HttpSession session) 
	{	
		String response = "";
		String userId = checkNull((String)session.getAttribute("userId"));
		try 
		{			
			if(userId.length() > 0) {
				response = masterDeliverableMappingService.getMasterDeliverableMapping(projectId);
			}else {
				//("timeout");
				return "redirect:timeout";
			}
			
		} catch (Exception e) 
		{			
			
			System.out.println("#### Exception :: viewMappedMasterDeliverableData : "+e);
			response = e.getMessage();
			e.printStackTrace();
		}
		System.out.println("#### response "+response);
		return response;
	}
	
	@ResponseBody
	@RequestMapping("/deactiveMasterDeliverableMapping")
	public String deactiveMasterDeliverableMapping(@RequestParam ("mappingIdArray") String mappingIdArray,Model model,HttpSession session) {
	
		String response = ""; String mappingIdList = "";
		String userId = checkNull((String)session.getAttribute("userId"));
		try {			
				mappingIdList = URLDecoder.decode(mappingIdArray, "UTF-8");
				System.out.println("#### mappingIdList "+mappingIdList);
				response = masterDeliverableMappingService.deactiveMasterDeliverableMapping(mappingIdList, userId);
			
		} catch (Exception e) {
			System.out.println("#### Exception :: deactiveMasterDeliverableMapping : "+e);
			response =  e.getMessage();
			e.printStackTrace();
		}
		
		return response;
		
	}
	/*
	@ResponseBody
	@RequestMapping("/getUserProjectMapping")
	public String getMappedProjects(@RequestParam("mappedUser") int mappedUser, @RequestParam("deliverableTypeId") int deliverableTypeId){
		String response = "";
		try 
		{				
			response = "";
			
		} catch (Exception e) 
		{			
			e.printStackTrace();
		}
		System.out.println("###### Json Response "+response);
		return response;
		
	}
	*/
	//Mapping Management User Project Mapping on 08-09-2021 END
	
	private String checkNull(String input)
    {
	    if(input == null || "null".equalsIgnoreCase(input) || "undefined".equalsIgnoreCase(input)) {
	    	
	    	input = "";
	    }
        
        return input.trim();    
    }
	
}
