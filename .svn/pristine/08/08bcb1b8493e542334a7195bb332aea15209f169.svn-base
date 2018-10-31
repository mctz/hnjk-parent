<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户管理</title>

<script type="text/javascript">
//校验唯一性
 function validateOnlyUser(){
 		var userid = $("#userEditForm #resourceid");
    	var userName = $("#userEditForm #username");    	
    	if(userName.val()==""){ alertMsg.warn("请输入登录账号"); userName.focus() ;return false; }
    	var url = "${baseUrl}/edu3/framework/system/org/user/validateCode.html";
    	jQuery.post(url,{username:userName.val(),resourceid:userid.val()},function(data){
    		if(data == "exist"){ alertMsg.warn("账号已存在!"); }else{ alertMsg.correct("恭喜，此账号可用！")}
    	})
   }
	//获取角色
  function checkSingleRole(obj){
    	var delRoleId = jQuery("input[name='hidRoleId']").val();
    	var valStr = ","+obj.value;
     	if(obj.checked){ //选中，从删除隐藏域中删除
    		if(delRoleId.length>0 && delRoleId.indexOf(obj.value)>-1){
    			var tt = delRoleId.replace(valStr,"");
	    		jQuery("input[name='hidRoleId']").val(tt);
    		}
    	}else{ //否则，添加到隐藏域中
	    	var delIds = jQuery("input[name='hidRoleId']").val()
	    	delIds = delIds + valStr;
	    	jQuery("input[name='hidRoleId']").val(delIds)
    	}
     	//设置用户为主讲老师角色后,自动添加班主任角色
    	if(obj.value=="402881382d0bddc7012d0cfa349f01df" && obj.checked){
    		alert("设置主讲老师角色后,默认会添加班主任角色！");
    		$("input[value='4028814946a46a690146a7880d200004']").attr("checked","checked");
   		}
     	
    }
  function openUserUnitTree(){
	 	var defaultValues = $("#userEditForm #_userEditForm_unitId").val();
	 	orgUnitSelector('_userEditForm_unitId','unitName','radio','',defaultValues);
 }
 </script>
</head>
<body>
	<h2 class="contentTitle">编辑用户</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/system/org/user/save.html" class="pageForm"
				id="userEditForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>基本信息</span></a></li>
									<li><a href="#"><span>教务管理人员信息</span></a></li>
									<security:authorize ifAnyGranted="ROLE_ADMIN">
										<li><a href="#"><span>授权角色</span></a></li>
									</security:authorize>
									<security:authorize ifAnyGranted="ROLE_NEWUSER">
										<li><a href="#"><span>授权角色</span></a></li>
									</security:authorize>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<table class="form">
									<input type="hidden" name="resourceid" id="resourceid"
										value="${resourceid }">
									<input type="hidden" name="userType" value="${user.userType }">
									<tr>
										<td style="width: 12%">登录账号:</td>
										<td style="width: 38%"><input type="text" name="username"
											id="username" size="34" value="${user.username }"
											class="required alphanumeric" alt="请输入字母下划线或数字的组合" /> <span
											class="buttonActive" style="margin-left: 8px"><div
													class="buttonContent">
													<button type="button" onclick="validateOnlyUser();">检查唯一性</button>
												</div></span></td>
										<td>用户姓名:</td>
										<td><input type="text" name="cnName" size="40"
											value="${user.cnName }" class="required" /></td>
									</tr>
									<tr>
										<td style="width: 12%">用户组织:</td>
										<td style="width: 38%"><input type="text" id="unitName"
											value="${orgUnit.unitShortName }" readonly="readonly" /> <input
											type="hidden" name="unitId" id="_userEditForm_unitId"
											value="${orgUnit.resourceid }" /> <span class="buttonActive"
											style="margin-left: 8px">
												<div class="buttonContent">
													<button type="button" onclick="openUserUnitTree();">选择组织单位</button>
												</div>
										</span></td>
										<td style="width: 12%">用户密码:</td>
										<td style="width: 38%"><input type="password"
											name="password" value="${user.password }" class="required">
										</td>
									</tr>
									<tr>
										<td style="width: 12%">是否可用:</td>
										<td style="width: 38%"><gh:select name="enabledChar"
												value="${user.enabledChar}" dictionaryCode="yesOrNo" /></td>
										<c:choose>
											<c:when test="${user.isDeleted == 1 }">
												<td style="width: 12%">恢复该账号:</td>
												<td style="width: 38%"><input type="checkbox"
													name="isDeleted" value="0" /> 恢复</td>
											</c:when>
											<c:otherwise>
												<td style="width: 12%">&nbsp;</td>
												<td style="width: 38%">&nbsp;</td>
											</c:otherwise>
										</c:choose>
									</tr>

								</table>
								<p style="width: 100%">
									<font color='red'>说明：<br /> 禁止用户账号登录使用系统，请选择'是否可用'为'否'；
										将删除的用户恢复到正常状态，请选择'恢复该账号'
									</font>

								</p>
							</div>
							<div>
								<table class="form">
									<tr>
										<td style="width: 12%">教师编号:</td>
										<td style="width: 38%"><input type="text"
											name="teacherCode" value="${user.teacherCode}"
											class="required" /></td>
										<td style="width: 12%">姓名拼音:</td>
										<td style="width: 38%"><input type="text" name="namePY"
											value="${user.namePY}" /></td>
									</tr>
									<tr>
										<td style="width: 12%">出生日期:</td>
										<td style="width: 38%"><input type="text" name="birthday"
											value="${user.birthday}" class="Wdate"
											onFocus="WdatePicker({isShowWeek:true})" /></td>
										<td style="width: 12%">性别:</td>
										<td style="width: 38%"><gh:select name="gender"
												value="${user.gender}" dictionaryCode="CodeSex" /></td>
									</tr>
									<tr>
										<td style="width: 12%">职称代码:</td>
										<td style="width: 38%"><gh:select name="titleOfTechnical"
												value="${user.titleOfTechnical}"
												dictionaryCode="CodeTitleOfTechnicalCode" /></td>
										<td style="width: 12%">办公电话:</td>
										<td style="width: 38%"><input type="text" name=officeTel
											value="${user.officeTel }" class="phone" /></td>
									</tr>
									<tr>
										<td style="width: 12%">家庭电话:</td>
										<td style="width: 38%"><input type="text" name="homeTel"
											value="${user.homeTel }" class="phone" /></td>
										<td style="width: 12%">手机:</td>
										<td style="width: 38%"><input type="text" name="mobile"
											value="${user.mobile }" class="mobile" /></td>
									</tr>
									<tr>
										<td style="width: 12%">E-mail:</td>
										<td style="width: 38%"><input type="text" name="email"
											value="${user.email }" class="email" /></td>
										<td style="width: 12%">文号:</td>
										<td style="width: 38%"><input type="text"
											name="documentcode" value="${user.documentcode }" /></td>
									</tr>
									<tr>
										<td>邮编:</td>
										<td><input type="text" name="postCode"
											value="${user.postCode }" class="postcode" /></td>
										<td>编制:</td>
										<td><gh:select name="orgUnitType"
												value="${user.orgUnitType}"
												dictionaryCode="teacherOrgUnitType" /></td>
									</tr>
									<tr>
										<td>单位通信地址:</td>
										<td colspan="3"><input type="text" name="homeAddress"
											value="${user.homeAddress }" 
											style="width: 75%;" /></td>
									</tr>
									<tr>
										<td>简介:</td>
										<td colspan="3"><textarea rows="3" cols=""
												name="introduction" style="width: 80%">${user.introduction }</textarea>
										</td>
									</tr>
								</table>
							</div>

							<security:authorize ifAnyGranted="ROLE_ADMIN">
								<div>
									<table class="form" id="childDict">
										<tr>
											<td width="15%">&nbsp;<input type="hidden"
												name="hidRoleId" value="" /></td>
											<td width="35%">角色名称</td>
											<td>角色描述</td>
										</tr>
										<c:forEach items="${roleList}" var="role" varStatus="vs">
											<tr>
												<td>
													<center>
														<input type="checkbox" id="roleId" name="roleId"
															value="${role.resourceid}"
															onclick="checkSingleRole(this)"
															<c:if test="${not empty userRole}">
									 <c:forEach items="${userRole }" var="ur">
									 	<c:if test="${ur.resourceid eq role.resourceid }"> checked="true" </c:if>
										</c:forEach>
										</c:if>
										>&nbsp;
										</center>
										</td>
										<td>${role.roleName}&nbsp;</td>
										<td>${role.roleDescript}&nbsp;</td>
										</tr>
										</c:forEach>
									</table>
								</div>
							</security:authorize>
							<security:authorize ifAnyGranted="ROLE_NEWUSER">
								<div>
									<table class="form" id="childDict">
										<tr>
											<td width="15%">&nbsp;<input type="hidden"
												name="hidRoleId" value="" /></td>
											<td width="35%">角色名称</td>
											<td>角色描述</td>
										</tr>
										<c:forEach items="${roleList}" var="role" varStatus="vs">
											<c:if test="${role.roleCode eq 'ROLE_BRS_STUDENTSTATUS' }">
												<tr>
													<td>
														<center>
															<input type="checkbox" id="roleId" name="roleId"
																value="${role.resourceid}"
																onclick="checkSingleRole(this)"
																<c:if test="${not empty userRole}">
									 <c:forEach items="${userRole }" var="ur">
									 	<c:if test="${ur.resourceid eq role.resourceid }"> checked="true" </c:if>
										</c:forEach>
										</c:if>
										>&nbsp;
										</center>
										</td>
										<td>${role.roleName}&nbsp;</td>
										<td>${role.roleDescript}&nbsp;</td>
										</tr>
										</c:if>
										</c:forEach>

									</table>
								</div>
							</security:authorize>
						</div>
						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
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
	<script type="text/javascript">
<!--
	//if('${not empty userRole}'){
		//var role = "${userRole}".toString();
		//var roles = role.substring(1,role.length-1).split(",");
		//for(var index=0;index<roles.length;index++){
		//	jQuery("input[value='"+jQuery.trim(roles[index])+"']").attr("checked",true);
		//}
		
//	}
//-->
</script>
</body>
</html>