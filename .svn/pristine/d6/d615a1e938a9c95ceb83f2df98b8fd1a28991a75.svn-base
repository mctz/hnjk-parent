<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>栏目管理</title>

<script type="text/javascript">

	function GetXmlHttpObject(){
	  var xmlHttp=null;
	  try{
	    xmlHttp=new XMLHttpRequest();
	  }catch (e){
	    try{
	      xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
	    }catch (e){
	      xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	    }
	  }
	  return xmlHttp;
	}
	
	function showChange(){
	  xmlHttp=GetXmlHttpObject();
	  if (xmlHttp==null){
	    alert ("您的浏览器不支持AJAX！");
	    return;
	  }
	  var url="${baseUrl }/edu3/portal/manage/helper/channel/validate.html";
	  var selector = document.getElementById('channelChannelId');
	  var resid = selector.options[selector.selectedIndex].value;
	  url=url+"?resid="+resid;
	  xmlHttp.onreadystatechange=stateChanged;
	  xmlHttp.open("GET",url,true);
	  xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	  xmlHttp.send(null);
	  
	}
	function stateChanged() { 
	  if(xmlHttp.readyState==4&&xmlHttp.status == 200){ 
	  var res = xmlHttp.responseText;
	  var associationRoleCodes='';
	  var associationRoleNames=''; 
	  if(null!=res&&'{"roleName":"","roleCode":null}'!=res){
		  var array = res.split('","');
		  associationRoleNames = array[0].split('":"')[1];
		  associationRoleCodes = array[1].split('":"')[1].split('"')[0];
	  }
	  document.getElementById("associationRoleNames").value=associationRoleNames;
	  document.getElementById("associationRoleCodes").value=associationRoleCodes;
	  }
	}

</script>
</head>
<body>
	<h2 class="contentTitle">
		<c:if test="${method=='add'}">新增在线帮助栏目</c:if>
		<c:if test="${method!='add'}">编辑在线帮助栏目</c:if>
	</h2>
	<div class="page">
		<div class="pageContent">


			<form method="post"
				<c:choose>
   		<c:when test='${method=="add"}'>  
   			action='${baseUrl}/edu3/portal/manage/helper/channel/save.html?method=add' 
   		</c:when>
   		<c:otherwise> 
   			action='${baseUrl}/edu3/portal/manage/helper/channel/save.html'
   		</c:otherwise>
	</c:choose>
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<input type="hidden" name="resourceid"
							value="${helpChannel.resourceid }" />
						<input type="hidden" name="channelLevel"
							value="${helpChannel.channelLevel }" />
						<input type="hidden" name="isChild"
							value="${helpChannel.isChild }" />
						<input type="hidden" name="parentId" value="${parentId}" />
						<tr>
							<td style="width: 20%">栏目名称:</td>
							<td><input type="text" name="channelName" size="34"
								value="${helpChannel.channelName }" class="required" /></td>
						</tr>
						<tr>
							<td style="width: 20%">父栏目:</td>
							<td><select id="channelChannelId" name="channelChannelId"
								onchange="showChange()">
									<c:forEach items="${allChanList}" var="hC">
										<option value="${hC.resourceid}"
											<c:if test="${hC.resourceid eq parentId}">selected="selected"</c:if>>
											<c:forEach begin="1" end="${hC.channelLevel}">
				            	&nbsp;&nbsp;&nbsp;
				            	</c:forEach>${hC.channelName}
										</option>
									</c:forEach>
							</select></td>
						</tr>
						<tr>
							<td style="width: 20%">排序:</td>
							<td><input type="text" name="showOrder"
								value="${helpChannel.showOrder }" size="6" /> <span
								style="padding: 4px; color: red">数字越小越靠前</span></td>
						</tr>
						<tr>
							<td>关联角色:</td>
							<td colspan="3" id="associationRole">
								<div id="roleDiv">
									<input type="hidden" id="associationRoleCodes"
										name="relateRoles" value="${helpChannel.relateRoles}" /> <input
										type="text" id="associationRoleNames" readonly="true"
										value="${relateRolesName}" style="width: 80%;" />&nbsp;&nbsp;
									<a href="#" class="button"
										onclick="javascript:$.pdialog.open('${baseUrl }/edu3/portal/message/role.html?codesN=associationRoleCodes&namesN=associationRoleNames','selector','选择角色',{mask:true,width:700,height:500});"><span>选择角色</span></a>
								</div>

							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<!-- 隐藏域 -->
									<input type="hidden" name="fillinMan"
										value="${helpChannel.fillinMan }" /> <input type="hidden"
										name="fillinManId" value="${helpChannel.fillinManId }" /> <input
										type="hidden" name="fillinDate"
										value=" <fmt:formatDate value="${helpChannel.fillinDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>

</html>