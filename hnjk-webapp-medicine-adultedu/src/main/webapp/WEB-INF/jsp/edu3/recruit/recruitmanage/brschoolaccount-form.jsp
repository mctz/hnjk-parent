<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生账号管理</title>
<script type="text/javascript">
//校验唯一性
 function validateOnly(){
    	var userid = jQuery("#brschoolAccountForm input[name='resourceid']");
    	var userName = jQuery("#brschoolAccountForm input[name='username']");    	
    	if(userName.val()==""){ alertMsg.warn("请输入登录账号"); userName.focus() ;return false; }
    	var url = "${baseUrl}/edu3/framework/system/org/user/validateCode.html";
    	jQuery.post(url,{username:userName.val(),resourceid:userid.val()},function(data){
    		if(data == "exist"){ alertMsg.warn("账号已存在!"); }else{ alertMsg.correct("恭喜，此账号可用！")}
    	})
}

 </script>
</head>
<body>
	<h2 class="contentTitle">编辑招生账号</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/recruit/recruitmanage/brschoolaccount-save.html"
				class="pageForm" id="brschoolAccountForm"
				onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>基本信息</span></a></li>
									<li><a href="#"><span>教务管理人员信息</span></a></li>
									<li><a href="#"><span>授权角色</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<table class="form">
									<input type="hidden" name="resourceid" id="resourceid"
										value="${user.resourceid }" />
									<input type="hidden" name="userType" value="${user.userType }" />
									<tr>
										<td style="width: 12%">登录账号:</td>
										<td style="width: 38%"><input type="text" name="username"
											id="username" size="34" value="${user.username }"
											class="required alphanumeric" alt="请输入字母下划线或数字的组合" /> <span
											class="buttonActive" style="margin-left: 8px"><div
													class="buttonContent">
													<button type="button" onclick="validateOnly()">检查唯一性</button>
												</div></span></td>
										<td>用户姓名:</td>
										<td><input type="text" name="cnName" size="40"
											value="${user.cnName }" class="required" /></td>
									</tr>
									<tr>
										<td style="width: 12%">所属教学站:</td>
										<td style="width: 38%"><gh:selectModel name="unitId"
												bindValue="resourceid" displayValue="unitName"
												modelClass="com.hnjk.security.model.OrgUnit"
												value="${unitId}" choose="Y" orderBy="unitName asc"
												condition="unitType='brSchool' and status='normal'" /></td>
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
							</div>
							<div>
								<table class="form">
									<tr>
										<td style="width: 12%">编号:</td>
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
											value="${user.officeTel }" class="required phone" /></td>
									</tr>
									<tr>
										<td style="width: 12%">家庭电话:</td>
										<td style="width: 38%"><input type="text" name="homeTel"
											value="${user.homeTel }" /></td>
										<td style="width: 12%">手机:</td>
										<td style="width: 38%"><input type="text" name="mobile"
											value="${user.mobile }" /></td>
									</tr>
									<tr>
										<td style="width: 12%">E-mail:</td>
										<td style="width: 38%"><input type="text" name="email"
											value="${user.email }" class="required email" /></td>
										<td style="width: 12%">文号:</td>
										<td style="width: 38%"><input type="text"
											name="documentcode" value="${user.documentcode }" /></td>
									</tr>
									<tr>
										<td>简介:</td>
										<td colspan="3"><textarea rows="3" cols=""
												name="introduction" style="width: 80%">${user.introduction }</textarea>
										</td>
									</tr>
								</table>
							</div>
							<div>
								<table class="form" id="childDict">
									<tr>
										<td width="15%">&nbsp;</td>
										<td width="35%">角色名称</td>
										<td>角色描述</td>
									</tr>
									<c:forEach items="${roleList}" var="role" varStatus="vs">
										<tr>
											<td>
												<center>
													<input type="radio" id="roleId" name="roleId"
														value="${role.resourceid}" checked="checked" />
													<!--<c:if test="${not empty user.roles }">
									<c:forEach items="${user.roles}" var="urole">
									 <c:if test="${urole.resourceid eq role.resourceid }"> checked="checked"</c:if> 								
									</c:forEach>
								</c:if> 
								-->
													&nbsp;
												</center>
											</td>
											<td>${role.roleName}&nbsp;</td>
											<td>${role.roleDescript}&nbsp;</td>
										</tr>
									</c:forEach>
								</table>
							</div>
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

</body>
</html>