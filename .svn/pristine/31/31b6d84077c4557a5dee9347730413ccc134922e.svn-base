<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>院历事件列表</title>
<script type="text/javascript">
	//新增
	function addCalendarEvent(){
		navTab.openTab('RES_ARRANGE_CALENDAREVENT_INPUT', '${baseUrl}/edu3/arrange/calendarEvent/input.html', '新增院历事件');
	}
	//修改
	function modifyCalendarEvent(){
		var url = "${baseUrl}/edu3/arrange/calendarEvent/input.html";
		if(isCheckOnlyone('resourceid','#sCalendarEventBody')){
			navTab.openTab('RES_ARRANGE_CALENDAREVENT_INPUT', url+'?eventid='+$("#sCalendarEventBody input[@name='resourceid']:checked").val(), '编辑院历事件');
		}			
	}
	//删除
	function removeCalendarEvent(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/arrange/calendarEvent/remove.html","#sCalendarEventBody");
	}
</script>
</head>
<body>
<div class="page">
	<div class="pageHeader">
		<form id="schoolCalendarEvent" onsubmit="return navTabSearch(this);" action="${baseUrl}/edu3/arrange/schoolCalendar/event.html" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				<li>
					<label>年度：</label>
					<gh:selectModel id="sCalendarEvent_yearInfoId" name="yearInfoId" bindValue="resourceid" displayValue="yearName" 
										 modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${condition['yearInfoId']}" orderBy="yearName desc" style="width:125px"/>
				</li>
				<li>
					<label>学期：</label>
					<gh:select name="term" dictionaryCode="CodeTerm" value="${condition['term']}" style="width:125px" />
				</li>		
				<li>
					<label>教学点：</label>
					<c:if test="${isBrschool }"><input type="hidden" name="brSchoolid" value="${condition['brSchoolid']}" >
						<input type="text" value="${brschoolName }" readonly="readonly" style="width: 125px"></c:if>
					<c:if test="${!isBrschool }"><gh:selectModel name="brSchoolid" bindValue="resourceid" displayValue="unitName" 
							modelClass="com.hnjk.security.model.OrgUnit" value="${condition['brSchoolid']}" style="width:125px"/></c:if>
				</li>
			</ul>
			<ul class="searchContent">
				<li>
					<label>院历：</label>
					<gh:selectModel id="schoolCalendar" name="schoolCalendarid" bindValue="resourceid" displayValue="name" 
										 modelClass="com.hnjk.edu.arrange.model.SchoolCalendar" value="${condition['schoolCalendarid']}" orderBy="name desc" style="width:125px"/>
				</li>
				<li>
					<label>名称：</label>
					<input type="text" name="name"  value="${condition['name']}" style="width:125px" />
				</li>
				<li>
					<label>类型：</label>
					<gh:select name="type" value="${condition['type']}" dictionaryCode="CodeCalendarEvent" style="width:125px" />
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
		<gh:resAuth parentCode="RES_ARRANGE_CALENDAREVENT_LIST" pageType="list"></gh:resAuth>
		<table class="table" layouth="160" width="100%">
			<thead>
				<tr>
					<th width="5%"><input type="checkbox" name="checkall" id="check_all_scEvent" onclick="checkboxAll('#check_all_scEvent','resourceid','#sCalendarEventBody')"/></th>
					
					<th width="15%" style="text-align: center;vertical-align: middle;">事件名称</th> 
					<th width="15%" style="text-align: center;vertical-align: middle;">院历名称</th> 
					<th width="20%" style="text-align: center;vertical-align: middle;">时间段</th> 
					<th width="10%" style="text-align: center;vertical-align: middle;">类型</th>  
					<th width="30%" style="text-align: center;vertical-align: middle;">内容</th>     
				</tr>
			</thead>  	  	   	   	   	
			<tbody id="sCalendarEventBody">
		       <c:forEach items="${sCalendarEventList.result}" var="event" varStatus="vs">
			        <tr>
			        	<td><input type="checkbox" name="resourceid" value="${event.resourceid }" autocomplete="off" /></td>
			        	<td style="text-align: center;vertical-align: middle;">${event.name }</td>
			        	<td style="text-align: center;vertical-align: middle;">${event.schoolCalendar.name }</td>
			            <td style="text-align: center;vertical-align: middle;">
			            <fmt:formatDate value="${event.startDate }" pattern="yyyy-MM-dd HH:mm:ss"/> 至 <fmt:formatDate value="${event.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
			            </td>
			            <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeCalendarEvent',event.type ) }</td>
			            <td style="text-align: center;vertical-align: middle;">${event.content }</td>
			            
			        </tr>
	       		</c:forEach>
			</tbody>
		</table>
		<gh:page page="${sCalendarEventList}" goPageUrl="${baseUrl }/edu3/arrange/schoolCalendar/event.html" pageType="sys" condition="${condition}"/>		
	</div>
</div>
</body>
</html>