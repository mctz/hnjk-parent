<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>院历管理</title>
</head>
<body>
	<h2 class="contentTitle">${(empty schoolCalendar.resourceid)?'新增':'编辑' }院历</h2>
	<div class="page">
	<div class="pageContent">	
	<form method="post" action="${baseUrl}/edu3/arrange/schoolCalendar/save.html" class="pageForm" onsubmit="return validateCallback(this);">
		<input type="hidden" name="resourceid" value="${schoolCalendar.resourceid }"/>        
		<div class="pageFormContent" layoutH="97">
			<table class="form">
				<tr>
					<td width="20%">院历名称：</td>
					<td width="30%"><input type="text" name="name" style="width:50%" value="${schoolCalendar.name }" class="required"/></td>
					<td width="20%">所属教点：</td>
					<td width="30%"><gh:selectModel name="branchSchoolId" bindValue="resourceid" displayValue="unitName" 
							modelClass="com.hnjk.security.model.OrgUnit" value="${schoolCalendar.unit.resourceid}" 
							style="width:50%" classCss="required" disabled="${brschool?'disabled':'' }"/><font color="red">*</font>
						<c:if test="${brschool }"><input type="hidden" name="branchSchoolId" value="${schoolCalendar.unit.resourceid}"/></c:if>
					</td>
				</tr>
				<tr>
					<td>年度：</td>
					<td>
					<gh:selectModel id="search_bachelorExam_yearInfoId" name="yearInfoId" bindValue="resourceid" displayValue="yearName" classCss="required"
										 modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${schoolCalendar.yearInfo.resourceid}" orderBy="yearName desc" style="width:50%"/>
					</td>
					<td>学期：</td>
					<td><gh:select name="term" dictionaryCode="CodeTerm" classCss="required" value="${schoolCalendar.term}" style="width:50%" /></td>
				</tr>
				<tr>
					<td>每周第一天：</td>
					<td><gh:select name="firstDay" dictionaryCode="CodeWeek" classCss="required" value="${schoolCalendar.firstDay}" style="width:50%" /></td>
					<td><label>周数：</label><input type="text" name="weeks" class="required" style="width:40%" value="${schoolCalendar.weeks }"/></td>
					<td><label>毕业教学周数：</label><input type="text" name="graduateWeeks" class="required" style="width:40%" value="${schoolCalendar.graduateWeeks }"/></td>
				</tr>
				<tr>
					<td>学期开始日期：</td>
					<td><input type="text"  name="termStartDate" size="40" style="width:50%" value="<fmt:formatDate value="${schoolCalendar.termStartDate }" pattern="yyyy-MM-dd" />" class="required date1" 
							 onFocus="WdatePicker({isShowWeek:true})"/></td>
					<td>学期结束日期：</td>
					<td><input type="text"  name="termEndDate" size="40" style="width:50%" value="<fmt:formatDate value="${schoolCalendar.termEndDate }" pattern="yyyy-MM-dd" />" class="required date1" 
							 onFocus="WdatePicker({isShowWeek:true})"/></td>
				</tr>
				<tr>
					<td>教学周开始日期：</td>
					<td><input type="text"  name="startDate" size="40" style="width:50%" value="<fmt:formatDate value="${schoolCalendar.startDate }" pattern="yyyy-MM-dd" />" class="required date1" 
							 onFocus="WdatePicker({isShowWeek:true})"/></td>
					<td>教学周结束日期：</td>
					<td><input type="text"  name="endDate" size="40" style="width:50%" value="<fmt:formatDate value="${schoolCalendar.endDate }" pattern="yyyy-MM-dd" />" class="required date1" 
							 onFocus="WdatePicker({isShowWeek:true})"/></td>
				</tr>
				<%-- <tr>
					<td>毕业教学周开始日期：</td>
					<td><input type="text"  name="graduateStartDate" size="40" style="width:50%" value="<fmt:formatDate value="${schoolCalendar.graduateStartDate }" pattern="yyyy-MM-dd" />" class="date1" 
							 onFocus="WdatePicker({isShowWeek:true})"/></td>
					<td>毕业教学周结束日期：</td>
					<td><input type="text"  name="graduateEndDate" size="40" style="width:50%" value="<fmt:formatDate value="${schoolCalendar.graduateEndDate }" pattern="yyyy-MM-dd" />" class="date1" 
							 onFocus="WdatePicker({isShowWeek:true})"/></td>
				</tr> --%>
				<%-- <tr>
					<td>发布状态：</td>
					<td><gh:select name="status" dictionaryCode="CodePublishStatus" classCss="required" value="${schoolCalendar.status}" style="width:50%" /></td>
				</tr> --%>
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