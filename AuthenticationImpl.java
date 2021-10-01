package com.tkis.qedbot.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkis.qedbot.FileEncryption;
import com.tkis.qedbot.ad.ADGroupWiseAuthentication;
import com.tkis.qedbot.entity.ADServiceMaster;
import com.tkis.qedbot.repo.ADServiceMasterRepo;

@Service
public class AuthenticationImpl implements Authentication
{
	
	  @Autowired
	  private ADGroupWiseAuthentication groupWiseAuthentication;	
	
	  @Autowired 
	  private ADServiceMasterRepo adServiceMasterRepo;
	  
	  @Autowired
	  private FileEncryption encdec;
	  
	  @Override 
	  public List<ADServiceMaster> getADServiceDetails() throws Exception
	  {	  
		  return adServiceMasterRepo.findAll();	  
	  }
	 
	
	@Override
	public String validateUser(String userId, String password, String domain, HttpSession session) throws Exception {
		
		String response = "", providerUrl = "", ldapUrl = "", serviceUserName = "", servicePassword = "";		
		
		if(checkNull(domain).equalsIgnoreCase("STMUDC02")) {
			
			providerUrl = "ldap://stmudc02.in.uhde.org";
			
		}else {
			
			providerUrl = "ldap://stmudc15.ext.in.uhde.org";
		}
		
		//Iterable<ADServiceMaster> allADServiceMasterItr = adServiceMasterService.getADServiceDetails();
		
		Iterable<ADServiceMaster> allADServiceMasterItr = getADServiceDetails();
		
		Iterator<ADServiceMaster> adServiceMasItr = allADServiceMasterItr.iterator();
		
		//ADServiceMaster contains only one record
		if(adServiceMasItr.hasNext()) {
			
			ADServiceMaster adServiceMaster = adServiceMasItr.next();
			serviceUserName = adServiceMaster.getUserId();
			servicePassword = encdec.Decrypt(adServiceMaster.getPassword());
			ldapUrl = adServiceMaster.getLdapUrl();
		}
		
		
		/*
		 * System.out.println("#### serviceUserName "+serviceUserName);
		 * System.out.println("#### servicePassword "+servicePassword);
		 * System.out.println("#### ldapUrl "+ldapUrl);
		 */
		 
		
		String serviceSecurityPrincipal = "CN=" + serviceUserName + ",CN=Managed Service Accounts,DC=in,DC=uhde,DC=org";
		
		groupWiseAuthentication.makeConnection(ldapUrl,serviceSecurityPrincipal, servicePassword);
		String userInfo = groupWiseAuthentication.findGroup(userId);		
		groupWiseAuthentication.closeConnection();
		
		if(checkNull(userInfo).length() > 0) {			
			
			String userInfoArray [] =  userInfo.split("~");
			String userRole = userInfoArray[0];
			String securityPrincipal = userInfoArray[1];
			
			System.out.println("#### userRole "+userRole);
			System.out.println("#### securityPrincipal "+securityPrincipal);
			
			groupWiseAuthentication.makeConnection(providerUrl,securityPrincipal, password);
			
			if(groupWiseAuthentication.validateUserDetails(securityPrincipal,userId, password, session)) {
				
				session.setAttribute("userId",userId);
				session.setAttribute("role",userRole);
				
				response = userRole;
			}
			
			groupWiseAuthentication.closeConnection();
			
		}else {
			response = "No Group";
		}
		
		return response;
	}
	
	public String checkNull(String input)
    {
		System.out.println("#### Input String ["+input+"]");
        if(input == null || "null".equalsIgnoreCase(input) || "undefined".equalsIgnoreCase(input))
        input = "";
        return input.trim();    
    }


	/*
	 * @Override public HashMap<String,String> getADUsers() throws Exception {
	 * HashMap<String,String> userHashMap = new HashMap<>();
	 * Iterable<ADServiceMaster> allADServiceMasterItr = getADServiceDetails();
	 * Iterator<ADServiceMaster> adServiceMasItr = allADServiceMasterItr.iterator();
	 * 
	 * String serviceUserName = "", servicePassword = "",ldapUrl = "";
	 * //ADServiceMaster contains only one record if(adServiceMasItr.hasNext()) {
	 * 
	 * ADServiceMaster adServiceMaster = adServiceMasItr.next(); serviceUserName =
	 * adServiceMaster.getUserId(); servicePassword =
	 * encdec.Decrypt(adServiceMaster.getPassword()); ldapUrl =
	 * adServiceMaster.getLdapUrl(); }
	 * 
	 * 
	 * String serviceSecurityPrincipal = "CN=" + serviceUserName +
	 * ",CN=Managed Service Accounts,DC=in,DC=uhde,DC=org";
	 * 
	 * groupWiseAuthentication.makeConnection(ldapUrl,serviceSecurityPrincipal,
	 * servicePassword); userHashMap = groupWiseAuthentication.getADUsers();
	 * groupWiseAuthentication.closeConnection();
	 * 
	 * 
	 * return userHashMap; }
	 */

}
