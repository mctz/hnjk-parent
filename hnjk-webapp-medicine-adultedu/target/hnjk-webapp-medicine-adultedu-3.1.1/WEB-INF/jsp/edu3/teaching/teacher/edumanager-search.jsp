<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>高级查询</title>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl }/edu3/teaching/edumanager/list.html"
				class="pageForm" onsubmit="return navTabSearch(this);">
				<div class="pageFormContent" layoutH="58">
					<c:if test="${not isBrschool }">
						<div>
							<label>所属单位：</label>
							<gh:brSchoolAutocomplete name="unitId" tabindex="2"
								id="edumanager_advanceSearch_as_unitId"
								defaultValue="${condition['unitId']}" displayType="code"
								scope="all" />
						</div>
					</c:if>
					<div>
						<label>姓名：</label><input type="text" name="cnName"
							value="${condition['cnName'] }" />
					</div>
					<div>
						<label>账号：</label><input type="text" name="username"
							value="${condition['username'] }" />
					</div>
					<div>
						<label>教师编号：</label><input type="text" name="teacherCode"
							value="${condition['teacherCode'] }" />
					</div>
					<div>
						<label>教学类型：</label>
						<gh:select name="teachingType"
							value="${condition['teachingType']}"
							dictionaryCode="CodeTeachingType" />
					</div>

					<div>
						<label>职称：</label>
						<gh:select name="titleOfTechnical"
							value="${condition['titleOfTechnical']}"
							dictionaryCode="CodeTitleOfTechnicalCode" />
					</div>
					<div>
						<label>最高学历：</label>
						<gh:select name="educationalLevel"
							value="${condition['educationalLevel']}"
							dictionaryCode="CodeEducation" />
					</div>
					<div>
						<label>担任课程：</label>
						<gh:courseAutocomplete name="courseId" tabindex="2"
							id="edumanager_advanceSearch_as_courseId" displayType="code" />
					</div>
					<div class="divider">divider</div>
					<div>
						<label>排序条件：</label> <select name="orderBy">
							<option value="">--请选择--</option>
							<option value="unitId">所属单位</option>
							<option value="teacherCode">教师编号</option>
							<option value="cnName">姓名</option>
							<option value="username">登录名</option>
							<option value="teachingType">教师类型</option>
							<option value="titleOfTechnical">职称</option>
						</select> <label class="radioButton"><input name="orderType"
							type="radio" value="ASC" />顺序</label> <label class="radioButton"><input
							name="orderType" type="radio" value="DESC" />倒序</label>
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
