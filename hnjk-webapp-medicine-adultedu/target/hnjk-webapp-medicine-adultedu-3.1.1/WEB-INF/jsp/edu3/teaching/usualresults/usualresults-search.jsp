<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>高级查询</title>
</head>
<body>
	<script type="text/javascript">
</script>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl }/edu3/teaching/usualresults/list.html"
				class="pageForm" onsubmit="return navTabSearch(this);">
				<div class="pageFormContent" layoutH="58">
					<div>
						<label>年度：</label>
						<gh:selectModel name="yearInfo" bindValue="resourceid"
							displayValue="yearName"
							modelClass="com.hnjk.edu.basedata.model.YearInfo"
							value="${condition['yearInfo']}" orderBy="firstYear desc" />
					</div>
					<div>
						<label>学期：</label>
						<gh:select name="term" value="${condition['term']}"
							dictionaryCode="CodeTerm" />
					</div>
					<div>
						<label>课程：</label><input type="text" name='courseName'
							value="${condition['courseName']}" />
					</div>
					<div>
						<label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" />
					</div>
					<div>
						<label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" />
					</div>
					<div class="divider">divider</div>
					<div>
						<label>排序条件：</label> <select name="orderBy">
							<option value="">--请选择--</option>
							<option value="yearInfo">年度</option>
							<option value="term">学期</option>
							<option value="teachingPlanCourse.course">课程</option>
							<option value="studentInfo.studyNo">学号</option>
						</select> <label class="radioButton"><input name="orderType"
							type="radio" value="ASC" checked="checked" />顺序</label> <label
							class="radioButton"><input name="orderType" type="radio"
							value="DESC" />倒序</label>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">开始检索</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="reset">清空重输</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
