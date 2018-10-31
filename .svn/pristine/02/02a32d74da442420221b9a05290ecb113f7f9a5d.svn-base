<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年级设置</title>
<script type="text/javascript">
	//新增
	function addGrade(){
		navTab.openTab('navTab', '${baseUrl}/edu3/sysmanager/grade/edit.html', '新增年级');
	}
	
	//修改
	function modifyGrade(){
		var url = "${baseUrl}/edu3/sysmanager/grade/edit.html";
		if(isCheckOnlyone('resourceid','#gradeBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#gradeBody input[@name='resourceid']:checked").val(), '编辑年级');
		}			
	}
		
	//删除
	function deleteGrade(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/sysmanager/grade/delete.html","#gradeBody");
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/sysmanager/grade/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年级名称：</label><input type="text" name="gradeName"
							value="${condition['gradeName']}" /></li>
					</ul>
					<div class="subBar">
						<ul>
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
			<gh:resAuth parentCode="RES_BASEDATA_GRADE" pageType="list"></gh:resAuth>
			<table id="gradeTab" class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_grade"
							onclick="checkboxAll('#check_all_grade','resourceid','#gradeBody')" /></th>
						<th width="20%">年级名称</th>
						<th width="20%">年度名称</th>
						<th width="10%" title="学期">学期</th>
						<th width="15%">是否默认年级</th>
						<th width="15%">默认入学日期</th>
						<th width="15%">默认毕业日期</th>
					</tr>
				</thead>
				<tbody id="gradeBody">
					<c:forEach items="${gradeList.result}" var="grade" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${grade.resourceid }" autocomplete="off" /></td>
							<td>${grade.gradeName }</td>
							<td>${grade.yearInfo }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',grade.term) }</td>
							<td>${grade.isDefaultGrade=='Y'?'是':'否' }</td>
							<td><fmt:formatDate value="${grade.indate }"
									pattern="yyyy-MM-dd" /></td>
							<td><fmt:formatDate value="${grade.graduateDate }"
									pattern="yyyy-MM-dd" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${gradeList}"
				goPageUrl="${baseUrl }/edu3/sysmanager/grade/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>