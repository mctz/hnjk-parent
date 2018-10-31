<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生专业列表</title>
<style type="text/css">
	td,th{text-align: center;}
</style>
</head>
<body>
	<!-- <script type="text/javascript">
	//新增
	function add(){
		var url = "${baseUrl}/edu3/recruit/recruitplan/major-input.html?planid=${planid}";
		navTab.openTab('major', url, '新增招生专业');
	}
	//编辑
	function modify(){
		var url = "${baseUrl}/edu3/recruit/recruitplan/major-input.html";
		if(isCheckOnlyone('resourceid','#majorBody')){
			navTab.openTab('major', url+'?resourceid='+$("#majorBody input[@name='resourceid']:checked").val(), '编辑招生专业');
		}			
	}
	//删除
	function del(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/recruit/recruitplan/delmajor.html","#majorBody");
	}
</script> -->
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);" id="subSeachPageFrom"
				action="${baseUrl }/edu3/recruit/recruitplan/planMajor.html?planid=${planid}"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>招生专业：</label><input type="text"
							name="recruitMajorName" value="${recruitMajorName}" /></li>
						<li><label>层次：</label>
						<gh:selectModel id="classic" name="classic" bindValue="resourceid"
								displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${classic}" /></li>
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
			<input type="hidden" id="planid" name="planid" value="${planid}" />
			<table class="table" layouth="142">
				<thead>
					<tr>
						<!-- <th width="5%"><input type="checkbox" name="checkall"
							id="check_all_major"
							onclick="checkboxAll('#check_all_major','resourceid','#majorBody')" /></th> -->
						<th width="12%">招生批次</th>
						<th width="12%">教学站</th>
						<th width="5%">层次</th>
						<th width="10%">招生专业编码</th>
						<th width="15%">招生专业名称</th>
						<th width="12%">专业</th>
						<th width="5%">学习形式</th>
						<th width="5%">学制</th>
						<%--
		            <th width="7%">指标数</th>
		            <th width="7%">下限人数</th>	 --%>
						<th width="7%">人数</th>
						<th width="7%">审批状态</th>
					</tr>
				</thead>
				<tbody id="majorBody">
					<c:forEach items="${majorlist.result}" var="major" varStatus="vs">
						<tr>
							<%-- <td><input type="checkbox" name="resourceid"
								value="${major.resourceid}" autocomplete="off" /></td> --%>
							<td>${major.recruitPlan.recruitPlanname }</td>
							<td style="text-align: left;">${major.brSchool.unitName }</td>
							<td>${major.classic.classicName }</td>
							<td>${major.recruitMajorCode }</td>
							<td style="text-align: left;">${major.recruitMajorName }</td>
							<td style="text-align: left;">${major.major.majorName }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',major.teachingType)}</td>
							<td>${major.studyperiod }</td>
							<td>${major.limitNum }</td>
							<%-- <td  align="left">${major.lowerNum }</td> --%>
							<td><c:choose>
									<c:when test="${major.status eq 1 }">审批通过</c:when>
									<c:otherwise>未审批</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${majorlist}"
				goPageUrl="${baseUrl }/edu3/recruit/recruitplan/planMajor.html?planid=${planid}"
				targetType="dialog" pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>
