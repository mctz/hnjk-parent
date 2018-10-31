<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预约考试统计导出—条件选择</title>


</head>
<body>
	<div class="page">
		<div class="pageContent">

			<input type="hidden" id="examOrderStatExportCondition_yearInfo"
				name="yearInfo" value="${condition['yearInfo']}" /> <input
				type="hidden" id="examOrderStatExportCondition_term" name="term"
				value="${condition['term']}" /> <input type="hidden"
				id="examOrderStatExportCondition_flag" name="flag"
				value="${condition['flag']}" />

			<table class="form" id="export_StatExamOrder_Condition_Table">
				<c:choose>
					<c:when test="${condition['flag'] eq 'time' }">
						<tr>
							<td>考试时间：</td>
							<td><select id="export_statexamOrder_examTimeSegment"
								name="examSub_examTimeSegment" style="width: 55%">
									<option value="">::::::请选择::::::</option>
									<c:forEach items="${timeSegmentList }" var="segment">
										<c:set var="tempSegment"
											value="${segment.STARTTIME }TO${segment.ENDTIME }"></c:set>
										<option value="${segment.STARTTIME }TO${segment.ENDTIME }"
											<c:if test="${tempSegment eq  condition['examSub_examTimeSegment']}"> selected="selected"</c:if>>${segment.STARTTIME }至${segment.SHORTENDTIME }</option>
									</c:forEach>
							</select></td>
						</tr>
					</c:when>
					<c:when test="${condition['flag'] eq 'unit' }">
						<tr>
							<td>教学站：</td>
							<td><select id="export_StatExamOrder_Condition_BranchSchool"
								name="export_StatExamOrder_Condition_BranchSchool"
								multiple='multiple' size="10">
									<c:forEach items="${brSchoolList }" var="brSchool">
										<option value="${brSchool.resourceid }">${brSchool.unitCode}-${brSchool.unitName }</option>
									</c:forEach>
							</select></td>
						</tr>
					</c:when>
					<c:when test="${condition['flag'] eq 'course' }">
						<tr>
							<td>课程：</td>
							<td><select id="export_StatExamOrder_Condition_Course"
								name="export_StatExamOrder_Condition_Course" multiple='multiple'
								size="10">
									<c:forEach items="${condition['examSub'].examInfo }"
										var="examInfo">
										<option value="${examInfo.course.resourceid }">${examInfo.examCourseCode}-${examInfo.course.courseName }</option>
									</c:forEach>
							</select></td>
						</tr>
					</c:when>
				</c:choose>
				<tr>
					<td>导出数据：</td>
					<td><select id="export_StatExamOrder_Condition_ismachineexam"
						name="ismachineexam">
							<option value="">请选择</option>
							<option value="Y">机考</option>
							<option value="N">笔试</option>
					</select> (选择此项后只导出所选条件下对应的统计数据)</td>
				</tr>
			</table>
			<div>
				<ul>
					<li>
						<div class="buttonActive">
							<div class="buttonContent">
								<button id="plansubmit" type="button"
									onclick="executeExamOrderExport();">导出</button>
							</div>
						</div>
					</li>
				</ul>
			</div>

		</div>
	</div>

	<script type="text/javascript">
	
	$(document).ready(function(){
		var flag = "${condition['flag']}";
		if("course"==flag){
			$('#export_StatExamOrder_Condition_Course').multiselect2side({
				selectedPosition: 'right',
				moveOptions: false,
				labelsx: '',
				labeldx: ''
			});
			
		}
		if("unit"==flag){
			$('#export_StatExamOrder_Condition_BranchSchool').multiselect2side({
				selectedPosition: 'right',
				moveOptions: false,
				labelsx: '',
				labeldx: ''
			});
		}
		
	})
	//导出
	function executeExamOrderExport(){
		
		var flag 		= "${condition['flag']}";
		var brSchool    = "";
		var course      = "";
		var yearInfo    = $("#examOrderStatExportCondition_yearInfo").val();
		var term        = $("#examOrderStatExportCondition_term").val();
		var examTimeSegment = $("#export_statexamOrder_examTimeSegment").val();
		var ismachineexam = $("#export_StatExamOrder_Condition_ismachineexam option:checked").val();
		
		if("course"==flag){
			course    = jQuery("#export_StatExamOrder_Condition_Table #export_StatExamOrder_Condition_Coursems2side__dx").val();
		}
		if("unit"==flag){
			brSchool    = jQuery("#export_StatExamOrder_Condition_Table #export_StatExamOrder_Condition_BranchSchoolms2side__dx").val();
		}
		
		if(null==yearInfo || ""==yearInfo){
			alertMsg.warn("未选择要统计的年度！");
			return false;
		}
		
		if(null==term || ""==term){
			alertMsg.warn("未选择要统计的学期！");
			return false;
		}
		
		if(null==flag){
			alertMsg.warn("请选择按教学站、课程、时间段导出！");
			return false;
		}
		
		$.pdialog.closeCurrent();
		var url = "${baseUrl}/edu3/teaching/examorder/export.html?flag="+flag+"&yearInfo="+yearInfo+"&term="+term+"&courseId="+course+"&branchSchool="+brSchool+"&examTimeSegment="+examTimeSegment+"&ismachineexam="+ismachineexam;
		downloadFileByIframe(url,'examStatExportIframe');
	}
	
</script>
</body>
</html>