<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"
	import="com.hnjk.edu.recruit.*"
	import="com.hnjk.edu.recruit.service.impl.statMatriculate.*"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预约学习统计</title>
<script type="text/javascript">

//预约考试统计导出
function examOrderExport(flag){
	var yearInfoId = $("#sateExamorder_yearInfo").val();
	var term       = $("#sateExamorder_term").val();

	if(yearInfoId =="" || term == ""){
		alertMsg.warn("请选择年度和学期！");	
		return false;
	}
	if("total"==flag){
		var url ="${baseUrl}/edu3/teaching/examorder/export.html?flag="+flag+"&yearInfo="+yearInfoId+"&term="+term;	
		downloadFileByIframe(url,'examStatTotalExportIframe');
	}else{
		$.pdialog.open("${baseUrl}/edu3/teaching/examorder/export-condition.html?flag="+flag+"&yearInfo="+yearInfoId+"&term="+term,'RES_TEACHING_EXAM_ORDER_EXPORT_CONDITION','条件选择',{ width:800,height:600});
	}
}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="courseorder_searchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/courseorder/statExamOrder.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBranchSchool}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="sateExamorder_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:55%" /></li>
						</c:if>
						<li><label>课程：</label> <gh:courseAutocomplete name="course"
								tabindex="1" id="sateExamorder__ccId"
								value="${condition['course']}" displayType="code"
								style="width:55%"></gh:courseAutocomplete></li>
						<li><label>年度：</label> <gh:selectModel
								id="sateExamorder_yearInfo" name="yearInfo"
								bindValue="resourceid" displayValue="yearName" style="width:57%"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfo']}" orderBy="firstYear desc " /><font
							color="red" c>*</font></li>
						<li><label>学期：</label>
						<gh:select id="sateExamorder_term" name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm"
								style="width:57%" /><font color="red" c>*</font></li>


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
							id="sateExamorder_StartTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'sateExamorder_EndTime\')}'})"
							style="width: 55%" /></li>
						<li><label>结束时间：</label> <input type="text" name="endTime"
							value="${condition['endTime']}" class="Wdate"
							id="sateExamorder_EndTime"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'sateExamorder_StartTime\')}'})"
							style="width: 55%" /></li>
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
			<gh:resAuth parentCode="RES_TEACHING_BOOKING_EXAMORDER_STATISTICS"
				pageType="list"></gh:resAuth>
			<table id="statExamOrderTable" class="table" layouth="168">
				<thead>
					<tr>
						<th width="25%">教学站</th>
						<th width="25%">课程名称</th>
						<th width="10%">考试形式</th>
						<th width="10%">课程编号</th>
						<th width="20%">考试时间</th>
						<th width="10%">预约人数</th>

					</tr>
				</thead>
				<tbody>
					<c:set var="total" value="0"></c:set>
					<c:forEach items="${statCourseOrderPage.result }"
						var="statCourseOrder">
						<tr>
							<td>${statCourseOrder.UNITNAME }</td>
							<td>${statCourseOrder.COURSENAME }</td>
							<td>${statCourseOrder.EXAMTYPE }</td>
							<td>${statCourseOrder.EXAMCOURSECODE }</td>
							<td><fmt:formatDate value="${statCourseOrder.STARTTIME }"
									pattern="yyyy-MM-dd HH:mm" />- <fmt:formatDate
									value="${statCourseOrder.ENDTIME }" pattern="HH:mm" /></td>
							<td><a
								href="${baseUrl }/edu3/teaching/courseorder/courseOrder-details.html?status=2&courseId=${statCourseOrder.COURSEID }&branchSchool=${statCourseOrder.BRSCHOOLID }&startTime=${condition['startTime']}&endTime=${condition['endTime']}&yearInfo=${condition['yearInfo']}&term=${condition['term']}"
								target="dialog" width="800" height="600" title="预约考试明细">${statCourseOrder.COUNTS }</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${statCourseOrderPage}"
				goPageUrl="${baseUrl }/edu3/teaching/courseorder/statExamOrder.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">

	
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
		navTab.reload(url,$("#courseorder_searchForm").serializeArray());
	}
</script>
</body>
</html>
