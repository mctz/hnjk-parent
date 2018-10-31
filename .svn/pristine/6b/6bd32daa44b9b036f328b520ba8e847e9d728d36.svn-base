<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预约学习补录</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="searchForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/examorder/individualOrder-list.html"
				method="post">
				<input id="teachingPlanId" name="teachingPlanId" type="hidden" />
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${condition['isBrschool'] !=true}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="courseorder_eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:50%" /></li>
						</c:if>
						<li><label>年级：</label>
						<gh:selectModel id="gradeid" name="gradeid" bindValue="resourceid"
								displayValue="gradeName" style="width:50%"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}" orderBy="gradeName desc" /></li>
						<li><label>专业：</label>
						<gh:selectModel id="major" name="major" bindValue="resourceid"
								displayValue="majorCodeName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major" style="width:50%"
								orderBy="majorCode" /></li>
						<li><label>层次：</label>
						<gh:selectModel id="classic" name="classic" bindValue="resourceid"
								displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" style="width:50%" /></li>
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 50%" /></li>
						<li><label>学号：</label><input type="text"
							name="matriculateNoticeNo"
							value="${condition['matriculateNoticeNo']}" style="width: 50%" />
						</li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_BOOKING_COURSE_LIST"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_info"
							onclick="checkboxAll('#check_all_info','resourceid','#infoBody')" /></th>
						<th width="15%">姓名</th>
						<th width="20%">学号</th>
						<th width="10%">年级</th>
						<th width="10%">培养层次</th>
						<th width="20%">专业</th>
						<th width="20%">教学站</th>
					</tr>
				</thead>

				<tbody id="infoBody">
					<c:forEach items="${page.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" autocomplete="off" /></td>
							<td><a href="#" onclick="viewStuInfo2('${stu.resourceid}')"
								title="点击查看">${stu.studentBaseInfo.name }</a></td>
							<td>${stu.studyNo}</td>
							<td>${stu.grade.gradeName}</td>
							<td>${stu.classic.classicName }</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.branchSchool}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/examorder/individualOrder-list.html"
				pageType="sys" condition="${condition}" />
		</div>

	</div>
	<script type="text/javascript">

	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		$.pdialog.open(url+'?act=view&resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});
	}
	function individualCourseManager(type){
		if("addCourseOrder" == type){      //新增预约
			if(isCheckOnlyone('resourceid','#infoBody')){
				var url = "${baseUrl}/edu3/teaching/courseorder/makeup-form.html";			
				$.pdialog.open(url+'?studentId='+$("#infoBody input[name='resourceid']:checked").val(), 'RES_TEACHING_EXAM_ORDER_ADD', '补预约', {width: 800, height:600});
			}
		}else if("delCourseOrder" == type){//删除课程预约
			if(isCheckOnlyone('resourceid','#infoBody')){
				var url = "${baseUrl}/edu3/teaching/courseorder/del-list.html";
				$.pdialog.open(url+'?studentId='+$("#infoBody input[name='resourceid']:checked").val(), 'RES_TEACHING_COURSE_ORDER_DELLIST', '课程预约管理', {width: 800, height: 400});
			}
		}else if("delExamOrder"==type){//删除考试预约
			if(isCheckOnlyone('resourceid','#infoBody')){
				var url = "${baseUrl}/edu3/teaching/examorder/del-list.html";
				$.pdialog.open(url+'?studentId='+$("#infoBody input[name='resourceid']:checked").val(), 'RES_TEACHING_EXAM_ORDER_DELLIST', '考试预约管理', {width: 800, height: 400});
			}
		}	
	}
</script>
</body>
</html>