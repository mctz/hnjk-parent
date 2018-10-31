<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	import="com.hnjk.edu.recruit.*"
	import="com.hnjk.edu.recruit.service.impl.statMatriculate.*"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预约教材统计</title>
<script type="text/javascript">
function statCourseBookOrderStat(){
	var yearInfoId = $("#searchForm [name='yearInfo']").val();
	var term = $("#searchForm [name='term']").val();
	if(yearInfoId =="" || term == ""){
		alertMsg.warn("请选择年度和学期！");		
	} else {
		window.location.href="${baseUrl}/edu3/teaching/coursebookorderstat/stat.html?yearInfoId="+yearInfoId+"&term="+term;
	}
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="searchForm" name="searchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/courseorder/statBookOrder.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label> <gh:selectModel name="yearInfo"
								bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfo']}" orderBy="firstYear desc " /></li>
						<li><label>学期：</label>
						<gh:select name="term" value="${condition['term']}"
								dictionaryCode="CodeTerm" /></li>
						<c:if test="${!isBranchSchool}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="statbookorder_eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:120px" /></li>
						</c:if>
					</ul>
					<ul class="searchContent">
						<%-- 
			<li>              
		         <label> 层次：</label><gh:selectModel id="classic" name="classic" bindValue="resourceid" displayValue="classicName" 
									modelClass="com.hnjk.edu.basedata.model.Classic"  value="${condition['classic']}" style="width:120px"/>
                 </li>
            <li>
				<label>专业：</label>
				<gh:selectModel name="major" bindValue="resourceid" displayValue="majorCodeName" value="${condition['major']}" 
							modelClass="com.hnjk.edu.basedata.model.Major" style="width:120px"/>
			
			</li>
			 --%>
						<li><label>课程：</label> <gh:courseAutocomplete name="course"
								tabindex="1" id="cId" value="${condition['course']}"
								displayType="code"></gh:courseAutocomplete></li>
						<li><label>开始时间：</label> <input type="text" name="startTime"
							value="${condition['startTime']}" class="Wdate"
							id="statbookorder_eiinfo_StartTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'statbookorder_eiinfo_EndTime\')}'})" />

						</li>
						<li><label>结束时间：</label> <input type="text" name="endTime"
							value="${condition['endTime']}" class="Wdate"
							id="statbookorder_eiinfo_EndTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'statbookorder_eiinfo_StartTime\')}'})" />
						</li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">预约教材统计</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_BOOKING_COURSEBOOKING"
				pageType="list"></gh:resAuth>
			<table id="statBookOrderTate" class="table" layouth="161">
				<thead>
					<tr>
						<th width="20%">教学站</th>
						<%-- <th  width="10%" >专业层次</th>
					<th  width="15%" >专业名称</th> 
					<th  width="10%">年级</th>--%>
						<th width="25%">课程名称</th>
						<th width="25%">教材名称</th>
						<th width="15%">作者</th>
						<th width="15%">预约人数</th>

					</tr>
				</thead>
				<tbody>
					<c:set var="total" value="0"></c:set>
					<c:forEach items="${statBookOrderPage.result }" var="statBookOrder">
						<tr>
							<td>${statBookOrder.UNITNAME }</td>
							<%-- <td>${statBookOrder.CLASSICNAME }</td>
			  		 	<td>${statBookOrder.MAJORNAME }</td>
			  			<td>${statBookOrder.GRADENAME }</td> --%>
							<td>${statBookOrder.COURSENAME }</td>
							<td>${statBookOrder.BOOKNAME }</td>
							<td>${statBookOrder.AUTHOR }</td>
							<td>${statBookOrder.COUNTS }</td>
						</tr>
						<%-- <c:set var="total" value="${statBookOrder.COUNTS + total }"></c:set> --%>
					</c:forEach>
					<%-- 
			  <tr>
			  	<td colspan="6"></td>
			  	<td>合计：</td>
			  	<td >${total }</td>
			  </tr> --%>
				</tbody>
			</table>
			<gh:page page="${statBookOrderPage}"
				goPageUrl="${baseUrl }/edu3/teaching/courseorder/statBookOrder.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
 	//$(document).ready(function(){      
 	//	_w_table_rowspan("#statBookOrderTate",3);    
 	 //   _w_table_rowspan("#statBookOrderTate",2);  
     //   _w_table_rowspan("#statBookOrderTate",1);   
    //});
	
	function groupData(flag){
		var recruitPlan =$("#recruitPlan").val();
		var classic ="";
		if($("#classic").length>0){
			 classic =$("#classic").val();
		}
		var isMatriculate ="";
		if($("#isMatriculate").length>0){
			isMatriculate =$("#isMatriculate").val()
		}
		var url = "${baseUrl }/edu3/recruit/matriculate/statMatriculate.html?groupBy="+flag;
		navTab.reload(url,$("#searchForm").serializeArray());
	}
</script>
</body>
</html>
