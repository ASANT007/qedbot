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
	String userName = checkNull((String)session.getAttribute("DisplayName"));
	
	String message = checkNull((String) request.getAttribute("message"));
	
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
.result-left {
	border-top: 1px solid #ccc;
	border-bottom: 1px solid #ccc;
	border-left: 5px solid #00a0f0;
	margin-left: 1px solid #ccc;
	margin-left: 0px;
	background:  #f7fafd;
}
.result-btn {
	border-top: 1px solid #ccc;
	border-right: 1px solid #ccc;
	border-bottom: 1px solid #ccc;
	background: #f7fafd;
	vertical-align: middle;
}
#result {
	text-align: left;
	font-size: 13px;
	padding: 11px 0px;
}
.save-rule {
	background: #dae9ff;
	color: #0d6efd;
	border: none;
	font-weight: bold;
	margin: 22px;
}
.alert {
	padding: 5px;
	font-size: 13px;
	display: inline-block;
	width: 34%;
	margin-bottom: 5px;
}
</style>
<script src="js/clientValidation.js"></script>
<script src="js/serverValidation.js"></script>
<script>
$(document).ready(function () {
  var trigger = $('.hamburger'),
      overlay = $('.overlay'),
     isClosed = false;

    trigger.click(function () {
      hamburger_cross();      
    });

    function hamburger_cross() {

      if (isClosed == true) {          
        overlay.hide();
        trigger.removeClass('is-open');
        trigger.addClass('is-closed');
        isClosed = false;
      } else {   
        overlay.show();
        trigger.removeClass('is-closed');
        trigger.addClass('is-open');
        isClosed = true;
      }
  }
  
  $('[data-toggle="offcanvas"]').click(function () {
        $('#wrapper').toggleClass('toggled');
  });  
});
</script>

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

<div class="container-fluid p-0"><div id="load_menu"></div></div>

<div class="container px-4">
  <div class="row" style="padding:5px 2px;">
    <div class="col-md-12 col-sm-12" >
      <div class="login_user">Welcome <span class="user-name"><%=userName %></span>. You  are logged in as <span  class="user-name"><%=role %></span> </div>
    </div>
  </div>
  <h2>Execute Rule</h2>  
  <div class="content-area">
  <!-- START -->
  <%if(message.length() > 0) {%>
  <div class="row">
	  <div class="row text-center">
        <div id="message" name="message"  class="alert alert-danger mx-auto mt-3" role="alert"><%=message %></div>
      </div>
  </div>
  <% }else{%>
  <form id="executeRulesFrm" name="executeRulesFrm">
  
  <div class="row">
	  
	  <div class="col-md-4 select-delivereable-type-createtable align-self-center">
      		<% 
      			List<Object[]> deliverabletype = (List<Object[]>) request.getAttribute("deliverableType");
     		 %>
     		 
			<label>Select Deliverable Type : </label>
			<sup class="mandatory">*</sup><div class="select-deliverable-type">
			<select class="form-select" name="deliverableType" id="deliverableType" onchange="getProjects(this)">
					<option value="" selected="selected" >--Select Deliverable--</option>
					<% for(Object[] dt : deliverabletype){%>
						
						<option value="<%= (Number) dt[0]%>"><%= (String) dt[1]%></option>
						
					<%} %>
			</select>
	 		</div>
	  </div>
		  
	  <div class="col-md-3 align-self-center">    
		<label>Select Project : </label> 
		<sup class="mandatory">*</sup><div class="select-project-003">		
		<select class="form-select tbl-select-opt" name="projectName" id="projectName" onchange="getTablesForSelectedProject(this)">
				<option value="" >--Select Project--</option>		
		</select>
 	 </div> 
 	 </div>
 	 
 	 <div class="col-md-4  align-self-center">
          <input class="btn btn-primary " type = "button" value = "View Rules" onClick="viewRulesForExecution()"/>
          <a href="executeRule">
          <input class="btn btn-primary" type = "button" value = "Cancel" />
          </a>
		</div>		
    
    </div>  
      			
	<div class="row text-center my-3">
        <div id="errorDiv" name="errorDiv"  class="alert alert-danger mx-auto" role="alert" style="display:none"></div>
      </div>
      
      <div class="row text-center my-3">
        <div id="successDiv" name="successDiv"  class="alert alert-success mx-auto" role="alert" style="display:none"></div>
      </div>	
      
      <div class="row" >
        <div class="col-md-12 mb-3" id="executeRuleDiv" name="executeRuleDiv" style=" text-align: right; display:none;">
        	<input class="btn btn-primary " type = "button" value = "Execute Rules" onClick="executeRules()"/>        
        </div>
      </div>
      
      <div class="row text-center">
        <div id="ruleListDiv" name="ruleListDiv"></div>
      </div>    
      
            
      <!-- Below field are used on modify rules START -->
      <input type="hidden" id="ruleId" name="ruleId"/>
      <input type="hidden" id="tableName" name="tableName"/>
      <input type="hidden" id="action" name="action"/>
      <input type="hidden" id="targetFieldNameHidden" name="targetFieldNameHidden"/>
      <!-- END -->
      </form>
      
      
      
     <% }%> 
      
   	
    
  <!-- END -->
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
                url: "menu/menu.html", success: function (result) {

                    $("#load_menu").html(result);

                }
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
