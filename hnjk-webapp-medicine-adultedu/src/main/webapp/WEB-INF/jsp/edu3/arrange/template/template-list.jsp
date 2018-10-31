<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>模版列表</title>
<script type="text/javascript">
	//新增
	function addArrangeTemplate(){
		navTab.openTab('RES_ARRANGE_TEMPLATE_INPUT', '${baseUrl}/edu3/arrange/template/input.html', '新增排课模版');
	}
	//修改
	function modifyArrangeTemplate(){
		var url = "${baseUrl}/edu3/arrange/template/input.html";
		if(isCheckOnlyone('resourceid','#templateBody')){
			navTab.openTab('RES_ARRANGE_TEMPLATE_INPUT', url+'?templateid='+$("#templateBody input[@name='resourceid']:checked").val()+'&brSchoolid='+$("#templateBody input[@name='resourceid']:checked").attr("brSchoolid"), '编辑排课模版');
		}
	}
	//删除
	function removeArrangeTemplate(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/arrange/template/remove.html","#templateBody");
	}
</script>
</head>
<body>
<div class="page">
	<div class="pageHeader">
		<form id="templateListForm" onsubmit="return navTabSearch(this);" action="${baseUrl}/edu3/arrange/template/list.html" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				<c:if test="${!isBrschool }">
					<li>
						<label>教学点：</label>
						<gh:brSchoolAutocomplete name="branchSchool" id="template-list_branchSchool" defaultValue="${condition['branchSchool']}" displayType="code" tabindex="1" style="width:120px" />
					</li>
				</c:if>
				<c:if test="${isBrschool}">
					<input type="hidden" name="branchSchool" value="${condition['branchSchool']}" />
				</c:if>
				<li>
					<label>院历：</label>
					<gh:selectModel name="schoolCalendarid" bindValue="resourceid" displayValue="name" classCss="combox"
										 modelClass="com.hnjk.edu.arrange.model.SchoolCalendar" value="${condition['schoolCalendarid']}" orderBy="name desc" style="width:120px"/>
				</li>
				<li>
					<label>模版名称：</label>
					<input name="templateName" bindValue="resourceid" displayValue="name" value="${condition['templateName']}" orderBy="name desc" style="width:120px"/>
				</li>
			</ul>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive"><div class="buttonContent"><button type="submit"> 查 询 </button></div></div></li>					
				</ul>
			</div>
		</div>
		</form>
	</div>
	<div  class="pageContent">
		<gh:resAuth parentCode="RES_ARRANGE_TEMPLATE_LIST" pageType="list"></gh:resAuth>
		<table class="table" layouth="138" width="100%" style="table-layout: fixed;">
			<thead>
				<tr>
					<th width="3%"><input type="checkbox" name="checkall" id="check_all_templete" onclick="checkboxAll('#check_all_templete','resourceid','#templateBody')"/></th>
					<th width="10%" style="text-align: center;vertical-align: middle;">院历名称</th>
					<th width="10%" style="text-align: center;vertical-align: middle;">年度</th> 
					<th width="5%" style="text-align: center;vertical-align: middle;">学期</th> 
					<th width="15%" style="text-align: center;vertical-align: middle;">模版名称</th> 
					<th width="15%" style="text-align: center;vertical-align: middle;">星期</th> 
					<th width="25%" style="text-align: center;vertical-align: middle;">时间段</th> 
					<th width="15%" style="text-align: center;vertical-align: middle;">上课时间</th>  
				</tr>
			</thead>  	  	   	   	   	
			<tbody id="templateBody">
		       <c:forEach items="${templateList.result}" var="template" varStatus="vs">
			        <tr>
			        	<td><input type="checkbox" name="resourceid" value="${template.resourceid }" brSchoolid="${template.schoolCalendar.unit.resourceid}" autocomplete="off" /></td>
			        	<td style="text-align: center;vertical-align: middle;">${template.schoolCalendar.name }</td>
			        	<td style="text-align: center;vertical-align: middle;">${template.schoolCalendar.yearInfo.yearName }</td>
			        	<td style="text-align: center;vertical-align: middle;">${template.schoolCalendar.term }</td>
			        	<td style="text-align: center;vertical-align: middle;">${template.templateName }</td>
			        	<td style="text-align: center;vertical-align: middle;" title="${template.daysName }">${template.daysName }</td>
			        	<td style="text-align: center;vertical-align: middle;" title="${template.timePeriodNames }">${template.timePeriodNames }</td>
			            <td style="text-align: center;vertical-align: middle;">
			            <c:choose>
						<c:when test="${empty template.weeks }">
							 <fmt:formatDate value="${template.startDate }" pattern="yyyy-MM-dd HH:mm:ss"/> 至 <fmt:formatDate value="${template.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
						</c:when>
						<c:otherwise>
							${template.weeksName }
						</c:otherwise>
						</c:choose>
			            </td>
			        </tr>
	       		</c:forEach>
			</tbody>
		</table>
		<gh:page page="${templateList}" goPageUrl="${baseUrl }/edu3/arrange/template/list.html" pageType="sys" condition="${condition}"/>		
	</div>
</div>
</body>
</html>