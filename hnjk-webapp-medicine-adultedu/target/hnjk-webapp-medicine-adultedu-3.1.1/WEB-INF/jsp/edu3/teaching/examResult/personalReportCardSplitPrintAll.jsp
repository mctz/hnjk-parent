<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>个人成绩打印</title>
<script type="text/javascript">
	function printPersionReportCardSprilt(flag,segment,pageNum){
		var url 		 = "${baseUrl}/edu3/teaching/result/personalReportCard-printall-view.html?flag="+flag+"&segment="+segment+"&pageNum="+pageNum;
	    
		var branchSchool = "${condition['branchSchool']}";
		var gradeid      = "${condition['gradeid']}";
		var classic      = "${condition['classic']}";
		var major        = "${condition['major']}";
		var classId        = "${condition['classId']}";
		var studyNo        = "${condition['studyNo']}";
		var name        = "${condition['name']}";
		var learningStyle= "${condition['learningStyle']}";
		var studentStatus= "${condition['studentStatus']}";
		var totalSize    = "${condition['totalSize']}";
		var graduateDate = "${condition['graduateDateStr']}";
		var printPage = "${condition['printPage']}";
		var degreeUnitExam = "${condition['degreeUnitExam']}";
		//添加毕业确认时间
		var confirmGraduateDateb= $("#confirmGraduateDateb_id").val();
		var confirmGraduateDatee= $("#confirmGraduateDatee_id").val();
		
		if(""!=totalSize && undefined!=totalSize){
			url         += "&totalSize="+totalSize;
		}
		if(""!=branchSchool && undefined!=branchSchool){
			url         += "&branchSchool="+branchSchool;
		}
		if(""!=gradeid && undefined!=gradeid){
			url         += "&gradeid="+gradeid;
		}
		if(""!=classic && undefined!=classic){
			url         += "&classic="+classic;
		}
		if(""!=major && undefined!=major){
			url         += "&major="+major;
		}
		if(""!=classId && undefined!=classId){
			url         += "&classId="+classId;
		}
		if(""!=studyNo && undefined!=studyNo){
			url         += "&studyNo="+studyNo;
		}
		if(""!=name && undefined!=name){
			url         += "&name="+name;
		}
		if(""!=learningStyle && undefined!=learningStyle){
			url         += "&learningStyle="+learningStyle;
		}
		if(""!=studentStatus && undefined!=studentStatus){
			url         += "&studentStatus="+studentStatus;
		}
		if(""!=graduateDate && undefined!=graduateDate){
			url         += "&graduateDateStr="+graduateDate;
		}
		if(""!=confirmGraduateDateb && undefined!=confirmGraduateDateb){
			url         += "&confirmGraduateDateb="+confirmGraduateDateb;
		}
		if(""!=confirmGraduateDatee && undefined!=confirmGraduateDatee){
			url         += "&confirmGraduateDatee="+confirmGraduateDatee;
		}
		if(""!=printPage && undefined!=printPage){
			url         += "&printPage="+printPage;
		}
		if(""!=degreeUnitExam && undefined!=degreeUnitExam){
			url         += "&degreeUnitExam="+degreeUnitExam;
		}
		$.pdialog.open(url,"RES_TEACHING_RESULT_REVIEW_FOR_PERSIONAL_REPORT_SEGMENT","打印成绩单",{width:800, height:600});
	}
</script>

</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<div class="searchBar">
				<ul class="searchContent">
					<li><label>教学站：</label> <gh:selectModel name="branchSchool"
							bindValue="resourceid" displayValue="unitCodeAndName"
							modelClass="com.hnjk.security.model.OrgUnit"
							value="${condition['branchSchool']}" style="width:55%"
							disabled="disabled" /></li>
				</ul>
				<ul class="searchContent">
					<li><label>年级：</label>
					<gh:selectModel id="gradeid" name="gradeid" bindValue="resourceid"
							displayValue="gradeName"
							modelClass="com.hnjk.edu.basedata.model.Grade"
							value="${condition['gradeid']}" orderBy="gradeName desc"
							style="width:55%" disabled="disabled" /></li>
				</ul>
				<ul class="searchContent">
					<li><label>层 次：</label> <gh:selectModel name="classic"
							bindValue="resourceid" displayValue="classicName"
							modelClass="com.hnjk.edu.basedata.model.Classic"
							value="${condition['classic']}" style="width:55%"
							disabled="disabled" /></li>
				</ul>
				<ul class="searchContent">
					<li><label>专 业：</label> <gh:selectModel name="major"
							bindValue="resourceid" displayValue="majorCodeName"
							modelClass="com.hnjk.edu.basedata.model.Major"
							orderBy="majorCode asc" value="${condition['major']}"
							style="width:55%" disabled="disabled" /></li>
				</ul>
				<ul class="searchContent">
					<li><label>班 级：</label> <gh:selectModel name="classId"
							bindValue="resourceid" displayValue="classname"
							modelClass="com.hnjk.edu.roll.model.Classes"
							value="${condition['classId']}" style="width:55%"
							disabled="disabled" /></li>
				</ul>
				<ul class="searchContent">
					<li><label>学籍状态：</label> <gh:select name="studentStatus"
							dictionaryCode="CodeStudentStatus" choose="Y"
							value="${condition['studentStatus']}" style="width:55%"
							disabled="disabled" /></li>
				</ul>
				<ul class="searchContent">
					<li><label>学习方式：</label> <gh:select name="learningStyle"
							dictionaryCode="CodeLearningStyle" choose="Y"
							value="${condition['learningStyle']}" style="width:55%"
							disabled="disabled" /></li>
				</ul>
				<ul class="searchContent">
					<li><label>毕业时间：</label> <select name="graduateDateStr"
						style="width: 55%" disabled="disabled">
							<c:choose>
								<c:when test="${not empty condition['graduateDateStr'] }">
									<option value="${condition['graduateDateStr']}"
										selected="selected">${condition['graduateDateStr']}</option>
								</c:when>
								<c:otherwise>
									<option value="">请选择</option>
								</c:otherwise>
							</c:choose>
					</select></li>
					<li><label>毕业确认时间:</label> <input type="text"
						id="confirmGraduateDateb_id" style="width: 60px;"
						name="confirmGraduateDateb" class="Wdate"
						value="${condition['confirmGraduateDateb']}"
						onfocus="WdatePicker({isShowWeek:true })" disabled="disabled" />
						到<input type="text" id="confirmGraduateDatee_id"
						style="width: 60px;" name="confirmGraduateDatee" class="Wdate"
						value="${condition['confirmGraduateDatee']}"
						onfocus="WdatePicker({isShowWeek:true })" disabled="disabled" /></li>
				</ul>
			</div>
		</div>

		<div class="pageContent">
			<table class="table" layoutH="188">
				<thead>
					<tr>
						<th width="90%"></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${spriltList}" var="spriltRange" varStatus="vs">
						<tr>
							<td><a href="javaScript:void(0)"
								onclick="printPersionReportCardSprilt('${condition['flag']}','${spriltRange}','${vs.index+1 }')">[${spriltRange }]</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>