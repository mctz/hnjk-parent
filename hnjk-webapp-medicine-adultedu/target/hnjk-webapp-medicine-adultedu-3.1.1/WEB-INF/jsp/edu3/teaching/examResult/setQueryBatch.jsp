<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改学士学位英语成绩查询批次</title>
</head>
<body>
	<script type="text/javascript">
	
</script>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl }/edu3/teaching/examResult/setQueryBatch.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="58">
					<div>
						<label>年度：</label>
						<gh:selectModel id="setQueryBatch_YearInfo" name="yearInfo"
							bindValue="firstYear" displayValue="yearName" orderBy="yearName"
							modelClass="com.hnjk.edu.basedata.model.YearInfo"
							value="${yearInfo}" style="width:50%;height:25px"
							classCss="required" />
					</div>
					<div style="height: 40px"></div>
					<div>
						<label>学期：</label>
						<gh:select id="setQueryBatch_term" name="term" value="${term}"
							dictionaryCode="CodeTerm" filtrationStr="1,2"
							style="width:50%;height:25px" classCss="required" />
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
