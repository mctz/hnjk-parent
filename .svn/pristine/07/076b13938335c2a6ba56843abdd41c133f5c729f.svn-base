<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年度设置</title>
</head>
<body>
	<script type="text/javascript">	
	//新增
	function addYearInfo(){
		navTab.openTab('navTab', '${baseUrl}/edu3/sysmanager/yearinfo/edit.html', '新增年度');
	}
	
	//修改
	function modifyYearInfo(){
		var url = "${baseUrl}/edu3/sysmanager/yearinfo/edit.html";
		if(isCheckOnlyone('resourceid','#yearBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#yearBody input[@name='resourceid']:checked").val(), '编辑年度');
		}			
	}
		
	//删除
	function deleteYearInfo(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/sysmanager/yearinfo/delete.html","#yearBody");
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/sysmanager/yearinfo/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度名称：</label><input type="text" name="yearName"
							value="${condition['yearName']}" /></li>
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
			<gh:resAuth parentCode="RES_BASEDATA_YEARINFO" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_year"
							onclick="checkboxAll('#check_all_year','resourceid','#yearBody')" /></th>
						<th width="10%">年度名称</th>
						<th width="9%">起始年份</th>
						<th width="20%">第一学期首周一日期</th>
						<th width="12%">第一学期周数</th>
						<th width="20%">第二学期首周一日期</th>
						<th width="12%">第二学期周数</th>
						<th width="12%">报到注册日</th>
					</tr>
				</thead>
				<tbody id="yearBody">
					<c:forEach items="${yearInfoList.result}" var="year" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${year.resourceid }" autocomplete="off" /></td>
							<td>${year.yearName }</td>
							<td>${year.firstYear }</td>
							<td><fmt:formatDate value="${year.firstMondayOffirstTerm }"
									pattern="yyyy-MM-dd" /></td>
							<td>${year.firstTermWeekNum }</td>
							<td><fmt:formatDate value="${year.firstMondayOfSecondTerm }"
									pattern="yyyy-MM-dd" /></td>
							<td>${year.secondTermWeekNum }</td>
							<td><fmt:formatDate value="${year.registrationDate }" pattern="yyyy-MM-dd" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${yearInfoList}"
				goPageUrl="${baseUrl }/edu3/sysmanager/yearinfo/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>