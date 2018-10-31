<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试卷袋标签统计</title>
</head>
<body>
	<script type="text/javascript">

//按课程打印试卷袋标签
function printExamPaperBag(){
	var examSubId = "${condition['examSubId']}";
	var examInfoIds = new Array();
	$("#examPaperBagPrintBycourseListBody input[name='resourceid']:checked").each(function(){
		examInfoIds.push(jQuery(this).val());
	});
	if(examInfoIds.length==0){
		alertMsg.warn("请选择要打印的课程！")
		return false;
	}
	alertMsg.confirm("确定打印所选课程的试卷袋标签吗？",{
		okCall:function(){
			var url = "${baseUrl}/edu3/teaching/exam/paperbag/print-view.html?flag=printByCourse&examInfoIds="+examInfoIds.toString()+"&examSubId="+examSubId;
			
			$.pdialog.open(url,'RES_TEACHING_EXAM_PAPERBAG_PRINTBYCOURSE_PRINT','打印预览',{height:600, width:800,mask:true});
			
		}
	});
}
	
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="examPaperBagPrintByCourseSearchForm"
				onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/exam/paperbag/print-bycourse.html?examSubId=${condition['examSubId']}"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBrschool}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="printPaperBagByCourse_List_brSchoolId" displayType="code"
									defaultValue="${condition['branchSchool']}" style="width:53%" />
							</li>
						</c:if>

						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1"
								id="printPaperBagByCourse_List_courseId"
								value="${condition['courseId']}" displayType="code"
								style="width:55%" /></li>
						<li><label>考试形式：</label> <select name="isMachineExam"
							id="printPaperBagByCourse_List_isMachineExam" style="width: 55%">
								<option value="">请选择</option>
								<option value="Y"
									<c:if test="${condition['isMachineExam'] eq 'Y'}"> selected="selected" </c:if>>机考</option>
								<option value="N"
									<c:if test="${condition['isMachineExam'] eq 'N'}"> selected="selected" </c:if>>笔试</option>
						</select></li>
						<li><label>考试时间：</label> <select
							id="printPaperBagByCourse_List_examSub_examTimeSegment"
							name="examSub_examTimeSegment" style="width: 55%">
								<option value="">::::::请选择::::::</option>
								<c:forEach items="${timeSegmentList }" var="segment">
									<c:set var="tempSegment"
										value="${segment.STARTTIME }TO${segment.ENDTIME }"></c:set>
									<option value="${segment.STARTTIME }TO${segment.ENDTIME }"
										<c:if test="${tempSegment eq  condition['examSub_examTimeSegment']}"> selected="selected"</c:if>>${segment.STARTTIME }至${segment.SHORTENDTIME }</option>
								</c:forEach>
						</select></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>

			</form>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_EXAM_PAPER_LIST"
			pageType="subList"></gh:resAuth>
		<div class="pageContent">
			<table class="table" layouth="166" width="100%">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_examPaperBag_print_byCourse"
							onclick="checkboxAll('#check_all_examPaperBag_print_byCourse','resourceid','#examPaperBagPrintBycourseListBody')" /></th>
						<th width="8%">考试编号</th>
						<th width="35%">课程名称</th>
						<th width="8%">考试形式</th>
						<th width="20%">考试时间</th>
						<th width="8%">预约人数</th>
						<th width="8%">试卷份数</th>
						<th width="8%">包数</th>
					</tr>
				</thead>
				<tbody id="examPaperBagPrintBycourseListBody">
					<c:forEach items="${page.result}" var="examPaperBagPrintInfo"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${examPaperBagPrintInfo.RESOURCEID}" autocomplete="off" /></td>
							<td>${examPaperBagPrintInfo.EXAMCOURSECODE }</td>
							<td>${examPaperBagPrintInfo.COURSENAME }</td>
							<td>${examPaperBagPrintInfo.EXAMTYPE}</td>
							<td><fmt:formatDate
									value="${examPaperBagPrintInfo.EXAMSTARTTIME }"
									pattern="yyyy-MM-dd HH:mm" />- <fmt:formatDate
									value="${examPaperBagPrintInfo.EXAMENDTIME }" pattern="HH:mm" />
							</td>
							<td>${examPaperBagPrintInfo.ORDERNUM }</td>
							<td>${examPaperBagPrintInfo.PAPERNUM }</td>
							<td>${examPaperBagPrintInfo.BAGNUM }</td>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/exam/paperbag/print-bycourse.html?examSubId=${condition['examSubId']}"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>

</body>
</html>