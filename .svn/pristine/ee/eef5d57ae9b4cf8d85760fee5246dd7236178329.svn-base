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
				action="${baseUrl }/edu3/learning/bbs/edumanager/stat.html"
				class="pageForm" onsubmit="return navTabSearch(this);">
				<div class="pageFormContent" layoutH="58">
					<div>
						<label>教学站：</label>
						<gh:brSchoolAutocomplete name="unitId" tabindex="1"
							id="edumanagerBbsstatSearch_as_unitId"
							defaultValue="${condition['unitId']}" displayType="code"
							scope="all" />
					</div>
					<div>
						<label>课程：</label>
						<gh:courseAutocomplete id="edumanagerbbsstatsearchCourseId"
							name="courseId" tabindex="2" value="${condition['courseId']}"
							displayType="code" />
					</div>
					<div>
						<label>姓名：</label><input type="text" name="cnName"
							value="${condition['cnName'] }" />
					</div>
					<div>
						<label>账号：</label><input type="text" name="username"
							value="${condition['username'] }" />
					</div>
					<div>
						<label>开始时间：</label> <input type="text" name="startTime"
							value="${condition['startTime']}" class="Wdate"
							id="edumanagerStatStartTime"
							onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'edumanagerStatEndTime\')}'})" />

					</div>
					<div>
						<label>结束时间：</label> <input type="text" name="endTime"
							value="${condition['endTime']}" class="Wdate"
							id="edumanagerStatEndTime"
							onFocus="WdatePicker({minDate:'#F{$dp.$D(\'edumanagerStatStartTime\')}'})" />
					</div>
					<div class="divider">divider</div>
					<div>
						<label>排序条件：</label> <select name="orderBy">
							<option value="">--请选择--</option>
							<option value="course.courseName">课程</option>
							<option value="unit.unitName">教学站</option>
							<option value="users.cnName">姓名</option>
							<option value="users.username">登录名</option>
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
