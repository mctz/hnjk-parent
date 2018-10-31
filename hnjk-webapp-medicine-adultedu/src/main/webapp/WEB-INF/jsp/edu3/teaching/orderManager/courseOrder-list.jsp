<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程预约列表</title>


</head>
<body>
	<script type="text/javascript">
	/**
	jQuery(document).ready(function(){      
		var flag = "${condition['searchOrderFlag']}";

		//设置为查询未预约URL 页面中FORM元素中的ACTION属性默认是查询已预约列表的URL，当查询未预约列表时把URL改成未预约列表的URL
		if("N" ==flag){
	
			var url = "${baseUrl}/edu3/teaching/not-courseorder-list.html";
			$("#courseOrderSearchForm").attr("action",url);	
		}
	});
	
	//设置查询FORM的action属性
	function changeURL(obj){
		
		//设置为查询已预约URL
		if("Y" ==$(obj).val()){
			
			var url = "${baseUrl}/edu3/teaching/courseorder-list.html";
			$("#courseOrderSearchForm").attr("action",url);
		}
		//设置为查询未预约URL
		if("N" ==$(obj).val()){
	
			var url = "${baseUrl}/edu3/teaching/not-courseorder-list.html";
			$("#courseOrderSearchForm").attr("action",url);	
		}
	}*/
	//给学生发送消息
	function sendMsgForCourseOrder(){
		var flag = "${condition['searchOrderFlag']}";
		if("N" ==flag){
			if(!isChecked("resourceid","#notCourseOrderListBody")){
	 			alertMsg.warn('请选择一条要操作记录！');
				return false;
		 	}
			alertMsg.confirm("您确定要给这些学生发消息?", {
					okCall: function(){		
						var res  = "";
						var name = "";
						$("#notCourseOrderListBody input[name='resourceid']:checked").each(function(){
		                        res +=$(this).val()+",";
		                        name+=$(this).attr('rel')+",";
		                 });
						$.pdialog.open('${baseUrl }/edu3/framework/message/dailog.html?type=batch&userNames='+res+'&cnNames='+encodeURIComponent(name),'selector','发送消息',{mask:true,width:750,height:550});
					}
			});			
		}else{
			alertMsg.info("发送消息功能针对未预约学习学生！");
		}
	}
	//查看学生信息
	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		$.pdialog.open(url+'?act=view&resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800,height: 600});
	}

</script>
	<div class="page">
		<div class="pageHeader">
			<form id="courseOrderSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/courseorder-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBrschool}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="courseorder_eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:55%" /></li>
						</c:if>
						<li><label>年度：</label> <gh:selectModel id="yearInfo"
								name="yearInfo" bindValue="resourceid" displayValue="yearName"
								style="width:57%"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								orderBy="yearName desc" value="${condition['yearInfo']}" /></li>
						<li><label>学期：</label> <gh:select name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm"
								style="width:57%" /></li>
						<li><label>预约情况：</label> <select name="searchOrderFlag"
							style="width: 57%">
								<option value="Y"
									<c:if test="${condition['searchOrderFlag'] eq 'Y'}">  selected="selected"</c:if>>已预约</option>
								<option value="N"
									<c:if test="${condition['searchOrderFlag'] eq 'N'}">  selected="selected"</c:if>>未预约</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<li><label>年级：</label>
						<gh:selectModel name="gradeid" bindValue="resourceid"
								displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}" orderBy="gradeName desc"
								style="width:57%" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" style="width:57%" /></li>
						<li><label>专业：</label>
						<gh:selectModel name="major" bindValue="resourceid"
								displayValue="majorCodeName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								orderBy="majorCode" style="width:57%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>课程:</label> <gh:courseAutocomplete name="courseId"
								tabindex="1" id="courseId" value="${condition['courseId'] }"
								displayType="code" style="width:55%" /></li>
						<li><label>姓名：</label><input type="text" id="name"
							name="name" value="${condition['name']}" style="width: 55%" /></li>
						<li><label>学号：</label><input type="text" id="studyNo"
							name="studyNo" value="${condition['studyNo']}" style="width: 55%" />
						</li>
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
		<div class="pageContent">

			<c:choose>
				<%-- --------------------------------------------已预约课程列表-------------------------------------------- --%>
				<c:when
					test="${empty condition['searchOrderFlag'] or condition['searchOrderFlag'] eq 'Y'}">
					<table class="table" layouth="161">
						<thead>
							<tr>
								<th width="5%"><input type="checkbox" name="checkall"
									id="check_all_course_order"
									onclick="checkboxAll('#check_all_course_order','resourceid','#courseOrderListBody')" /></th>
								<th width="10%">姓名</th>
								<th width="30%">课程名称</th>
								<th width="10%">课程性质</th>
								<th width="10%">学时</th>
								<th width="10%">学分</th>
								<th width="15%">预约年度</th>
								<th width="10%">学期</th>
							</tr>
						</thead>
						<tbody id="courseOrderListBody">
							<c:forEach items="${page.result}" var="studentLearnPlan"
								varStatus="vs">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${studentLearnPlan.resourceid }" autocomplete="off" /></td>
									<td><a href="#"
										onclick="viewStuInfo2('${studentLearnPlan.studentInfo.resourceid}')"
										title="点击查看">${studentLearnPlan.studentInfo.studentName }</a></td>
									<td>${studentLearnPlan.teachingPlanCourse.course.courseName}</td>
									<td>${ghfn:dictCode2Val('CodeCourseType',studentLearnPlan.teachingPlanCourse.courseType) }</td>
									<td>${studentLearnPlan.teachingPlanCourse.stydyHour}</td>
									<td>${studentLearnPlan.teachingPlanCourse.creditHour}</td>
									<td>${studentLearnPlan.yearInfo.yearName }</td>
									<td>${ghfn:dictCode2Val('CodeTermType',studentLearnPlan.term ) }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<gh:page page="${page}"
						goPageUrl="${baseUrl }/edu3/teaching/courseorder-list.html"
						pageType="sys" condition="${condition}" />
				</c:when>
				<%-- --------------------------------------------未预约课程列表-------------------------------------------- --%>
				<c:when test="${condition['searchOrderFlag'] eq 'N'}">
					<gh:resAuth parentCode="RES_TEACHING_BOOKING_COURSEORDER_LIST"
						pageType="list"></gh:resAuth>
					<table class="table" layouth="191">
						<thead>
							<tr>
								<th width="5%"><input type="checkbox" name="checkall"
									id="check_all_not_course_order"
									onclick="checkboxAll('#check_all_not_course_order','resourceid','#notCourseOrderListBody')" /></th>
								<th width="10%">姓名</th>
								<th width="30%">联系电话</th>
								<th width="10%">电子邮件</th>
								<th width="10%">年级</th>
								<th width="10%">层次</th>
								<th width="15%">专业</th>
								<th width="10%">教学站</th>
							</tr>
						</thead>

						<tbody id="notCourseOrderListBody">
							<c:forEach items="${page.result}" var="studentInfo"
								varStatus="vs">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${studentInfo.USERNAME}" autocomplete="off"
										rel="${studentInfo.NAME }" /></td>
									<td><a href="#"
										onclick="viewStuInfo2('${studentInfo.RESOURCEID}')"
										title="点击查看">${studentInfo.NAME }</a></td>
									<td>${studentInfo.TEMPCONTACTPHONE}</td>
									<td>${studentInfo.EMAIL }</td>
									<td>${studentInfo.GRADENAME}</td>
									<td>${studentInfo.CLASSICNAME}</td>
									<td>${studentInfo.MAJORNAME}</td>
									<td>${studentInfo.UNITNAME }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<gh:page page="${page}"
						goPageUrl="${baseUrl }/edu3/teaching/courseorder-list.html"
						pageType="sys" condition="${condition}" />
				</c:when>

			</c:choose>
		</div>
	</div>

</body>
</html>