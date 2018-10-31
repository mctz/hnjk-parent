<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>院历列表</title>
<script type="text/javascript">
	//新增
	function addSchoolCalendar(){
		navTab.openTab('RES_ARRANGE_SCHOOLCALENDAR_INPUT', '${baseUrl}/edu3/arrange/schoolCalendar/input.html', '新增院历');
	}
	//修改
	function modifySchoolCalendar(){
		var url = "${baseUrl}/edu3/arrange/schoolCalendar/input.html";
		if(isCheckOnlyone('resourceid','#sCalendarBody')){
			var isUsed = $("#sCalendarBody input[@name='resourceid']:checked").attr("isUsed");
			if(isUsed=="Y"){
				alertMsg.warn("该院历已发布并使用，不允许修改！");
			}else {
				navTab.openTab('RES_ARRANGE_SCHOOLCALENDAR_INPUT', url+'?resourceid='+$("#sCalendarBody input[@name='resourceid']:checked").val(), '编辑院历');
			}
		}			
	}
	//删除
	function removeSchoolCalendar(){
		var status = $("#sCalendarBody input[@name='resourceid']:checked").attr("status");
		var isUsed = $("#sCalendarBody input[@name='resourceid']:checked").attr("isUsed");
		if(isUsed=="Y"){
			alertMsg.warn("该院历已发布并使用，不允许删除！");
		}else{
			pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/arrange/schoolCalendar/remove.html","#sCalendarBody");
		}
	}
	//发布
	function publishSchoolCalendar(){	
		pageBarHandle("您确定要发布这些记录吗？","${baseUrl}/edu3/arrange/schoolCalendar/publish.html","#sCalendarBody");
	}
	//查看事件
	function queryEvent(){	
		var ids = "";
		$("#sCalendarBody input[@name='resourceid']:checked").each(function(){
			if(""==ids){
				ids += $(this).val();
	    	}else{
	    		ids += ","+$(this).val();
	    	}
        });
		if(ids == ""){
			alertMsg.warn("请您至少选择一条记录进行查看");
			return;
		}
		var url = "${baseUrl}/edu3/arrange/schoolCalendarEvent/list.html";
		navTab.openTab('RES_ARRANGE_SCHOOLCALENDAR_INPUT', url+'?ids='+ids, '查看院历事件');
	}
</script>
</head>
<body>
<div class="page">
	<div class="pageHeader">
		<form id="schoolCalendar" onsubmit="return navTabSearch(this);" action="${baseUrl}/edu3/arrange/schoolCalendar/list.html" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				<li>
					<label>年度：</label>
					<gh:selectModel id="schoolCalendar_yearInfoId" name="yearInfoId" bindValue="resourceid" displayValue="yearName" 
										 modelClass="com.hnjk.edu.basedata.model.YearInfo" value="${condition['yearInfoId']}" orderBy="yearName desc" style="width:125px"/>
				</li>
				<li>
					<label>学期：</label>
					<gh:select name="term" dictionaryCode="CodeTerm" value="${condition['term']}" style="width:125px" />
				</li>		
				<c:if test="${!isBrschool }">
				<li>
					<label>教学点：</label>
					<c:if test="${isBrschool }"><input type="hidden" name="branchSchool" value="${condition['branchSchool']}" >
						<input type="text" value="${brschoolName }" readonly="readonly" style="width: 125px"></c:if>
					<c:if test="${!isBrschool }"><gh:selectModel name="branchSchool" bindValue="resourceid" displayValue="unitName" 
							modelClass="com.hnjk.security.model.OrgUnit" value="${condition['branchSchool']}" style="width:125px"/></c:if>
				</li>
				</c:if>
				<c:if test="${isBrschool}">
						<input type="hidden" name="branchSchool" id="schoolCalendar_branchSchool" value="${condition['branchSchool']}" />
				</c:if>
			</ul>
			<ul class="searchContent">
				<li>
					<label>名称：</label>
					<input type="text" name="name"  value="${condition['schoolCalendar_name']}" style="width:125px" />
				</li><li>
					<label>发布状态：</label>
					<gh:select name="status" value="${condition['status']}" dictionaryCode="CodePublishStatus" style="width:125px" />
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
		<gh:resAuth parentCode="RES_ARRANGE_SCHOOLCALENDAR_LIST" pageType="list"></gh:resAuth>
		<table class="table" layouth="163" width="100%">
			<thead>
				<tr>
					<th width="3%"><input type="checkbox" name="checkall" id="check_all_sc" onclick="checkboxAll('#check_all_sc','resourceid','#sCalendarBody')"/></th>
					<th width="12%" style="text-align: center;vertical-align: middle;">教学站</th> 
					<th width="10%" style="text-align: center;vertical-align: middle;">名称</th> 
					<th width="8%" style="text-align: center;vertical-align: middle;">年度</th>            
		            <th width="8%" style="text-align: center;vertical-align: middle;">学期</th> 
		            <th width="5%" style="text-align: center;vertical-align: middle;">每周第一天</th> 
		            <th width="5%" style="text-align: center;vertical-align: middle;">周数</th> 
		            <th width="5%" style="text-align: center;vertical-align: middle;">毕业教学周数</th> 
					<th width="10%" style="text-align: center;vertical-align: middle;">学期起始日期</th> 
					<th width="10%" style="text-align: center;vertical-align: middle;">教学周日期段</th>
					<!-- <th width="10%" style="text-align: center;vertical-align: middle;">毕业教学周日期段</th>  -->
					<th width="8%" style="text-align: center;vertical-align: middle;">发布状态</th>         
				</tr>
			</thead>  	  	   	   	   	
			<tbody id="sCalendarBody">
		       <c:forEach items="${schoolCalendarList.result}" var="schoolCalendar" varStatus="vs">
			        <tr>
			        	<td><input type="checkbox" name="resourceid" value="${schoolCalendar.resourceid }" calendarEvent="${schoolCalendar.calendarEvents }" status="${schoolCalendar.status }" isUsed="${schoolCalendar.isUsed}" autocomplete="off" /></td>
			        	<td style="text-align: center;vertical-align: middle;">${schoolCalendar.unit.unitName }</td>
			        	<td style="text-align: center;vertical-align: middle;">${schoolCalendar.name }</td>
				        <td style="text-align: center;vertical-align: middle;">${schoolCalendar.yearInfo.yearName }</td>
				        <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeTermType',schoolCalendar.term) }</td>
				        <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodeWeek',schoolCalendar.firstDay) }</td>
			            <td style="text-align: center;vertical-align: middle;">${schoolCalendar.weeks }</td>
			            <td style="text-align: center;vertical-align: middle;">${schoolCalendar.graduateWeeks }</td>
			            <td style="text-align: center;vertical-align: middle;"><fmt:formatDate value="${schoolCalendar.termStartDate }" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate value="${schoolCalendar.termEndDate }" pattern="yyyy-MM-dd"/></td>
			            <td style="text-align: center;vertical-align: middle;"><fmt:formatDate value="${schoolCalendar.startDate }" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate value="${schoolCalendar.endDate }" pattern="yyyy-MM-dd"/></td>
			            <%-- <td style="text-align: center;vertical-align: middle;"><fmt:formatDate value="${schoolCalendar.graduateStartDate }" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate value="${schoolCalendar.graduateEndDate }" pattern="yyyy-MM-dd"/></td> --%>
			            <td style="text-align: center;vertical-align: middle;">${ghfn:dictCode2Val('CodePublishStatus',schoolCalendar.status) }</td>
			        </tr>
	       		</c:forEach>
			</tbody>
		</table>
		<gh:page page="${schoolCalendarList}" goPageUrl="${baseUrl }/edu3/arrange/schoolCalendar/list.html" pageType="sys" condition="${condition}"/>		
	</div>
</div>
</body>
</html>