<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%
	
	response.setHeader("pragma","no-cache");//HTTP 1.1
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Cache-Control","no-store");
	response.addDateHeader("Expires", -1);
	response.setDateHeader("max-age", 0);
	response.setIntHeader ("Expires", -1); //prevents caching at the proxy server
	response.addHeader("cache-Control", "private");
	
	String userId = checkNull((String)session.getAttribute("userId"));
	String role = checkNull((String)session.getAttribute("role"));
	String userName = (String)session.getAttribute("DisplayName");
	
	if(userId == null || userId.equals("-1") || userId.equals("")) 
	{  
	     response.sendRedirect("logout");
	     return;
	}
%>
<!DOCTYPE HTML>
<html lang="en-US">
<head>
<title>thyssenkrupp Industrial Solutions India Pvt Ltd</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no"/>
<link href="css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="vendor/hover/effects.min.css" rel="stylesheet">
<script src="js/jquery.js"></script>
<script src="js/common.js"></script>
<script src="js/bootstrap.js"></script>
<style>
.header {
	position: sticky;
	top:0;
}
body {
	height: 800px;
}

.colors {
	display:none;
}
</style>
<script src="js/clientValidation.js"></script>
<script src="js/serverValidation.js"></script>

</head>

<body>

<%@page import="java.util.List" %>

<!--header start-->
<div class="header-top">
  <div class="container">
    <div class="row">
      <div class="col-md-6 col-sm-6 col-6 centerdiv">
        <div class="logo"> <img src="images/thyssenkrupp-logo.jpg"  class="img-responsive logoimg"> </div>
      </div>
      <div class="col-md-6 col-sm-6 col-6  heading">QED BOT</div>
    </div>
    <!--header end-->
  </div>
</div>

<div class="container-fluid p-0">
	<div id="load_menu"></div>
</div>

<div class="container-fluid px-4">
  <div class="row" style="font-size:16px; padding:5px 2px;">
    <div class="col-md-12 col-sm-12" >
      <div class="login_user">Welcome <span class="user-name"><%=userName %></span>. You  are logged in as <span  class="user-name"><%=role %></span> </div>
    </div>
  </div>
  <div class="row"><div class="col-md-12">
  <% 
      			List<Object[]> keyFieldList = (List<Object[]>) request.getAttribute("keyField");
     		 %>
 	<label>Select Key Field</label>
  <form  style="display:inline-block" onSubmit="javascript: return false;"> 
  	<select name='dropDown' id='dropDown'  class="form-select" style="width:250px;margin:0 10px 10px 10px;display:inline-block">
  			<option value="" selected="selected" >--Select Key Field--</option>
					<% for(Object[] kfRow : keyFieldList){%>
						
						<option value="<%= (Number) kfRow[0]%>"><%= (String) kfRow[1]%></option>
						
					<%} %>
            <!-- <option value="KeyField1">PROJECT-V000-XX00-AB021</option>
            <option value="KeyField2">PROJECT-V000-XX00-AF004	</option>
            <option value="KeyField3">PROJECT-V000-XX00-AL001	</option> -->
        </select>
        
        <input id="goBttn" type="button" class="btn btn-primary done" value="Submit">
  </form>
  </div>
  </div>
  <div class="content-area" style="padding:10px 20px 10px 20px">
  <div id="KeyField1" class="KeyField1"> <h2>Inconsistency for Project: <span>Project_001	</span> </h2>
  <div id="rec-report-table"><div class="content-inner">
      <table width="100%" border="0" align="left" cellpadding="0" cellspacing="0" class="table tbl-report table-bordered table-striped">
        <thead style="position: sticky;top: 0" class="thead-dark">
          <tr>
            <th style="width:50px;" align="center" valign="middle" class="table-heading header">Sr.No</th>
            <!-- <th align="center" valign="middle" class="table-heading header">Mapping Id</th> -->
            <th align="center" valign="middle" class="table-heading header">Key Field</th>
            <th  align="center" valign="middle" class="table-heading header">Field Name</th>
            <th  align="center" valign="middle" class="table-heading header">Master Data</th>
            <th  align="center" valign="middle" class="table-heading header">Deliverable Data</th>
            <th  align="center" valign="middle" class="table-heading header">Set flag as</th>
            <th width=15% align="center" valign="middle" class="table-heading header">Remarks</th>
            <th  align="center" valign="middle" class="table-heading header">Action</th>
          </tr>
        </thead>
        
        <% 
        String jsonResponse = (String) request.getAttribute("projectdata");
        
        JSONParser jsonParse = new JSONParser();
        
        if(checkNull(jsonResponse).length() > 0){
        	
        	JSONObject jsonObject = (JSONObject) jsonParse.parse(jsonResponse);
        	JSONArray jsonArray = (JSONArray) jsonObject.get("MAPPING_DATA");
        	System.out.println("***** Data Size "+jsonArray.size());
        	
        	int srNo = 1;
        	for(int i = 0; i<jsonArray.size(); i++){
        		
        		srNo = srNo+i;
        		JSONObject dataObject = (JSONObject) jsonArray.get(i);
        		long mappingId = 0; 
        		String keyField = "", deliverableField = "", masterData = "", deliverableData =""; 
        		try{
        			
        			mappingId = (Long) dataObject.get("MAPPING_ID");
        			//System.out.println("***** mappingId "+mappingId);
        			keyField = (String) dataObject.get("KEY_FIELD");
            		deliverableField = (String) dataObject.get("DELIVERABLE_NAME");
            		masterData = (String) dataObject.get("MASTER_DATA");
            		deliverableData = (String) dataObject.get("DELIVERABLE_DATA");
            		//System.out.println("***** masterData "+masterData+" "+deliverableData); 		
        		}catch(Exception e){
        			e.printStackTrace();
        		}
        		
        		%>
     <tr><tr valign="top">
              <td class="text-center"><%=srNo %><input type='hidden' id='mappingId_<%=srNo %>' value="<%=mappingId %>"/></td>              
              <%-- <td id='mappingId_<%=srNo %>'><%=mappingId %></td> --%>
              <td id='keyField_<%=srNo %>'><%=keyField %></td>
	          <td id='deliverableField_<%=srNo %>'><%=deliverableField %></td>
	          <td id='masterData_<%=srNo %>'><%=masterData %></td>
	          <td id='deliverableData_<%=srNo %>'><%=deliverableData %></td>
	          
          <td><select class="form-select select-action"  id='consistencyFlag_<%=srNo %>' name='consistencyFlag_<%=srNo %>'>
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold</option>
            </select></td>
            
          <td><textarea class="form-control" id='consistencyRemark_<%=srNo %>'></textarea></td>
          <td>
            <input id='submitBtn_<%=srNo %>' type="button" class="btn btn-primary done" value="Submit" onClick="saveConsistency('<%=srNo %>','<%=userId%>')">
          </td>
        </tr>
        	<% 
        	//System.out.println("***** ROW "+i);
        	}
        }else{%>
        	<tr><tr valign="top">
            <td colsapn="8" class="text-center">No Data Found</td>
        <% }%>
        
        
        <!-- <tr>
        <tr valign="top">
          <td class="text-center">1</td>
          <td>PROJECT-V000-XX00-AB021</td>
          <td>Size </td>
          <td>150 mm </td>
          <td>150 </td>
          
          <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">2</td>
          <td>PROJECT-V000-XX00-AB021</td>
          <td>Process Unit</td>
          <td>V000</td>
          <td>V090</td>
          <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">3</td>
          <td>PROJECT-V000-XX00-AB021</td>
          <td>Speciality Tag </td>
          <td> </td>
          <td>SPAB074</td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">4</td>
          <td>PROJECT-V000-XX00-AB021</td>
          <td>Reporting requirement</td>
          <td> </td>
          <td>To be reported </td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">5</td>
          <td>PROJECT-V010-AX10-AF004</td>
          <td>Size</td>
          <td>250 mm</td>
          <td>250</td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">6</td>
          <td>PROJECT-V010-AX10-AF004</td>
          <td>Reporting requirement</td>
          <td></td>
          <td>To be reported </td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">7</td>
          <td>PROJECT-V010-AX10-AL001</td>
          <td>Piping Spec</td>
          <td></td>
          <td>10CA01B1RA102</td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr> -->
      </table>
    </div></div></div>

    <div id="KeyField2" class="colors KeyField2"> <h2>Inconsistency for Project: <span>PROJECT_002	</span> </h2>
  <div id="rec-report-table"><div class="content-inner">
      <table width="100%" border="0" align="left" cellpadding="0" cellspacing="0" class="table tbl-report table-bordered table-striped">
        <thead style="position: sticky;top: 0" class="thead-dark">
          <tr>
            <th style="width:50px;" align="center" valign="middle" class="table-heading header">Sr.No</th>
            <th align="center" valign="middle" class="table-heading header">Key Field</th>
            <th  align="center" valign="middle" class="table-heading header">Field Name</th>
            <th  align="center" valign="middle" class="table-heading header">Master Data</th>
            <th  align="center" valign="middle" class="table-heading header">Deliverable Data</th>
            <th  align="center" valign="middle" class="table-heading header">Set flag as</th>
            <th width=15% align="center" valign="middle" class="table-heading header">Remarks</th>
            <th  align="center" valign="middle" class="table-heading header">Action</th>
          </tr>
        </thead>
        <tr>
        <tr valign="top">
          <td class="text-center">1</td>
          <td>PROJECT-V000-XX00-AB021</td>
          <td>Size </td>
          <td>150 mm </td>
          <td>150 </td>
          
          <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">2</td>
          <td>PROJECT-V000-XX00-AB021</td>
          <td>Process Unit</td>
          <td>V000</td>
          <td>V090</td>
          <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">3</td>
          <td>PROJECT-V000-XX00-AB021</td>
          <td>Speciality Tag </td>
          <td> </td>
          <td>SPAB074</td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">4</td>
          <td>PROJECT-V000-XX00-AB021</td>
          <td>Reporting requirement</td>
          <td> </td>
          <td>To be reported </td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">5</td>
          <td>PROJECT-V010-AX10-AF004</td>
          <td>Size</td>
          <td>250 mm</td>
          <td>250</td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">6</td>
          <td>PROJECT-V010-AX10-AF004</td>
          <td>Reporting requirement</td>
          <td></td>
          <td>To be reported </td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">7</td>
          <td>PROJECT-V010-AX10-AL001</td>
          <td>Piping Spec</td>
          <td></td>
          <td>10CA01B1RA102</td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
      </table>
    </div></div></div>
    <div id="KeyField3" class="colors KeyField3"> <h2>Inconsistency for Project: <span>PROJECT_003	</span> </h2>
  <div id="rec-report-table"><div class="content-inner">
      <table width="100%" border="0" align="left" cellpadding="0" cellspacing="0" class="table tbl-report table-bordered table-striped">
        <thead style="position: sticky;top: 0" class="thead-dark">
          <tr>
            <th style="width:50px;" align="center" valign="middle" class="table-heading header">Sr.No</th>
            <th align="center" valign="middle" class="table-heading header">Key Field</th>
            <th  align="center" valign="middle" class="table-heading header">Field Name</th>
            <th  align="center" valign="middle" class="table-heading header">Master Data</th>
            <th  align="center" valign="middle" class="table-heading header">Deliverable Data</th>
            <th  align="center" valign="middle" class="table-heading header">Set flag as</th>
            <th width=15% align="center" valign="middle" class="table-heading header">Remarks</th>
            <th  align="center" valign="middle" class="table-heading header">Action</th>
          </tr>
        </thead>
        <tr>
        <tr valign="top">
          <td class="text-center">1</td>
          <td>PROJECT-V000-XX00-AB021</td>
          <td>Size </td>
          <td>150 mm </td>
          <td>150 </td>
          
          <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">2</td>
          <td>PROJECT-V000-XX00-AB021</td>
          <td>Process Unit</td>
          <td>V000</td>
          <td>V090</td>
          <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">3</td>
          <td>PROJECT-V000-XX00-AB021</td>
          <td>Speciality Tag </td>
          <td> </td>
          <td>SPAB074</td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">4</td>
          <td>PROJECT-V000-XX00-AB021</td>
          <td>Reporting requirement</td>
          <td> </td>
          <td>To be reported </td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">5</td>
          <td>PROJECT-V010-AX10-AF004</td>
          <td>Size</td>
          <td>250 mm</td>
          <td>250</td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">6</td>
          <td>PROJECT-V010-AX10-AF004</td>
          <td>Reporting requirement</td>
          <td></td>
          <td>To be reported </td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
        <tr valign="top">
          <td class="text-center">7</td>
          <td>PROJECT-V010-AX10-AL001</td>
          <td>Piping Spec</td>
          <td></td>
          <td>10CA01B1RA102</td> <td><select class="form-select select-action"  id='approveRejectDD1' name='approveRejectDD1' onChange="showHideRemark('1')">
              <option value="" selected="selected" >Select Flag</option>
              <option value="Mark as Alias">Mark as Alias </option>
              <option value="Ignore by Rule">Ignore by Rule</option>
              <option value="Ignore Manually">Ignore Manually</option>
              <option value="On Hold">On Hold </option>
            </select></td>
            <td><textarea class="form-control"></textarea></td>
          <td>
            <input  type="button" class="btn btn-primary done" value="Submit" onClick="approved('20201214006','','seller','1')">
          </td>
        </tr>
      </table>
    </div></div></div>
  
  
  </div>
</div>
<div class="container-fluid">
  <div class="row">
    <div class="footer"> &copy  thyssenkrupp Industrial Solutions India Pvt Ltd </div>
  </div>
</div>

<script>
		        
		        $(document).ready(function () {
		            $.ajax({
		                url: "menu/usermenu.html", success: function (result) {

		                    $("#load_menu").html(result);

		                }
		            });
		        });			
				
				
	    </script>


 <script>
$(function () {
    $("#goBttn").click(function () {
        $("#dropDown").find("option").each(function () {
            var div_id = $(this).val();
            $("." + div_id).each(function () {
                $(this).hide();
            });
        });
        $("." + $("#dropDown").val()).each(function () {
            $(this).show();
        });
    });
});

  </script>
</body>
</html>
<%!
	public String checkNull(String input)
	{
	    if(input == null || "null".equalsIgnoreCase(input) || "undefined".equalsIgnoreCase(input)) {
	    	
	    	input = "";
	    }
	    
	    return input.trim();    
	}
%>