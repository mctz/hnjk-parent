<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随机抽取试题</title>
<script type="text/javascript">
	function _randomExamPaperValidateCallback(form){
		var $form = $(form);
		var isValid = true;
		$form.find("input[name='num']").each(function (){
			if(!$.trim($(this).val()).isInteger()){
				isValid = false;
				return false;
			}
			var max = parseInt($(this).attr('title'));
			var num = parseInt($.trim($(this).val()));
			if(num > max){
				isValid = false;
				return false;
			}
		});
		if(!isValid){
			alertMsg.warn("题目数必须为整数并且不大于可用题目数");
			return false;
		}
		
		isValid = true;
		$form.find("input[name='score']").each(function (){
			if(!$.trim($(this).val()).isNumber()){
				isValid = false;
				return false;
			}
		});
		if(!isValid){
			alertMsg.warn("每个类型的题目分值必须为数字");
			return false;
		}
		
		return validateCallback(form,function(json){
			DWZ.ajaxDone(json);
			if (json.statusCode == 200){
				$.pdialog.closeCurrent();
			}
		});
	}
</script>
</head>
<body>
	<h2 class="contentTitle">${courseExamPaper.paperName }</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/metares/courseexampapers/courseexam/save.html"
				class="pageForm"
				onsubmit="return _randomExamPaperValidateCallback(this);">
				<input type="hidden" name="paperId"
					value="${courseExamPaper.resourceid }" /> <input type="hidden"
					name="random" value="random" />
				<table class="table" layouth="138">
					<thead>
						<tr>
							<th width="15%">课程</th>
							<th width="25%">类别</th>
							<th width="20%">题型</th>
							<th width="10%">可用题目数</th>
							<th width="15%">分值</th>
							<th width="15%">题目数</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${courseExamTypeAndCounts }"
							var="courseExamTypeAndCount" varStatus="vs">
							<tr>
								<td><c:choose>
										<c:when
											test="${courseExamPaper.paperType eq 'entrance_exam' }">${ghfn:dictCode2Val('CodeEntranceExam',courseExamPaper.courseName)}</c:when>
										<c:otherwise>${courseExamPaper.course.courseName }</c:otherwise>
									</c:choose> <input type="hidden" name="examNodeType"
									value="${courseExamTypeAndCount.examNodeType }" /> <input
									type="hidden" name="examType"
									value="${courseExamTypeAndCount.examType }" /></td>
								<td>${ghfn:dictCode2Val('CodeExamNodeType',courseExamTypeAndCount.examNodeType)}</td>
								<td>${ghfn:dictCode2Val('CodeExamType',courseExamTypeAndCount.examType)}</td>
								<td>${courseExamTypeAndCount.examcount}</td>
								<td><input type="text" name="score" size="5" value="0" /></td>
								<td><input type="text" name="num" size="5" value="0"
									title="${courseExamTypeAndCount.examcount}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
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
</body>
</html>