<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<style>
* {
	font-size: 12px;
}

.type {
	text-align: center;
	line-height: 28px;
}
</style>
<script type="text/javascript">
		$(document).ready(function(){
			window.setTimeout(function(){				
				var existIds = $("#${condition['usersN']}").val();
				$("#check_all_msg_studentSelectorBody input[name=resourceid]").each(function(){
					if($(this).val()!="" && existIds.indexOf($(this).val())>=0){
						$(this).attr("checked",true);
					}
				});
			},500);
			
		});
		
		function msgStudentClickThis(obj){
			if(obj.value!=""){
				if(obj.checked){				
					if($("#${condition['usersN']}").val().indexOf(obj.value)<0){
						$("#${condition['usersN']}").val($("#${condition['usersN']}").val()+obj.value+",");
						$("#${condition['namesN']}").val($("#${condition['namesN']}").val()+$(obj).attr('rel')+",");
					}
				}else{
					if($("#${condition['usersN']}").val().indexOf(obj.value)>=0){
						var ids = $("#${condition['usersN']}").val().replace((obj.value+","),"");
						var names = $("#${condition['namesN']}").val().replace(($(obj).attr('rel')+","),"");
						$("#${condition['usersN']}").val(ids);
						$("#${condition['namesN']}").val(names);
					}
				}
			}			
		}	
		
		function msgStudentCheckboxAll(obj){			
			$("#check_all_msg_studentSelectorBody input[name='resourceid']").each(function(){
				$(this).attr("checked",$(obj).attr("checked"));	
				msgStudentClickThis(this);		
			});
		}		

	</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/portal/message/student.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 115px" /> <input
							type="hidden" name="usersN" value="${condition['usersN']}" /> <input
							type="hidden" name="namesN" value="${condition['namesN']}" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 115px" /></li>
						<li><label>课程：</label> <gh:courseAutocomplete name="courseId"
								tabindex="2" id="msg_selector_courseId" isFilterTeacher="Y"
								value="${condition['courseId']}" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>教学站：</label> <gh:brSchoolAutocomplete
								name="branchSchool" tabindex="2" id="msg_student_branchSchool"
								defaultValue="${condition['branchSchool']}" style="width:120px"></gh:brSchoolAutocomplete>
						</li>
						<li><label>专业：</label>
						<gh:selectModel name="major" bindValue="resourceid"
								displayValue="majorName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:120px" /></li>
						<li>
							<%--
						<label>学习层次：</label>
								<gh:selectModel name="classic" bindValue="resourceid" displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic" style="width:120px"/> --%>
							<label>预约状态：</label> <select name="status">
								<option value="1"
									<c:if test="${condition['status'] eq 1}">selected="selected"</c:if>>预约学习</option>
								<option value="2"
									<c:if test="${condition['status'] eq 2}">selected="selected"</c:if>>预约考试</option>
								<option value="3"
									<c:if test="${condition['status'] eq 3}">selected="selected"</c:if>>考试结束</option>
						</select>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><div class="button">
									<div class="buttonContent">
										<button type="button" class="close">确 定</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="">
			<table class="table" layouth="168">
				<thead>
					<tr>
						<th width="10%"><input type="checkbox" name="checkall"
							onclick="msgStudentCheckboxAll(this)" /></th>
						<th width="16%">姓名</th>
						<th width="14%">学号</th>
						<th width="10%">年级</th>
						<th width="10%">培养层次</th>
						<th width="15%">专业</th>
						<th width="10%">教学站</th>
						<th width="15%">课程</th>
					</tr>
				</thead>
				<tbody id="check_all_msg_studentSelectorBody">
					<c:forEach items="${studentlist.result}" var="plan" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${plan.studentInfo.sysUser.username }"
								rel="${plan.studentInfo.sysUser.cnName }"
								onclick="msgStudentClickThis(this)" /></td>
							<td>${plan.studentInfo.studentName}</td>
							<td>${plan.studentInfo.studyNo}</td>
							<td>${plan.studentInfo.grade.gradeName}</td>
							<td>${plan.studentInfo.classic.classicName }</td>
							<td>${plan.studentInfo.major.majorName }</td>
							<td>${plan.studentInfo.branchSchool }</td>
							<td>${plan.teachingPlanCourse.course.courseName}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${studentlist}"
				goPageUrl="${baseUrl }/edu3/portal/message/student.html"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>
</body>
</html>