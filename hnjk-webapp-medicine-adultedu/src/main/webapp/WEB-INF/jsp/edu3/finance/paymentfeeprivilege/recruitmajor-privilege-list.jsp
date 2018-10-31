<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生专业学习中心优惠设置</title>
<script type="text/javascript">
	//修改
	function editMajorPrivilege(){
		if(isCheckOnlyone('resourceid','#recruitMajorPrivilegeBody')){
			var $obj = $("#recruitMajorPrivilegeBody input[@name='resourceid']:checked");
			var res = $obj.val(),unitId = $obj.attr('rel'),majorid = $obj.attr('majorid');
			navTab.openTab('RES_FINANCE_PAYMENTFEEPRIVILEGE', "${baseUrl}/edu3/finance/paymentfeeprivilege/input.html?resourceid="+res+"&unitId="+unitId+"&recruitMajorId="+majorid, '招生专业学习中心优惠设置');
		}			
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/recruitmajor/privilege/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>学习中心：</label> <gh:brSchoolAutocomplete
								name="brSchool" id="recruitMajorPrivilege_brSchool" tabindex="1"
								defaultValue="${condition['brSchool'] }" displayType="code"
								style="width:240px;" /></li>
						<li><label>招生批次：</label> <gh:selectModel id="recruitPlanId"
								name="recruitPlanId" bindValue="resourceid"
								displayValue="recruitPlanname"
								orderBy="yearInfo.firstYear desc,term desc,resourceid desc"
								modelClass="com.hnjk.edu.recruit.model.RecruitPlan"
								style="width:55%;" condition="isPublished='Y'"
								value="${condition['recruitPlanId']}" /></li>
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
			<gh:resAuth parentCode="RES_FINANCE_MAJORPRIVILEGE" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_recruitMajorPrivilege"
							onclick="checkboxAll('#check_all_recruitMajorPrivilege','resourceid','#recruitMajorPrivilegeBody')" /></th>
						<th width="15%">学习中心</th>
						<th width="25%">招生批次</th>
						<th width="25%">招生专业</th>
						<th width="15%">优惠费总金额</th>
						<th width="15%">备注</th>
					</tr>
				</thead>
				<tbody id="recruitMajorPrivilegeBody">
					<c:forEach items="${brSchoolPrivilegeList.result}" var="privilege"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${privilege.paymentFeePrivilegeId }"
								rel="${privilege.unitId }"
								majorid="${privilege.recruitMajorId }" autocomplete="off" /></td>
							<td>${privilege.unitName }</td>
							<td>${privilege.recruitPlanname }</td>
							<td>${privilege.recruitMajorName }</td>
							<td><fmt:formatNumber
									value='${privilege.totalPrivilegeFee }' pattern='####.##' /></td>
							<td>${privilege.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${brSchoolPrivilegeList}"
				goPageUrl="${baseUrl }/edu3/finance/recruitmajor/privilege/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>