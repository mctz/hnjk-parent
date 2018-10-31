<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学习中心优惠设置</title>
<script type="text/javascript">
	//修改
	function editBrSchoolPrivilege(){
		if(isCheckOnlyone('resourceid','#brSchoolPrivilegeBody')){
			var res = $("#brSchoolPrivilegeBody input[@name='resourceid']:checked").val();
			var unitId = $("#brSchoolPrivilegeBody input[@name='resourceid']:checked").attr('rel');
			navTab.openTab('RES_FINANCE_PAYMENTFEEPRIVILEGE', "${baseUrl}/edu3/finance/paymentfeeprivilege/input.html?resourceid="+res+"&unitId="+unitId, '学习中心优惠设置');
		}			
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/brschool/privilege/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>单位名称：</label><input type="text" name="unitName"
							value="${condition['unitName']}" class="custom-inp"/></li>
						<li><label>单位编码：</label><input type="text" name="unitCode"
							value="${condition['unitCode']}" /></li>
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
			<gh:resAuth parentCode="RES_FINANCE_BRSCHOOLPRIVILEGE"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_brSchoolPrivilege"
							onclick="checkboxAll('#check_all_brSchoolPrivilege','resourceid','#brSchoolPrivilegeBody')" /></th>
						<th width="10%">学习中心编码</th>
						<th width="20%">学习中心名称</th>
						<th width="20%">学习中心简称</th>
						<th width="15%">优惠前每学分学费</th>
						<th width="15%">优惠后每学分学费</th>
						<th width="15%">备注</th>
					</tr>
				</thead>
				<tbody id="brSchoolPrivilegeBody">
					<c:forEach items="${brSchoolPrivilegeList.result}" var="privilege"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${privilege.paymentFeePrivilegeId }"
								rel="${privilege.unitId }" autocomplete="off" /></td>
							<td>${privilege.unitCode }</td>
							<td>${privilege.unitName }</td>
							<td>${privilege.unitShortName }</td>
							<td><fmt:formatNumber
									value='${privilege.beforePrivilegeFee }' pattern='####.##' /></td>
							<td><fmt:formatNumber
									value='${privilege.afterPrivilegeFee }' pattern='####.##' /></td>
							<td>${privilege.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${brSchoolPrivilegeList}"
				goPageUrl="${baseUrl }/edu3/finance/brschool/privilege/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>