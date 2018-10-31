<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预约考试列表</title>
<script type="text/javascript">
	$(document).ready(function(){
		examOrderListQueryBegin();
	});
	
	//打开页面或者点击查询（即加载页面执行）
	function examOrderListQueryBegin() {
		var defaultValue = "${condition['branchSchool']}";
		var schoolId = "";
		var isBrschool = "${isBrschool}";
		if(isBrschool==true || isBrschool=="true"){
			schoolId = defaultValue;
		}
		var gradeId = "${condition['gradeid']}";
		var classicId = "${condition['classic']}";
		var teachingType = "${condition['name~teachingType']}";
		var majorId = "${condition['major']}";
		var classesId = "${condition['name~classes']}";
		var selectIdsJson = "{unitId:'examorder_eiinfo_brSchoolName',gradeId:'gradeid',classicId:'classic',teachingType:'id~teachingType',majorId:'majorid',classesId:'id~classes'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function examOrderListQueryUnit() {
		var defaultValue = $("#examorder_eiinfo_brSchoolName").val();
		var selectIdsJson = "{gradeId:'gradeid',classicId:'classic',teachingType:'id~teachingType',majorId:'majorid',classesId:'id~classes'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function examOrderListQueryGrade() {
		var defaultValue = $("#examorder_eiinfo_brSchoolName").val();
		var gradeId = $("#gradeid").val();
		var selectIdsJson = "{classicId:'classic',teachingType:'id~teachingType',majorId:'majorid',classesId:'id~classes'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function examOrderListQueryClassic() {
		var defaultValue = $("#examorder_eiinfo_brSchoolName").val();
		var gradeId = $("#gradeid").val();
		var classicId = $("#classic").val();
		var selectIdsJson = "{teachingType:'id~teachingType',majorId:'majorid',classesId:'id~classes'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

</script>

</head>
<body>
	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form id="searchForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/examorder-list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label> <gh:selectModel id="yearInfo"
								name="yearInfo" bindValue="resourceid" displayValue="yearName"
								style="width:120px;"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								orderBy="yearName desc" value="${condition['yearInfo']}" /></li>
						<li><label>学期：</label> <gh:select name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm"
								style="width:120px;" /></li>
						<%-- <c:if test="${!isBrschool}"> --%>
							<li class="custom-li"><label>教学站：</label> <span
								sel-id="examorder_eiinfo_brSchoolName" sel-name="branchSchool"
								sel-onchange="examOrderListQueryUnit()" sel-classs="flexselect"
								></span></li>
						<%-- </c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="branchSchool"
								id="examorder_eiinfo_brSchoolName"
								value="${condition['branchSchool']}" />
						</c:if> --%>

						<li><label>预约情况：</label> <select name="searchOrderFlag"
							style="width: 100px;">
								<option value="Y"
									<c:if test="${condition['searchOrderFlag'] eq 'Y'}">  selected="selected"</c:if>>已预约</option>
								<option value="N"
									<c:if test="${condition['searchOrderFlag'] eq 'N'}">  selected="selected"</c:if>>未预约</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<li><label>年级：</label> <span sel-id="gradeid"
							sel-name="gradeid" sel-onchange="examOrderListQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="classic"
							sel-name="classic" sel-onchange="examOrderListQueryClassic()"
							sel-style="width: 120px"></span></li>
						<li class="custom-li"><label>专业：</label> <span sel-id="majorid"
							sel-name="major" sel-classs="flexselect"></span>
						</li>
						<li><label>考试时间：</label><input type="text" id="examTime"
							name="examTime" value="${condition['examTime']}"
							readonly="readonly" onFocus="WdatePicker({isShowWeek:true})"
							style="width: 100px;" /></li>
					</ul>
					<ul class="searchContent">
						
						<li><label>姓名：</label><input type="text" id="name"
							name="name" value="${condition['name']}" style="width: 55%" /></li>
						<li><label>学号：</label><input type="text" id="studyNo"
							name="studyNo" value="${condition['studyNo']}" style="width: 55%" />
						</li>
						<li class="custom-li"><label>课程名称：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1" id="examOrder_courseId"
								value="${condition['courseId'] }" displayType="code"
								style="width:240px;" /> <%-- <input type="text" id="courseName" name="courseName" value="${condition['courseName']}" style="width:55%"/>--%>
						</li>
						<div class="buttonActive" style="float: right;">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>

					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="135">
				<thead>
					<c:choose>
						<c:when
							test="${empty condition['searchOrderFlag'] or condition['searchOrderFlag'] eq 'Y'}">
							<tr>
								<th width="5%"><input type="checkbox" name="checkall"
									id="check_all_info"
									onclick="checkboxAll('#check_all_info','resourceid','#examDelayBody')" /></th>
								<th width="10%">姓名</th>
								<th width="20%">课程名称</th>
								<th width="25%">试室</th>
								<th width="10%">座位号</th>
								<th width="20%">考试时间</th>
								<th width="5%">选考次数</th>
								<th width="5%">是否缓考</th>
							</tr>
						</c:when>
						<c:when test="${condition['searchOrderFlag'] eq 'N'}">
							<tr>
								<th width="5%"><input type="checkbox" name="checkall"
									id="check_all_info"
									onclick="checkboxAll('#check_all_info','resourceid','#examDelayBody')" /></th>
								<th width="10%">姓名</th>
								<th width="20%">课程名称</th>

								<th width="10%">年级</th>
								<th width="10%">层次</th>
								<th width="10%">专业</th>
								<th width="15%">教学站</th>
								<th width="10%">学籍状态</th>
								<th width="10%">学号</th>
							</tr>
						</c:when>
					</c:choose>

				</thead>

				<tbody id="examDelayBody">
					<c:forEach items="${page.result}" var="plan" varStatus="vs">
						<c:choose>
							<%--  --------------------------------------------预约考试列表-------------------------------------------- --%>
							<c:when
								test="${empty condition['searchOrderFlag'] or condition['searchOrderFlag'] eq 'Y'}">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${plan.resourceid }" autocomplete="off" /></td>
									<td><a href="#"
										onclick="viewStuInfo2('${plan.studentInfo.resourceid}')"
										title="点击查看">${plan.studentInfo.studentName }</a></td>
									<td>${plan.examResults.course.courseName}</td>
									<td>${plan.examResults.examroom.examroomName}</td>
									<td>${plan.examResults.examSeatNum}</td>
									<td><fmt:formatDate
											value="${plan.examInfo.examStartTime}"
											pattern="yyyy-MM-dd HH:mm:ss" type="date" /> - <fmt:formatDate
											value="${plan.examInfo.examEndTime}" pattern="HH:mm:ss"
											type="date" /></td>
									<td>${plan.examResults.examCount }</td>
									<td>${ghfn:dictCode2Val('yesOrNo',plan.examResults.isDelayExam ) }</td>
								</tr>
							</c:when>
							<%--  --------------------------------------------未预约考试列表-------------------------------------------- --%>
							<c:when test="${condition['searchOrderFlag'] eq 'N'}">
								<tr>
									<td><input type="checkbox" name="resourceid"
										value="${plan.resourceid }" autocomplete="off" /></td>
									<td><a href="#"
										onclick="viewStuInfo2('${plan.studentInfo.resourceid}')"
										title="点击查看">${plan.studentInfo.studentName }</a></td>
									<td><c:choose>
											<c:when test="${ not empty plan.teachingPlanCourse}">
				            			${plan.teachingPlanCourse.course.courseName}
				            		</c:when>
											<c:when test="${ not empty plan.planoutCourse}">
				            			${plan.planoutCourse.courseName}
				            		</c:when>
										</c:choose></td>

									<td>${plan.studentInfo.grade.gradeName }</td>
									<td>${plan.studentInfo.classic.classicName }</td>
									<td title="${plan.studentInfo.major.majorName}">${plan.studentInfo.major.majorName}</td>
									<td title="${plan.studentInfo.branchSchool.unitName}">${plan.studentInfo.branchSchool.unitName}</td>
									<td>
										${ghfn:dictCode2Val('CodeStudentStatus',plan.studentInfo.studentStatus) }
									</td>
									<td>${plan.studentInfo.studyNo}</td>
								</tr>
							</c:when>
						</c:choose>

					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/examorder-list.html"
				pageType="sys" condition="${condition}" />
		</div>

	</div>
	<script type="text/javascript">
	
	//查看学生信息
	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		$.pdialog.open(url+'?act=view&resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});
	}

</script>
</body>
</html>