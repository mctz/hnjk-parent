<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>期末机考学生名单</title>
<script type="text/javascript">
	function finalExamStudentListReload(currentIndex){
		$.pdialog.reload("${baseUrl }/edu3/teaching/examinfo/student/list.html?examInfoId=${condition['examInfoId'] }&currentIndex="+currentIndex);
	}
	$(function(){//学院2016修改
		$("#searchExamResult_classesid1").flexselect();
	});
</script>
</head>
<body>
	<div class="page">
		<div class="tabs" currentIndex="${condition['currentIndex'] }"
			eventtype="click">
			<div class="tabsHeader">
				<div class="tabsHeaderContent">
					<ul>
						<li><a href="javascript:void(0)"
							onclick="finalExamStudentListReload(0)"><span>已安排机考学生</span></a></li>
						<li><a href="javascript:void(0)"
							onclick="finalExamStudentListReload(1)"><span>未安排机考学生</span></a></li>
					</ul>
				</div>
			</div>
			<div class="tabsContent" style="height: 100%;">
				<c:if test="${condition['currentIndex'] eq '1' }">
					<div></div>
				</c:if>
				<div>
					<div class="pageHeader">
						<form onsubmit="return dialogSearch(this);"
							action="${baseUrl }/edu3/teaching/examinfo/student/list.html"
							method="post">
							<input type="hidden" name="examInfoId"
								value="${condition['examInfoId'] }" /> <input type="hidden"
								name="currentIndex" value="${condition['currentIndex']}" />
							<div class="searchBar">
								<ul class="searchContent">
									<c:if test="${!isBrschool }">
										<li><label>教学站：</label> <gh:brSchoolAutocomplete
												name="branchSchool" tabindex="2"
												id="finalExamStudentList_branchSchool"
												defaultValue="${condition['branchSchool']}"
												style="width:120px" /></li>
									</c:if>
									<li><label>专业：</label>
									<gh:selectModel id="finalExamStudentList_major" name="major"
											bindValue="resourceid" displayValue="majorCodeName"
											value="${condition['major']}"
											modelClass="com.hnjk.edu.basedata.model.Major"
											style="width:120px" orderBy="majorCode" /></li>
									<li><label>层次：</label>
									<gh:selectModel id="finalExamStudentList_classic"
											name="classic" bindValue="resourceid"
											displayValue="classicName" value="${condition['classic']}"
											modelClass="com.hnjk.edu.basedata.model.Classic"
											style="width:120px" /></li>
									<li id="graduateudit-gradeToMajorToClasses5"><label>班级：</label>
										${classesSelect5 }</li>
								</ul>
								<ul class="searchContent">
									<li><label>年级：</label> <gh:selectModel
											id="finalExamStudentList_gradeid" name="gradeid"
											bindValue="resourceid" displayValue="gradeName"
											modelClass="com.hnjk.edu.basedata.model.Grade"
											value="${condition['gradeid']}"
											orderBy="yearInfo.firstYear desc" choose="Y"
											style="width: 120px" /></li>
									<li><label>姓名：</label><input
										id="finalExamStudentList_name" type="text" name="name"
										value="${condition['name']}" /></li>
									<li><label>学号：</label><input
										id="finalExamStudentList_studyNo" type="text" name="studyNo"
										value="${condition['studyNo']}" /></li>
									<li><label>学习形式:</label> <gh:select
											id="finalExamStudentList_leType" name="leType"
											value="${condition['leType']}"
											dictionaryCode="CodeTeachingType" style="width:100px;" /></li>
									<!-- 学院2016修改 -->
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
					<div class="">
						<c:choose>
							<c:when test="${condition['currentIndex'] eq '0' }">
								<gh:resAuth parentCode="RES_TEACHING_EXAM_FINALEXAM"
									pageType="sublist1"></gh:resAuth>
							</c:when>
							<c:otherwise>
								<gh:resAuth parentCode="RES_TEACHING_EXAM_FINALEXAM"
									pageType="sublist2"></gh:resAuth>
							</c:otherwise>
						</c:choose>
						<table class="table" layouth="217">
							<!-- 学院2016修改 -->
							<thead>
								<tr>
									<th width="5%"><input type="checkbox" name="checkall"
										id="check_all_studentlearnplan1"
										onclick="checkboxAll('#check_all_studentlearnplan1','resourceid','#studentlearnplan1Body')" /></th>
									<th width="10%">考试批次</th>
									<th width="10%">课程</th>
									<th width="10%">姓名</th>
									<th width="10%">学号</th>
									<th width="10%">教学站</th>
									<th width="10%">年级</th>
									<th width="10%">专业</th>
									<th width="10%">层次</th>
									<th width="15%">考试时间</th>
								</tr>
							</thead>
							<tbody id="studentlearnplan1Body">
								<!-- 学院2016修改 -->
								<c:choose>
									<c:when
										test="${ismuk eq 'Y' and condition['currentIndex'] eq '1' }">
										<c:forEach items="${finalExamStudentList.result}" var="muk"
											varStatus="vs">
											<tr>
												<td><input type="checkbox" name="resourceid"
													value="${muk.resourceid }" autocomplete="off" /></td>
												<td>${examInfo.examSub.batchName}</td>
												<td>${examInfo.course.courseName}</td>
												<td>${muk.studentInfo.studentName}</td>
												<td>${muk.studentInfo.studyNo}</td>
												<td>${muk.studentInfo.branchSchool }</td>
												<td>${muk.studentInfo.grade.gradeName }</td>
												<td>${muk.studentInfo.major.majorName }</td>
												<td>${muk.studentInfo.classic.classicName }</td>
												<td><fmt:formatDate value="${examInfo.examStartTime }"
														pattern="yyyy-MM-dd HH:mm:ss" /> - <fmt:formatDate
														value="${examInfo.examEndTime }"
														pattern="yyyy-MM-dd HH:mm:ss" /></td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<c:forEach items="${finalExamStudentList.result}" var="plan"
											varStatus="vs">
											<tr>
												<td><input type="checkbox" name="resourceid"
													value="${plan.resourceid }" autocomplete="off" /> <input
													type="hidden" id="${plan.resourceid }" name="examSeatFlag"
													value="${plan.examResults.examSeatNum }"
													title="${plan.studentInfo.studentName}" /></td>
												<td>${examInfo.examSub.batchName}</td>
												<td>${examInfo.course.courseName}</td>
												<td>${plan.studentInfo.studentName}</td>
												<td>${plan.studentInfo.studyNo}</td>
												<td>${plan.studentInfo.branchSchool }</td>
												<td>${plan.studentInfo.grade.gradeName }</td>
												<td>${plan.studentInfo.major.majorName }</td>
												<td>${plan.studentInfo.classic.classicName }</td>
												<td><fmt:formatDate value="${examInfo.examStartTime }"
														pattern="yyyy-MM-dd HH:mm:ss" /> - <fmt:formatDate
														value="${examInfo.examEndTime }"
														pattern="yyyy-MM-dd HH:mm:ss" /></td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<gh:page page="${finalExamStudentList}"
							goPageUrl="${baseUrl }/edu3/teaching/examinfo/student/list.html"
							pageType="sys" targetType="dialog" condition="${condition}"
							pageNumShown="3" />
					</div>
				</div>
				<c:if test="${condition['currentIndex'] eq '0' }">
					<div></div>
				</c:if>
			</div>
			<div class="tabsFooter">
				<div class="tabsFooterContent"></div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
function arrangeFinalExamInfo(arrangeType){
	var res = "";
	var k = 0;
	var num  = $("#studentlearnplan1Body input[name='resourceid']:checked").size();
	$("#studentlearnplan1Body input[@name='resourceid']:checked").each(function(){
            res+=$(this).val();
            if(k != num -1 ) res += ",";
            k++;
     });
	
	var branchSchool;
	var major;
	var classic;
	var gradeid;
	var name;
	var studyNo;
	var classes;
	var leType;
	var currentIndex;	
	if(res==''){
		branchSchool =$("#finalExamStudentList_branchSchool").val();
		major = $("#finalExamStudentList_major").val();
		classic = $("#finalExamStudentList_classic").val();
		gradeid = $("#finalExamStudentList_gradeid").val();
		name = $("#finalExamStudentList_name").val();
		studyNo = $("#finalExamStudentList_studyNo").val();
		classes= $("#searchExamResult_classesid1").val();
		leType=$("#finalExamStudentList_leType").val();
		currentIndex="${condition['currentIndex']}";	
	}
	
	var msg = "";
	if(arrangeType==0){
		var names = "";
		//检查是否已安排座位
		$("#studentlearnplan1Body input[name='resourceid']:checked").each(function(i){
			var examSeatNum = $("#"+$(this).val()).val();
			if(examSeatNum!= ""&&arrangeType==0){
				names += $("#"+$(this).val()).attr("title")+",";
			}
		});
		if(names.length>1){
			msg += "<font color='red'>"+names+"</font>已安排座位！";
		}
		msg += "你确定要取消这些学生的机考安排?";
	}else{
		msg = "你确定要给这些学生安排期末机考?";
	}
	
	alertMsg.confirm(msg, {
			okCall: function(){//执行			
	            var examInfoId = "${condition['examInfoId'] }";
				$.post("${baseUrl}/edu3/teaching/examinfo/student/arrange.html",{resourceid:res,examInfoId:examInfoId,arrangeType:arrangeType,branchSchool:branchSchool,major:major,
					classic:classic,gradeid:gradeid,name:name,studyNo:studyNo,classes:classes,leType:leType,currentIndex:currentIndex}, function(json){
					DWZ.ajaxDone(json);
					if (json.statusCode == 200){												
						dialogPageBreak();
					}
				}, "json");
			}
	});	
}
</script>
</body>
</html>