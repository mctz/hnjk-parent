<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学任务书模板管理</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/teachtask/template/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label>
						<gh:selectModel id="teachtask_template_yearInfoId"
								name="yearInfoid" bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoid']}" /></li>
						<li><label>学期：</label>
						<gh:select id="teachtask_template_term" name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm" /></li>

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
			<gh:resAuth parentCode="RES_TEACHING_ESTAB_TEACHTASK_TEMPLATE"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_tkTemplate"
							onclick="checkboxAll('#check_all_tkTemplate','resourceid','#taskTemplateBody')" /></th>
						<th width="30%">年度</th>
						<th width="30%">学期</th>
						<th width="35%">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="taskTemplateBody">
					<c:forEach items="${taskList.result}" var="t" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${t.resourceid }" autocomplete="off" /></td>
							<td>${t.yearInfo }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',t.term) }</td>
							<td><a height="600" width="800" target="dialog"
								rel="_teachTaskView"
								href="${baseUrl }/edu3/teaching/teachtask/view.html?resourceid=${t.resourceid }">${t.yearInfo }${ghfn:dictCode2Val('CodeTerm',t.term) }教学任务书模板</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${taskList}"
				goPageUrl="${baseUrl }/edu3/teaching/teachtask/template/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
<script type="text/javascript">
	function addTaskTemplate(){
		navTab.openTab('RES_TEACHING_ESTAB_TEACHTASK_TEMPLATE_INPUT', '${baseUrl}/edu3/teaching/teachtask/template/input.html', '新增教学任务书模板');
	}
	
	function modifyTaskTemplate(){		
		if(isCheckOnlyone('resourceid','#taskTemplateBody')){
			navTab.openTab('RES_TEACHING_ESTAB_TEACHTASK_TEMPLATE_INPUT', baseUrl+'/edu3/teaching/teachtask/template/input.html?resourceid='+$("#taskTemplateBody input[@name='resourceid']:checked").val(), '修改教学任务书模板');
		}
	}
	
	function removeTaskTemplate(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/teachtask/template/remove.html","#taskTemplateBody");
	}	

</script>
</html>