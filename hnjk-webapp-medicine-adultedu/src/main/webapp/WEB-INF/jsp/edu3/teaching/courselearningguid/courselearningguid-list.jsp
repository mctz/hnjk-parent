<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程学习目标管理</title>
<script type="text/javascript">
	//新增
	function addCourseLearningGuid(){
		navTab.openTab('RES_TEACHING_COURSELEARNINGGUID', '${baseUrl}/edu3/teaching/courselearningguid/input.html?syllabusId='+$("#courseLearningGuidForm input[name='syllabusId']").val(), '新增学习目标');
	}
	
	//修改
	function modifyCourseLearningGuid(){
		var url = "${baseUrl}/edu3/teaching/courselearningguid/input.html";
		if(isCheckOnlyone('resourceid','#courseLearningGuidBody')){
			navTab.openTab('RES_TEACHING_COURSELEARNINGGUID', url+'?resourceid='+$("#courseLearningGuidBody input[@name='resourceid']:checked").val(), '编辑学习目标');
		}			
	}
		
	//删除
	function deleteCourseLearningGuid(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/courselearningguid/delete.html","#courseLearningGuidBody");
	}
	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form id="courseLearningGuidForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/courselearningguid/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>类型：</label>
						<gh:select name="type" value="${condition['type']}"
								dictionaryCode="CodeCourseLearningGuidType" style="width:135px" />
							<input type="hidden" name="syllabusId"
							value="${condition['syllabusId']}" /></li>
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
			<gh:resAuth parentCode="RES_METARES_COURSEWARE_MANAGE"
				pageType="cglist"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="10%"><input type="checkbox" name="checkall"
							id="check_all_courselearningguid"
							onclick="checkboxAll('#check_all_courselearningguid','resourceid','#courseLearningGuidBody')" /></th>
						<th width="20%">所属知识节点</th>
						<th width="30%">类型</th>
						<th width="20%">填写人</th>
						<th width="20%">填写日期</th>
					</tr>
				</thead>
				<tbody id="courseLearningGuidBody">
					<c:forEach items="${courseLearningGuidPage.result}"
						var="courseLearningGuid" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${courseLearningGuid.resourceid }" autocomplete="off" /></td>
							<td>${courseLearningGuid.syllabus.syllabusName}</td>
							<td>${ghfn:dictCode2Val('CodeCourseLearningGuidType',courseLearningGuid.type) }</td>
							<td>${courseLearningGuid.fillinMan }</td>
							<td>${courseLearningGuid.fillinDate }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${courseLearningGuidPage}"
				goPageUrl="${baseUrl }/edu3/teaching/courselearningguid/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>