<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置登分老师</title>
<script type="text/javascript">
		function goSelectTeacher(type,idsN,namesN,title){
			$.pdialog.open("${baseUrl }/edu3/teaching/teachingplancoursetimetable/teacher.html?idsN="+idsN+"&namesN="+namesN+"&type="+type+"&unitIds=${unitids}"
					,"SelectTeacher","选择"+title,{height:600,width:800});
		}
	</script>
</head>
<body>
	<h2 class="contentTitle">设置登分老师</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/teachingplancoursetimetable/setTeachSave.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input id="resids" name="resids" value="${resourceids}"
					type="hidden"> <input id="guiplanids" name="guiplanids"
					value="${guiplanids}" type="hidden"> <input
					id="plancourseids" name="plancourseids" value="${plancourseids}"
					type="hidden"> <input id="unitids" name="unitids"
					value="${unitids}" type="hidden"> <input id="classesids"
					name="classesids" value="${classesids}" type="hidden"> <input
					id="courseids" name="courseids" value="${courseids}" type="hidden">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<!-- 1 -->
							<div>
								<div class="pageFormContent">
									<table class="form">
										<%--<tr>
									<td width="20%">班级</td>
									<td>${setTeachList.classesname}</td>
								</tr>
								<tr>
									<td width="20%">年级</td>
									<td>${setTeachList.gradename}</td>
								</tr>
								<tr>
									<td width="20%">专业</td>
									<td>${setTeachList.majorname}</td>
								</tr>
								<tr>
									<td width="20%">层次</td>
									<td>${setTeachList.classicname}</td>
								</tr>
								<tr>
									<td width="20%">学习方式</td>
									<td>${ghfn:dictCode2Val('CodeTeachingType',setTeachList.teachingType) }</td>
								</tr> 	
								<tr>
									<td width="20%">教学点</td>
									<td>${setTeachList.unitname}</td>
								</tr> 	
								<tr>
									<td width="20%">课程</td>
									<td>${setTeachList.coursename}</td>
								</tr> 
								<tr>
									<td width="20%">上课学期</td>
									<td>${ghfn:dictCode2Val('CodeCourseTermType',setTeachList.courseterm) }</td>
								</tr> --%>
										<tr>
											<td width="20%">登分老师</td>
											<td><input type="text" name="setteacherName"
												value="${setteacherName }" id="teacherName"
												readonly="readonly" /> <input type="hidden"
												name="teacherId" id="teacherId" value="${teacherId }" /> <a
												href="javascript:;" class="button"
												onclick="goSelectTeacher('0','teacherId','teacherName','登分老师');"><span>登分老师</span></a>
											</td>
										</tr>
									</table>
								</div>
							</div>
							<!-- 2 -->
							<div class="tabsFooter">
								<div class="tabsFooterContent"></div>
							</div>
						</div>
					</div>
					<div class="formBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">提交</button>
									</div>
								</div></li>
							<li><div class="button">
									<div class="buttonContent">
										<button type="button" class="close"
											onclick="navTab.closeCurrentTab();">取消</button>
									</div>
								</div></li>
						</ul>
					</div>
			</form>
		</div>
	</div>
</body>
</html>