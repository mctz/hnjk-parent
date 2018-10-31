<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	import="com.hnjk.edu.recruit.*"
	import="com.hnjk.edu.recruit.service.impl.statMatriculate.*"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预约学习统计</title>
<script type="text/javascript">

//预约学习统计导出
function exportCourseOrder(){
	var brSchool   = $("#satecourseorder_eiinfo_brSchoolName").val();
	var course     = $("#satecourseorder_ccId").val();
	var yearInfoId = $("#courseorder_search_form_yearInfo").val();
	var term       = $("#courseorder_search_form_term").val();
	var startTime  = $("#satecourseorder_StartTime").val();
	var endTime    = $("#satecourseorder_EndTime").val();
	
	if(yearInfoId =="" || term == ""){
		alertMsg.warn("请选择年度和学期！");	
		return false;
	}else {
		var url = "${baseUrl}/edu3/teaching/courseorder/statcourseorder/export.html?status=1&branchSchool="+brSchool+"&startTime="+startTime+"&endTime="+endTime+"&course="+course+"&yearInfo="+yearInfoId+"&term="+term;
		downloadFileByIframe(url,'courseOrderStatTotalExportIframe');
		//window.location.href="${baseUrl}/edu3/teaching/courseorder/statcourseorder/export.html?status=1"+$("#courseorder_searchForm").serialize();
	}
}

</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="courseorder_searchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/courseorder/statCourseOrder.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBranchSchool}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="satecourseorder_eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:55%" /></li>
						</c:if>
						<li><label>课程：</label> <gh:courseAutocomplete name="course"
								tabindex="1" id="satecourseorder_ccId"
								value="${condition['course']}" displayType="code"
								style="width:55%"></gh:courseAutocomplete></li>
						<li><label>年度：</label> <gh:selectModel
								id="courseorder_search_form_yearInfo" name="yearInfo"
								bindValue="resourceid" displayValue="yearName" style="width:57%"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfo']}" orderBy="firstYear desc " /></li>
						<li><label>学期：</label>
						<gh:select id="courseorder_search_form_term" name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm"
								style="width:57%" /></li>
					</ul>
					<%-- 
			<ul class="searchContent">

			<li >
					<label>年级：</label><gh:selectModel  name="gradeid" bindValue="resourceid" displayValue="gradeName" 
														modelClass="com.hnjk.edu.basedata.model.Grade" value="${condition['gradeid']}"
														orderBy="gradeName desc" style="width:57%"/>
				</li>
			 <li >             
		         <label> 层次：</label><gh:selectModel id="classic" name="classic" bindValue="resourceid" displayValue="classicName" style="width:57%"
									modelClass="com.hnjk.edu.basedata.model.Classic"  value="${condition['classic']}"  />
                 </li>
            <li >
				<label>专业：</label>
				<gh:selectModel name="major" bindValue="resourceid" displayValue="majorCodeName" value="${condition['major']}" style="width:57%"
							modelClass="com.hnjk.edu.basedata.model.Major"  orderBy="majorCode"  />
			
			</li>
							
			</ul>
			--%>
					<ul class="searchContent">

						<li><label>开始时间：</label> <input type="text" name="startTime"
							value="${condition['startTime']}" class="Wdate"
							id="satecourseorder_StartTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'satecourseorder_EndTime\')}'})"
							style="width: 55%" /></li>
						<li><label>结束时间：</label> <input type="text" name="endTime"
							value="${condition['endTime']}" class="Wdate"
							id="satecourseorder_EndTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'satecourseorder_StartTime\')}'})"
							style="width: 55%" /></li>
						<%-- 
			<li>
				<label>统计选项：</label>
				<gh:select  name="status" value="${condition['status']}" dictionaryCode="CodeCourseOrderStatus" style="width:55%" choose="N"/>
				
			</li>	
			<li><span class="tips">预约学习、考试统计</span></li>
			--%>
					</ul>

					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">统计</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">

			<gh:resAuth parentCode="RES_TEACHING_BOOKING_COURSEORDER_STAT"
				pageType="list"></gh:resAuth>
			<table id="statCourseOrderTable" class="table" layouth="188">
				<thead>
					<tr>
						<th width="30%">教学站</th>
						<th width="10%">专业层次</th>
						<th width="20%">专业名称</th>
						<th width="30%">课程名称</th>
						<th width="10%">预约人数</th>
					</tr>
				</thead>
				<tbody>
					<c:set var="total" value="0"></c:set>
					<c:forEach items="${statCourseOrderPage.result }"
						var="statCourseOrder">
						<tr>
							<td>${statCourseOrder.UNITNAME }</td>
							<td>${statCourseOrder.CLASSICNAME }</td>
							<td>${statCourseOrder.MAJORNAME }</td>
							<td>${statCourseOrder.COURSENAME }</td>
							<td><a
								href="${baseUrl }/edu3/teaching/courseorder/courseOrder-details.html?&flag=1&courseId=${statCourseOrder.COURSEID}&branchSchool=${statCourseOrder.UNITID }&startTime=${condition['startTime']}&endTime=${condition['endTime']}&yearInfo=${condition['yearInfo']}&term=${condition['term']}&classic=${statCourseOrder.CLASSICID }&major=${statCourseOrder.MAJORID }"
								target="dialog" width="800" height="600" title="预约明细">${statCourseOrder.COUNTS }</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${statCourseOrderPage}"
				goPageUrl="${baseUrl }/edu3/teaching/courseorder/statCourseOrder.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>
