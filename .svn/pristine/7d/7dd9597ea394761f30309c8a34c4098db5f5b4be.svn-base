<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>年级预交费用设置</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/schoolroll/studentfeerule/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBrschool}">
							<li><label>学习中心：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1" id="eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" style="width:120px" />
							</li>
						</c:if>
						<li><label>年级：</label>
						<gh:selectModel name="grade" bindValue="resourceid"
								displayValue="gradeName" style="width:120px"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								orderBy="gradeName desc" value="${condition['grade']}" /></li>

					</ul>
					<ul class="searchContent">
						<li><label>专业：</label>
						<gh:selectModel name="major" bindValue="resourceid"
								displayValue="majorName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:120px" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:125px" /></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_SCHOOLFEE_RULE" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_sfr"
							onclick="checkboxAll('#check_all_sfr','resourceid','#stuFeeRuleBody')" /></th>
						<th width="20%">费用标准名称</th>
						<th width="10%">年级</th>
						<th width="10%">专业</th>
						<th width="10%">层次</th>
						<th width="10%">每学分缴费标准</th>
						<th width="10%">总缴费金额</th>
						<th width="10%">生成缴费明细</th>
					</tr>
				</thead>
				<tbody id="stuFeeRuleBody">
					<c:forEach items="${feeList.result}" var="s" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${s.resourceid }" autocomplete="off" /></td>
							<td>${s.feeRuleName }</td>
							<td>${s.grade }</td>
							<td>${s.major }</td>
							<td>${s.classic }</td>
							<td><fmt:formatNumber type="currency"
									value="${s.creditFee }" /></td>
							<td><fmt:formatNumber type="currency"
									value="${s.totalFee }" /></td>
							<td><c:choose>
									<c:when test="${s.generatorFlag eq 'Y'}">已生成</c:when>
									<c:otherwise>未生成</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${feeList}"
				goPageUrl="${baseUrl }/edu3/schoolroll/studentfeerule/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

	<script type="text/javascript">
	//新增
	function addStuFeeRule(){
		var url = "${baseUrl}/edu3/schoolroll/studentfeerule/edit.html";
		navTab.openTab('_blank', url, '新增缴费标准');
	}
	//修改
	function modifyStuFeeRule(){
		var url = "${baseUrl}/edu3/schoolroll/studentfeerule/edit.html";
		if(isCheckOnlyone('resourceid','#stuFeeRuleBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#stuFeeRuleBody input[@name='resourceid']:checked").val(), '编辑缴费标准');
		}			
	}
		
	//删除
	function delStuFeeRule(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/schoolroll/studentfeerule/delete.html","#stuFeeRuleBody");
	}
	//生成繳費明細
	function generatorFeeDetail(){
		var ids = new Array();
    	$("#stuFeeRuleBody input[name='resourceid']:checked").each(function(ind){
		  	ids.push($(this).val());
		});
		if(ids.length>0){
			var url = "${baseUrl}/edu3/schoolroll/studentfeerule/generatorFeeDetail.html?resourceids="+ids.toString();
			pageBarHandle("您确定要生成交费明细吗？",url,"#stuFeeRuleBody");
		}
	}
</script>

</body>
</html>