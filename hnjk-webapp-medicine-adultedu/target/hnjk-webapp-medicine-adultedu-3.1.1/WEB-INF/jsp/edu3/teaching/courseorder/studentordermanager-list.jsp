<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生取消预约列表</title>


</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		
	})
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="studentOrderManagerSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/courseorder/student-order-manager.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>预约类型：</label> <select name="operateType">
								<option value="orderCourse"
									<c:if test="${operateType eq 'orderCourse' }"> selected="selected" </c:if>>预约学习</option>
								<option value="orderExam"
									<c:if test="${operateType eq 'orderExam' }"> selected="selected" </c:if>>预约考试</option>
								<option value="orderGraduatePaper"
									<c:if test="${operateType eq 'orderGraduatePaper' }"> selected="selected" </c:if>>预约毕业论文</option>
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
					<font color="red"> 提示：取消预约操作成功后，请按F5刷新页面查看课程的最新状态</font>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="85" width="100%">
				<thead>
					<tr>
						<th width="5%">编号</th>
						<th width="40%">课程名称</th>
						<th width="10%">课程性质</th>
						<th width="10%">学分</th>
						<th width="10%">建议学期</th>
						<th width="10%">预约状态</th>
						<th width="10%">操作</th>
					</tr>
				</thead>

				<tbody id="studentCourseOrderMangerListBody">
					<c:forEach items="${studentLearnPlanList}" var="learnPlan"
						varStatus="vs">
						<tr>
							<td>${vs.index+1 }</td>
							<td><c:choose>
									<c:when test="${not empty learnPlan.teachingPlanCourse }">
		            			${learnPlan.teachingPlanCourse.course.courseName }
		            		</c:when>
									<c:when test="${not empty learnPlan.planoutCourse }">
		            			${learnPlan.planoutCourse.courseName }
		            		</c:when>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${not empty learnPlan.teachingPlanCourse }">
		            			${ghfn:dictCode2Val('CodeCourseType',learnPlan.teachingPlanCourse.courseType)}
		            		</c:when>
									<c:when test="${not empty learnPlan.planoutCourse }">
		            			计划外课程
		            		</c:when>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${not empty learnPlan.teachingPlanCourse }">
		            			 ${learnPlan.teachingPlanCourse.creditHour }
		            		</c:when>
									<c:when test="${not empty learnPlan.planoutCourse }">
		            			 ${learnPlan.planoutCourse.planoutCreditHour }
		            		</c:when>
								</c:choose></td>
							<td>${ghfn:dictCode2Val('CodeTermType',learnPlan.teachingPlanCourse.term)}</td>
							<td><c:choose>
									<c:when test="${learnPlan.status eq 1}">预约学习</c:when>
									<c:when test="${learnPlan.status eq 2}">预约考试</c:when>
								</c:choose></td>
							<td><a href="javaScript:void(0)"
								onclick="delOrderForStu('${learnPlan.resourceid}')"> 取消预约</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</div>

	</div>
	<script type="text/javascript">

	//学生取消预约
	function delOrderForStu(learnPlanId){
		var url = "${baseUrl}/edu3/teaching/courseorder/student-order-del.html?operateType=${operateType}&stuLearnPlanId="+learnPlanId;
		alertMsg.confirm("确定要取消所选的预约吗？", {
            okCall: function(){
            	$.ajax({
					type:"post",
					url:url,
					dataType:"json",
					success:function(data){
						var isSuccess   = data['isSuccess'];
						var operateType = data['operateType'];
						var msgList     = data['msg'];
						var msg         = "";
						if(true===isSuccess){
							var reloadUrl = "${baseUrl}/edu3/teaching/courseorder/student-order-manager.html?operateType"+operateType;
							 navTab.reload(reloadUrl,$("#studentOrderManagerSearchForm").serializeArray());
						}else{
							for(i=0;i<msgList.length;i++){
								msg +="<strong>"+(i+1)+"、</strong>"+msgList[i]+'\n';
							}
							alertMsg.warn(msg);
						}
					}
				});
			}
		});

	}
</script>
</body>
</html>