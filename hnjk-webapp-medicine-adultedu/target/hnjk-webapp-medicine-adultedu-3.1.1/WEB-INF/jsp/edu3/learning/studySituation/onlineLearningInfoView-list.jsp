<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看在线学习情况</title>
<script type="text/javascript">
	$(document).ready(function(){
		onlineLearningInfoViewBegin();
	});
	
	// 打开页面或者点击查询（即加载页面执行）
	function onlineLearningInfoViewBegin() {
	   var defaultValue = "${condition['branchSchool']}";
	   var schoolId = "${unitId}";
	   var gradeId = "${condition['gradeId']}";
	   var classicId = "${condition['classicId']}";
	   var teachingType = "${condition['schoolType']}";
	   var majorId = "${condition['majorId']}";
	   var classesId = "${condition['classesId']}";
	   var selectIdsJson = "{unitId:'onlineLearningInfoView-brSchoolId',gradeId:'onlineLearningInfoView-gradeId',classicId:'onlineLearningInfoView-classicId',"
	 							  +"teachingType:'onlineLearningInfoView-teachingType',majorId:'onlineLearningInfoView-majorId',classesId:'onlineLearningInfoView-classesId'}";
	   cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId, teachingType, majorId, classesId, selectIdsJson);
	}
	// 选择教学点
	function onlineLearningInfoViewUnit() {
	   var defaultValue = $("#onlineLearningInfoView-brSchoolId").val();
	   var selectIdsJson = "{gradeId:'onlineLearningInfoView-gradeId',classicId:'onlineLearningInfoView-classicId',"
			  					  +"teachingType:'onlineLearningInfoView-teachingType',majorId:'onlineLearningInfoView-majorId',classesId:'onlineLearningInfoView-classesId'}";
	   cascadeQuery("unit", defaultValue, "", "", "", "", "", "", selectIdsJson);
	}
	// 选择年级
	function onlineLearningInfoViewGrade() {
	   var defaultValue = $("#onlineLearningInfoView-brSchoolId").val();
	   var gradeId = $("#onlineLearningInfoView-gradeId").val();
	   var selectIdsJson = "{classicId:'onlineLearningInfoView-classicId',teachingType:'onlineLearningInfoView-teachingType',majorId:'onlineLearningInfoView-majorId',"
		   						  +"classesId:'onlineLearningInfoView-classesId'}";
	   cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "", selectIdsJson);
	}
	// 选择层次
	function onlineLearningInfoViewClassic() {
	   var defaultValue = $("#onlineLearningInfoView-brSchoolId").val();
	   var gradeId = $("#onlineLearningInfoView-gradeId").val();
	   var classicId = $("#onlineLearningInfoView-classicId").val();
	   var selectIdsJson = "{teachingType:'onlineLearningInfoView-teachingType',majorId:'onlineLearningInfoView-majorId',classesId:'onlineLearningInfoView-classesId'}";
	   cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}
	// 选择学习形式
	function onlineLearningInfoViewTeachingType() {
	   var defaultValue = $("#onlineLearningInfoView-brSchoolId").val();
	   var gradeId = $("#onlineLearningInfoView-gradeId").val();
	   var classicId = $("#onlineLearningInfoView-classicId").val();
	   var teachingTypeId = $("#onlineLearningInfoView-teachingType").val();
	   var selectIdsJson = "{majorId:'onlineLearningInfoView-majorId',classesId:'onlineLearningInfoView-classesId'}";
	   cascadeQuery("teachingType", defaultValue, "", gradeId, classicId, teachingTypeId, "", "", selectIdsJson);
	}
	//选择专业
	function onlineLearningInfoViewMajor(){
	   var defaultValue = $("#onlineLearningInfoView-brSchoolId").val();
	   var gradeId = $("#onlineLearningInfoView-gradeId").val();
	   var classicId = $("#onlineLearningInfoView-classicId").val();
	   var teachingTypeId = $("#onlineLearningInfoView-teachingType").val();
	   var majorId = $("#onlineLearningInfoView-majorId").val();
	   var selectIdsJson = "{classesId:'onlineLearningInfoView-classesId'}";
	   cascadeQuery("classes", defaultValue, "", gradeId, classicId, teachingTypeId, majorId, "", selectIdsJson);
	}
	//导出
	function exportOnlineLearningInfo(){
	   var brSchoolId = $("#onlineLearningInfoView-brSchoolId").val();
	   var gradeId = $("#onlineLearningInfoView-gradeId").val();
	   var classicId = $("#onlineLearningInfoView-classicId").val();
	   var schoolType = $("#onlineLearningInfoView-teachingType").val();
	   var majorId = $("#onlineLearningInfoView-majorId").val();
	   var classesId = $("#onlineLearningInfoView-classesId").val();
	   var courseId = $("#onlineLearningInfoView_courseId").val();
	   var yearInfo = $("#onlineLearningInfoView_yearInfo").val();
	   var term = $("#onlineLearningInfoView_term").val();
	   var name = "${condition['name']}";
	   var studyNo = "${condition['studyNo']}";
	   var usualStatus = "${condition['usualStatus']}";
	   var url = "${baseUrl}/edu3/studySituation/view_onlineLearningInfo.html?optType=export";
	   url += "&branchSchool="+brSchoolId+"&gradeId="+gradeId+"&classicId="+classicId+"&schoolType="+schoolType
		   +"&majorId="+majorId+"&classesId="+classesId+"&courseId="+courseId+"&examOrderYearInfo="+yearInfo+"&examOrderTerm="+term
		   +"&studyNo="+studyNo+"&name="+name+"&usualStatus="+usualStatus;
	   downloadFileByIframe(url,'learningInfo_downloadIframe');
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/studySituation/view_onlineLearningInfo.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学站：</label> <span
							sel-id="onlineLearningInfoView-brSchoolId"
							sel-name="branchSchool"
							sel-onchange="onlineLearningInfoViewUnit()"
							sel-classs="flexselect"></span></li>
						<li><label>年级：</label> <span
							sel-id="onlineLearningInfoView-gradeId" sel-name="gradeId"
							sel-onchange="onlineLearningInfoViewGrade()"
							sel-style="width: 53%"></span></li>
						<li><label>层次：</label> <span
							sel-id="onlineLearningInfoView-classicId" sel-name="classicId"
							sel-onchange="sonlineLearningInfoViewClassic()"
							sel-style="width: 53%"></span></li>
						<li><label>办学模式：</label> <span
							sel-id="onlineLearningInfoView-teachingType"
							sel-name="schoolType"
							sel-onchange="onlineLearningInfoViewTeachingType()"
							sel-style="width: 100px;"></span></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span
							sel-id="onlineLearningInfoView-majorId" sel-name="majorId"
							sel-onchange="onlineLearningInfoViewMajor()"
							sel-classs="flexselect"></span></li>
						<li><label>年度：</label>
						<gh:selectModel id="onlineLearningInfoView_yearInfo"
								name="examOrderYearInfo" bindValue="resourceid"
								displayValue="yearName" style="width: 53%"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['examOrderYearInfo']}"
								orderBy="firstYear desc" /></li>
						<li><label>学期：</label>
						<gh:select id="onlineLearningInfoView_term" name="examOrderTerm"
								value="${condition['examOrderTerm']}" dictionaryCode="CodeTerm"
								style="width: 53%" /></li>
						<li><label>状态：</label> <select name="usualStatus"
							style="width: 100px;">
								<option value="">请选择</option>
								<option value="-1"
									<c:if test="${condition['usualStatus'] eq '-1' }">selected="selected"</c:if>>未录入</option>
								<option value="0"
									<c:if test="${condition['usualStatus'] eq '0' }">selected="selected"</c:if>>已保存</option>
								<option value="1"
									<c:if test="${condition['usualStatus'] eq '1' }">selected="selected"</c:if>>已提交</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span
							sel-id="onlineLearningInfoView-classesId" sel-name="classesId"
							sel-classs="flexselect"></span></li>
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 53%" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 53%" /></li>
						<li><label style="width: 40px;">课程：</label> <gh:courseAutocomplete name="courseId"
								value="${condition['courseId']}"
								id="onlineLearningInfoView_courseId" hasResource="Y"
								displayType="code" tabindex="1" style="width: 140px;"></gh:courseAutocomplete>
						</li>
					</ul>
					<ul class="searchContent">
						<label style="width: 500px;font-size: small;color: green;">网上学习时间：${learningTime.startTime } 至 ${learningTime.endTime }</label>
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
			<gh:resAuth parentCode="RES_LEAR_VIEW_ONLINELEARNINGINFO"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<!-- <th width="3%"><input type="checkbox" name="checkall" id="check_all_onlineLearningInfoView" onclick="checkboxAll('#check_all_onlineLearningInfoView','resourceid','#onlineLearningInfoViewBody')"/></th> -->
						<th width="7%">年度</th>
						<th width="4%">学期</th>
						<th width="9%">课程</th>
						<th width="6%">学号</th>
						<th width="5%">姓名</th>
						<th width="10%">教学站</th>
						<th width="5%">年级</th>
						<th width="8%">专业</th>
						<th width="12%">班级</th>
						<th width="4%">问答分</th>
						<th width="4%">练习分</th>
						<th width="4%">作业分</th>
						<th width="5%">综合分数</th>
						<th width="4%">状态</th>
						<th width="10%">成绩比重</th>
					</tr>
				</thead>
				<tbody id="onlineLearningInfoViewBody">
					<c:forEach items="${onlineLearningInfoList.result}" var="info"
						varStatus="vs">
						<tr>
							<!-- 
			            <td>			            	
		           			<input type="checkbox" name="resourceid" value="${p.studentLearnPlanId }" autocomplete="off"/>
			            </td>
			             -->
							<td>${info.yearName}</td>
							<td>${ghfn:dictCode2Val('CodeTerm',info.term) }</td>
							<td>${info.courseName}<input type="hidden" name="courseId"
								value="${info.courseId }" /></td>
							<td>${info.studyNo}</td>
							<td>${info.studentName}</td>
							<td>${info.unitName}</td>
							<td>${info.gradeName}</td>
							<td>${info.majorName}</td>
							<td>${info.classesName}</td>
							<td>${info.askQuestionResults}</td>
							<td>${info.courseExamResults}</td>
							<td>${info.exerciseResults}</td>
							<td><fmt:formatNumber pattern="###.#"
									value="${info.usualResults}" /></td>
							<td><c:choose>
									<c:when test="${info.status eq '1' }">已提交</c:when>
									<c:when test="${info.status eq '0' }">已保存</c:when>
									<c:otherwise>未录入</c:otherwise>
								</c:choose></td>
							<td>${info.resultsProportion }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${onlineLearningInfoList}"
				goPageUrl="${baseUrl}/edu3/studySituation/view_onlineLearningInfo.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</body>
</html>