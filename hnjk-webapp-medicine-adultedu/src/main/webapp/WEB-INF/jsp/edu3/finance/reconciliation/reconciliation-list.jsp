<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>对账文件列表</title>
</head>
<script type="text/javascript">

	//对账
	function reconciliationProgress() {
		var url = "${baseUrl}/edu3/finance/tempStudentFee/reconciliation/progress.html";
		if (isCheckOnlyone('resourceid', '#ReconciliationListBody')) {
			var resourceid = $("#ReconciliationListBody input[name='resourceid']:checked").val();
			$.ajax({
				type: "POST",
				url:url,
				dateType:"json",
				data:{resourceid:resourceid},
				error:DWZ.ajaxError,
				success:function(data){
					if(data.statusCode==200){
						alertMsg.correct(data.message);
					}else{
				   		alertMsg.error(data.message);
				   		return false;
				   	}
				}
			})
		}
	}
	
	//删除
	function deleteReconciliation() {
		pageBarHandle("您确定要删除这些记录吗？",
				"${baseUrl}/edu3/finance/tempStudentFee/reconciliation/delete.html",
				"#ReconciliationListBody");
	}
</script>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/tempStudentFee/reconciliation/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>文件名：</label><input type="text" name="fileName"
							value="${condition['fileName']}" class="custom-inp"/></li>
						
					</ul>
					<ul>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_FINANCE_RECONCILIATION" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_reconciliation"
<!-- 							onclick="checkboxAll('#check_all_feemajor','resourceid','#feemajorBody')"  -->
							</th>
						<th width="30%">文件名</th>
						<th width="30%">对账日期</th>
						<th width="10%">总笔数</th>
						<th width="15%">总金额</th>
						<th width="15%">已否已对账</th>						
					</tr>
				</thead>
				<tbody id="ReconciliationListBody">
					<c:forEach items="${page.result}" var="r"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${r.resourceid }"  autocomplete="off" /></td>
							<td>${r.fileName }</td>
							<td><fmt:formatDate value="${r.uploadDate }"
															pattern="yyyy年MM月dd日" /></td>
							<td>${r.totalCount }</td>
							<td>${r.totalFee}</td>
							<td>${ghfn:dictCode2Val('yesOrNo',r.status) }</td>
<%-- 							<td>${ghfn:dictCode2Val('CodeQuestionBankUserType',qs.userType) }</td> --%>
<%-- 							<td>${ghfn:dictCode2Val('CodeQuestionBankCourseType',qs.courseType) }</td> --%>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/finance/tempStudentFee/reconciliation/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>