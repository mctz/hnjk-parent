<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>检阅复学学生成绩替换情况</title>
<script type="text/javascript">
function checkForMark(){
	var ids ="";
	$('input[name="markdelete"]:checked').each(function(){
		ids+="'"+$(this).attr('id')+"',";
		}) 
	
	var url= "${baseUrl}/edu3/register/stuchangeinfo/self/setDeleteMark.html?ids="+ids;
	$.ajax({
	type:"post",
	url:url,
	dataType:"json",
	success:function(data){
		if(data['statusCode']==200){
			window.parent.submit();
			$.pdialog.closeCurrent();
		}else if(data['statusCode']==300){
			alertMsg.warn(data['message']);
			return false;
		}
	}
	});
	
}

</script>
</head>
<body>

	<h2 class="contentTitle">检阅复学学生成绩替换情况</h2>
	<div class="page">
		<div class="pageContent">


			<div id="changeInfoView" class="pageFormContent" layoutH="97">
				<div class="tabs">
					<div class="tabsHeader">
						<div class="tabsHeaderContent">
							<ul>
								<li class="selected"><a href="#"><span>成绩信息</span></a></li>
							</ul>
						</div>
					</div>
					<div class="tabsContent" style="height: 100%;">
						<div>
							<table class="form" id="changeTab">
								<tr>
									<th style="width: 6%">匹配结果</th>
									<th style="width: 6%">考试课程</th>
									<th style="width: 6%">替换后课程所在年度</th>
									<th style="width: 6%">替换后课程所在年度的学期</th>
									<th style="width: 8%">综合成绩</th>
									<th style="width: 7%">操作</th>
								</tr>
								<%-- <c:forEach items="${passList}" var="pass" varStatus="vs">
								<tr>
									
									<td style="width: 6%">考核通过</td>
									<td style="width: 6%">${pass.courseName}</td>
									<td style="width: 6%">${ghfn:dictCode2Val('CodeTermType',pass.courseTerm) }</td>
									<td style="width: 6%">${pass.batchName}</td>
									<td style="width: 8%">${ghfn:dictCode2Val('ExamResult',pass.examSubType) }</td>
									<td style="width: 8%">${pass.integratedScore}</td>
									<td style="width: 7%">替换</td>
									
								</tr>
								</c:forEach>
								<c:forEach items="${failList}" var="fail" varStatus="vs">
								<tr>
									
									<td style="width: 6%">考核不通过</td>
									<td style="width: 6%">${fail.courseName}</td>
									<td style="width: 6%">${ghfn:dictCode2Val('CodeTermType',fail.courseTerm) }</td>
									<td style="width: 6%">${fail.batchName}</td>
									<td style="width: 8%">${ghfn:dictCode2Val('ExamResult',fail.examSubType) }</td>
									<td style="width: 8%">${fail.integratedScore}</td>
									<td style="width: 7%" >
										<input <c:if test="${fail.isMarkDelete eq 'Y' }">checked="checked"</c:if> <c:if test="${seeOnly eq 'true'}" >disabled="disabled"</c:if>  name="markdelete" id="${fail.examResultId}"  type="checkbox"/>标记删除
									</td>
									
								</tr>
								</c:forEach>
								<c:forEach items="${outOfPlanList}" var="out" varStatus="vs">
								<tr>
									
									<td style="width: 6%">计划外课程</td>
									<td style="width: 6%">${out.courseName}</td>
									<td style="width: 6%">${ghfn:dictCode2Val('CodeTermType',out.courseTerm) }</td>
									<td style="width: 6%">${out.batchName}</td>
									<td style="width: 8%">${ghfn:dictCode2Val('ExamResult',out.examSubType) }</td>
									<td style="width: 8%">${out.integratedScore}</td>
									<td style="width: 7%">
										计划外
									</td>
									
								</tr>
								</c:forEach> --%>

								<c:forEach items="${passList}" var="pass" varStatus="vs">
									<tr>
										<td style="width: 6%">匹配且通过</td>
										<td style="width: 6%">${pass.teachingPlanCourse.course.courseName}</td>
										<td style="width: 6%">${pass.yearInfo.firstYear }</td>
										<td style="width: 6%">${ghfn:dictCode2Val('CodeTermType',pass.term) }</td>
										<td style="width: 8%">${pass.finalScore}</td>
										<td style="width: 7%">替换</td>
									</tr>
								</c:forEach>
								<c:forEach items="${failList}" var="fail" varStatus="vs">
									<tr>
										<td style="width: 6%">匹配不通过</td>
										<td style="width: 6%">${fail.teachingPlanCourse.course.courseName}</td>
										<td style="width: 6%">${fail.yearInfo.firstYear }</td>
										<td style="width: 6%">${ghfn:dictCode2Val('CodeTermType',fail.term) }</td>
										<td style="width: 8%">${fail.finalScore}</td>
										<td style="width: 7%"><input
											<c:if test="${fail.examResults.isMarkDelete eq 'Y' }">checked="checked"</c:if>
											<c:if test="${seeOnly eq 'true'}" >disabled="disabled"</c:if>
											name="markdelete" id="${fail.examResults.resourceid}"
											type="checkbox" />标记删除</td>

									</tr>
								</c:forEach>
								<c:forEach items="${outOfPlanList}" var="out" varStatus="vs">
									<tr>
										<td style="width: 6%">不匹配</td>
										<td style="width: 6%">${out.teachingPlanCourse.course.courseName}</td>
										<td style="width: 6%">${out.yearInfo.firstYear }</td>
										<td style="width: 6%">${ghfn:dictCode2Val('CodeTermType',out.term) }</td>
										<td style="width: 8%">${out.finalScore}</td>
										<td style="width: 7%">进入计划外</td>
									</tr>
								</c:forEach>
							</table>
							<c:if test="${!(seeOnly eq 'true')}">
								<ul>
									<li style="float: right"><div class="button">
											<div class="buttonContent">
												<button type="button" class="close"
													onclick="$.pdialog.closeCurrent()">关闭</button>
											</div>
										</div></li>
									<li style="float: right"><div class="buttonActive">
											<div class="buttonContent">
												<button type="button" onclick="checkForMark()">保存</button>
											</div>
										</div></li>

								</ul>
							</c:if>
						</div>

					</div>


				</div>
			</div>



		</div>
	</div>
</body>
</html>