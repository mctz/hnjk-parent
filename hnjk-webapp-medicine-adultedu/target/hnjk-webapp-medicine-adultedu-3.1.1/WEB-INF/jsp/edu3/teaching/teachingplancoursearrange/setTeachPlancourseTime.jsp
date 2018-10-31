<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>批量设置入学资格审核状态</title>
</head>
<div class="page">
	<div class="pageContent">
		<!-- Hidden District -->

		<div class="searchBar">
			<ul class="searchContent">
				<gh:select id="setTeachPlanCourseTime" name="setTeachPlanCourseTime"
					dictionaryCode="CodeCourseTermType" />
			</ul>
			<ul class="searchContent" id="extensionDiv">
			</ul>
			<div class="searchContent" id="extensionDiv2"></div>
			<div class="buttonActive" style="float: right;">
				<div class="buttonContent">
					<button type="button" onclick="doBatchSetRecruitStatus();">确定
					</button>
				</div>
			</div>
		</div>

	</div>
	</body>
	</html>