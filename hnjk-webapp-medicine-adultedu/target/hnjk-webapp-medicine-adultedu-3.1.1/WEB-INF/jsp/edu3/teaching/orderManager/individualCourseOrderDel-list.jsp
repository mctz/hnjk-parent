<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程预约管理</title>
</head>
<body>
	<div class="page">
		<div class="pageContent" layouth="94%">
			<gh:resAuth parentCode="RES_TEACHING_BOOKING_COURSE_LIST"
				pageType="courseOrderDel"></gh:resAuth>
			<div class="panel" defH="300">
				<div>
					<table class="list" width="100%">
						<thead>
							<!-- 学生学习计划 -->
							<tr style="color: #183152">
								<th width="5%" align="center"><input type="checkbox"
									name="checkall" id="del_course_check_all_box"
									onclick="checkboxAll('#del_course_check_all_box','resourceid','#individualCourseOrderDelInfoBody')" /></th>
								<th width="30%" align="center">课程名称</th>
								<th width="15%" align="center">课程性质</th>
								<th width="15%" align="center">学分</th>
								<th width="15%" align="center">建议学期</th>
								<th width="20%" align="center">学习状态</th>
							</tr>
						</thead>
						<tbody id="individualCourseOrderDelInfoBody">
							<c:forEach items="${learnPlan}" var="learnPlanCourses"
								varStatus="vs">
								<c:if test="${learnPlanCourses.status==1}">
									<tr>
										<td><input type="checkbox" name="resourceid"
											value="${learnPlanCourses.resourceid }" autocomplete="off" />
										</td>
										<td>${learnPlanCourses.teachingPlanCourse.course.courseName }</td>
										<td>${ghfn:dictCode2Val('CodeCourseType',learnPlanCourses.teachingPlanCourse.courseType) }</td>
										<td>${learnPlanCourses.teachingPlanCourse.creditHour }</td>
										<td>${ghfn:dictCode2Val('CodeTermType',learnPlanCourses.teachingPlanCourse.term ) }</td>
										<td><font color="green">预约学习</font></td>
									</tr>
								</c:if>
							</c:forEach>
						</tbody>
						<tfoot>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">	
	//删除课程预约
	function individualCourseOrderDel(){
		var ids = new Array();
		jQuery("#individualCourseOrderDelInfoBody input[name='resourceid']:checked").each(function(){
			ids.push(jQuery(this).val());
		})
		if(ids.length==0){
			alertMsg.warn("请选择要删除的预约!"); 
		}else{
			alertMsg.confirm("确认要删除所选的预约吗？",{
				okCall:function(){
					var url = "${baseUrl}/edu3/teaching/courseorder/del.html"
					jQuery.ajax({
						type:"post",
						url :url,
						data:"studentLearnPlanCourseIds="+ids.toString(),
						dataType:"json",
						success:function(resultDate){
							if(resultDate['errorMsg']!=null){
								alertMsg.warn(resultDate['errorMsg']);
							}else{
								var isSuccess = resultDate['isSuccess'];
								var msg = resultDate['msg'];
								if(msg!=null){
									alertMsg.warn(msg);
								}else{
									alertMsg.info("删除成功！");
								}
								if(isSuccess==true){
									$.pdialog.reload("${baseUrl}/edu3/teaching/courseorder/del-list.html?studentId="+resultDate['studentId']);
								}
							}
						}
					});
				}
			});
		}
	}
</script>
</body>
</html>