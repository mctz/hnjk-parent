<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>工作时间管理</title>
</head>
<body>
	<h2 class="contentTitle">${(empty userTimeManage.resourceid)?'新增':'编辑' }工作时间管理</h2>
	<div class="page">
	<div class="pageContent">	
	<form method="post" action="${baseUrl}/edu3/work/userTimeManage/save.html" class="pageForm" onsubmit="return validateCallback(this);">
		<input type="hidden" name="resourceid" value="${userTimeManage.resourceid }"/>
		<div class="pageFormContent" layoutH="97">
			<table class="form">
				<tr>
					<td width="20%">所属教点：</td>
					<td width="30%"><gh:selectModel name="branchSchoolId" bindValue="resourceid" displayValue="unitName"
							modelClass="com.hnjk.security.model.OrgUnit" value="${userTimeManage.unit.resourceid}" condition="unitType='brSchool'"
							style="width:50%" classCss="required" disabled="${brschool?'disabled':'' }"/><font color="red">*</font>
						<c:if test="${brschool }"><input type="hidden" name="branchSchoolId" value="${userTimeManage.unit.resourceid}"/></c:if>
					</td>
					<td width="20%">活动类型</td>
					<td width="30%">
						<gh:select name="workType" dictionaryCode="Code.WorkManage.workType" classCss="required" value="${userTimeManage.workType}" style="width:50%" />
					</td>
				</tr>
				<tr>
					<td>年度：</td>
					<td>
					<gh:selectModel  name="yearInfoId" bindValue="resourceid" displayValue="yearName" classCss="required"
										 modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${userTimeManage.yearInfo.resourceid}" orderBy="yearName desc" style="width:50%"/>
					</td>
					<td>学期：</td>
					<td><gh:select name="term" dictionaryCode="CodeTerm" classCss="required" value="${userTimeManage.term}" style="width:50%" /></td>
				</tr>

				<tr>
					<td>开始日期：</td>
					<td><input type="text"  name="startTime" size="40" style="width:50%" value="<fmt:formatDate value="${userTimeManage.startTime }" pattern="yyyy-MM-dd" />"
							 onFocus="WdatePicker({isShowWeek:true})"/></td>
					<td>截止日期：</td>
					<td><input type="text"  name="endTime" size="40" style="width:50%" value="<fmt:formatDate value="${userTimeManage.endTime }" pattern="yyyy-MM-dd" />"
							 onFocus="WdatePicker({isShowWeek:true})"/></td>
				</tr>
				<tr>
					<td>竞选时间：</td>
					<td><input type="text"  name="joinTime" size="40" style="width:50%" value="<fmt:formatDate value="${userTimeManage.joinTime }" pattern="yyyy-MM-dd HH:mm:ss" />"
							 onFocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>地点</td>
					<td colspan="3"><textarea name="workPlace" style="width: 50%" rows="3">${userTimeManage.workPlace }</textarea></td>
				</tr>
			</table>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent">
					<button type="submit">提交</button>
					</div></div></li>
					<li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="navTab.closeCurrentTab();">取消</button></div></div></li>
			</ul>
		</div>
	</form>
	</div>
	</div>	
</body>
</html>