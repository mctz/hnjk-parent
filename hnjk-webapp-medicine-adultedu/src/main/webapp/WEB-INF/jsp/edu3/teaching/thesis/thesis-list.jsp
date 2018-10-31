<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业论文</title>
<script type="text/javascript">
	//新增
	function Gthesis_add(){
		navTab.openTab('RES_TEACHING_THESIS_PLAN_EDIT', '${baseUrl}/edu3/teaching/graduatethesis/edit.html', '新增论文批次');
	}
	
	//修改
	function Gthesis_edit(){
		var url = "${baseUrl}/edu3/teaching/graduatethesis/edit.html";
		if(isCheckOnlyone('resourceid','#thesisBody')){
			navTab.openTab('RES_TEACHING_THESIS_PLAN_EDIT', url+'?resourceid='+$("#thesisBody input[@name='resourceid']:checked").val(), '编辑论文批次');
		}			
	}
		
	//删除
	function Gthesis_del(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/graduatethesis/delete.html","#thesisBody");
	}
	
	//开放
	function Gthesis_open(){
		pageBarHandle("您确定要开放这些记录吗？","${baseUrl}/edu3/teaching/graduatethesis/states.html?states=open","#thesisBody");
	}
	//关闭
	function Gthesis_close(){
		pageBarHandle("您确定要关闭这些记录吗？","${baseUrl}/edu3/teaching/graduatethesis/states.html?states=close","#thesisBody");
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/graduatethesis/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label> <gh:selectModel name="yearId"
								bindValue="resourceid" displayValue="yearName"
								style="width:120px"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearId']}" /></li>
						<li><label>学期：</label> <gh:select name="term"
								dictionaryCode="CodeTerm" value="${condition['term']}"
								style="width:120px" /></li>
						<li><label>论文批次：</label> <input type="text" name="batchName"
							value="${condition['batchName']}" /></li>
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
			<gh:resAuth parentCode="RES_TEACHING_THESIS_PLAN" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_tp"
							onclick="checkboxAll('#check_all_tp','resourceid','#thesisBody')" /></th>
						<th width="15%">论文批次</th>
						<th width="10%">年度</th>
						<th width="10%">学期</th>
						<th width="10%">预约开始时间</th>
						<th width="10%">预约截止时间</th>
						<!--<th width="10%">当前总预约人数</th>
		            -->
						<th width="10%">定稿截止时间</th>
						<th width="10%">预约公布时间</th>
						<th width="10%">预约状态</th>
					</tr>
				</thead>
				<tbody id="thesisBody">
					<c:forEach items="${thesisList.result}" var="t" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${t.resourceid }" autocomplete="off" /></td>
							<td>${t.batchName }</td>
							<td>${t.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',t.term)}</td>
							<td><fmt:formatDate value="${t.startTime}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${t.endTime}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<!--<td >&nbsp;</td>
			            -->
							<td><fmt:formatDate
									value="${t.gradendDate.secondDraftEndDate}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${t.gradendDate.publishDate}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${ghfn:dictCode2Val('CodeExamSubscribeState',t.examsubStatus)}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${thesisList}"
				goPageUrl="${baseUrl}/edu3/teaching/graduatethesis/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
