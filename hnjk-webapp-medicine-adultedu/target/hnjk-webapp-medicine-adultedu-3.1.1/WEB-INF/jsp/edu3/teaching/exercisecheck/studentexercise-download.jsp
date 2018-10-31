<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>作业附件下载</title>
<script type="text/javascript">
		//附件下载
	   function downloadAttachFile(attid){
	   		var url = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
	   		var elemIF = document.createElement("iframe");  
			elemIF.src = url;  
			elemIF.style.display = "none";  
			document.body.appendChild(elemIF); 
	   }
	   //提交评分
	   function sendStudentExercise(){	
	 		var url = "${baseUrl}/edu3/teaching/exercisecheck/studentexercise/save.html";
			$.get(url,{exerciseBatchId:$('input[name=exerciseBatchId]').val(),studentExerciseResultId:$('input[name=studentExerciseResultId]').val(),attachsresult:$('input[name=attachsresult]').val()},function(json){
				validateFormCallback(json);
			},'json');
		}
	</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/exercisecheck/studentexercise/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="exerciseBatchId"
					value="${studentExerciseResult.exerciseBatch.resourceid }" /> <input
					type="hidden" name="studentExerciseResultId"
					value="${studentExerciseResult.resourceid }" /> <input
					type="hidden" name="from" value="fact" />
				<div class="pageFormContent" layoutH="60">
					<table class="form" style="width: 99%">
						<c:choose>
							<c:when test="${empty attachs }">
								<tr>
									<td colspan="4"><font color="green">作业附件不存在</font></td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${attachs}" var="a" varStatus="vs">
									<tr>
										<td>学生作业：</td>
										<td colspan="3"><a href="#"
											onclick="downloadAttachFile('${a.resourceid }')"
											style="color: blue">${a.attName }</a></td>
									</tr>
								</c:forEach>
								<c:forEach items="${attachsAnswer}" var="a" varStatus="vs">
									<tr>
										<td>参考答案：</td>
										<td colspan="3"><a href="#"
											onclick="downloadAttachFile('${a.resourceid }')"
											style="color: blue">${a.attName }</a></td>
									</tr>
								</c:forEach>
								<c:if
									test="${studentExerciseResult.exerciseBatch.scoringType eq '2' }">
									<tr>
										<td>老师评分：</td>
										<td colspan="3"><input type="text" name="result"
											value="${studentExerciseResult.result }"
											class="required number" size="30" /></td>
									</tr>
								</c:if>
								<tr>
									<td width="20%">是否评为优秀作业</td>
									<td width="30%"><gh:select name="isExcell"
											value="${studentExerciseResult.isExcell}"
											dictionaryCode="yesOrNo" choose="N" /></td>
									<td width="20%">是否为典型批改</td>
									<td width="30%"><gh:select name="isTypical"
											value="${studentExerciseResult.isTypical}"
											dictionaryCode="yesOrNo" choose="N" /></td>
								</tr>
								<tr>
									<td>老师评价：</td>
									<td colspan="3"><textarea name="teacherAdvise" rows="8"
											cols="" style="width: 99%;">${studentExerciseResult.teacherAdvise}</textarea></td>
								</tr>
							</c:otherwise>
						</c:choose>
					</table>
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
									<button type="button" class="close">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>