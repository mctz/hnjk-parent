<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>班级列表</title>
<script type="text/javascript">
	function adjustStuClasses(){
		var res = $("#adjustStudentIds").val();
		var stu = $("#adjustStuTbody input[name='resourceid']:checked");
		if(stu.size()==0){
			alertMsg.warn("请选择一个班级！");
		}
		var classesid = stu.val();
		$.post("${baseUrl}/edu3/roll/studentinfo/classes/adjust.html",{resourceid:res,classesid:classesid}, function (json){
			DWZ.ajaxDone(json);
			if (json.statusCode == 200){
				navTabPageBreak();
				$.pdialog.closeCurrent();
			}
		}, "json");
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl }/edu3/framework/studentinfo/classes/adjust.html"
				method="post">
				<input type="hidden" id="adjustStudentIds" name="resourceid"
					value="${condition['resourceid'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>班级名称：</label><input type="text" name="classname"
							value="${condition['classname']}" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="adjustStuClasses()">
											调整分班</button>
									</div>
								</div></li>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"></th>
						<th width="35%">班级名称</th>
						<th width="10%">年级</th>
						<th width="15%">教学站</th>
						<th width="15%">专业</th>
						<th width="10%">层次</th>
						<th width="10%">学习形式</th>
					</tr>
				</thead>
				<tbody id="adjustStuTbody">
					<c:forEach items="${classesPage.result}" var="cl" varStatus="vs">
						<tr>
							<td><input type="radio" name="resourceid"
								value="${cl.resourceid }" autocomplete="off" /></td>
							<td>${cl.classname }</td>
							<td>${cl.grade.gradeName }</td>
							<td>${cl.brSchool.unitName }</td>
							<td>${cl.major.majorName }</td>
							<td>${cl.classic.classicName }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',cl.teachingType) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${classesPage}"
				goPageUrl="${baseUrl }/edu3/framework/studentinfo/classes/adjust.html"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>
</body>
</html>