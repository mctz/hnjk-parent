<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>申报招生专业列表</title>
<script type="text/javascript">
	//申报招生专业
	function schoolMajorReportAdd(){
		var url = "${baseUrl}/edu3/recruit/recruitmanage/branchschoolmajor-form.html";
		navTab.openTab('brsmajor', url, '申报招生专业');
	}
	//审批招生专业
	function schoolMajorReportModify(){
		var url      = "${baseUrl}/edu3/recruit/recruitmanage/branchschoolmajor-form.html";
		if(isCheckOnlyone('resourceid','#brsmajorBody')){
			navTab.openTab('brsmajor', url+'?resourceid='+$("#brsmajorBody input[@name='resourceid']:checked").val()+"&tabIndex=${condition['tabIndex']}", '编辑招生专业');
		}			
	}
	//查看当前用户退回的流程
	function showSendBackSchoolMajor(){
		navTab.reload('${baseUrl}/edu3/recruit/recruitmanage/branchschoolmajor-list.html?selectFlag=BACK','');
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/recruitmanage/branchschoolmajor-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${empty condition['isBranchSchool'] }">
							<li class="custom-li"><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="branchschoolmajor_brSchoolName"
									defaultValue="${condition['branchSchool']}" style="width:240px;" />
							</li>
						</c:if>
						<li><label>招生批次：</label> <gh:recruitPlanAutocomplete
								name="recruitplanId" tabindex="2"
								id="_branchschoolmajor_recruitPlan"
								value="${condition['recruitplanId']}" style="width:55%" /></li>

						<li><label>办学模式：</label> <gh:select name="teachingType"
								dictionaryCode="CodeTeachingType"
								value="${condition['teachingType']}" style="width:57%" /></li>

					</ul>
					<ul class="searchContent">
						<li><label>是否审核通过：</label> <gh:select name="isAudited"
								dictionaryCode="yesOrNo" value="${condition['isAudited']}"
								style="width:57%" /></li>
						<li><label>招生批次名称：</label> <input type="text"
							name="recruitplanName" value="${condition['recruitplanName']}"
							style="width: 55%" /></li>
						<li><label>提交人：</label> <input type="text"
							name="fillinMan" value="${condition['fillinMan']}"
							style="width: 55%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>提交时间：</label> <input type="text"
							name="fillinDate" size="40" value="${condition['fillinDate']}"
							onFocus="WdatePicker({isShowWeek:true})" style="width: 55%" /></li>

						<li><label>查询范围：</label> <gh:select name="selectFlag"
								dictionaryCode="CodeWorkFlowEvent"
								value="${condition['selectFlag']}" style="width:57%" /></li>
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
			<div class="panelBar">
				<ul class="toolBar">
					<gh:resAuth parentCode="RES_RECRUIT_MANAGE_SCHOOLMAJORREPORT"
						pageType="list"></gh:resAuth>
				</ul>
			</div>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_brsmajor"
							onclick="checkboxAll('#check_all_brsmajor','resourceid','#brsmajorBody')" /></th>
						<th width="20%">招生批次</th>
						<th width="20%">教学站</th>
						<th width="20%">批准文号</th>
						<th width="10%">提交人</th>
						<th width="15%">提交时间</th>
						<th width="10%">当前状态</th>
					</tr>
				</thead>
				<tbody id="brsmajorBody">
					<c:forEach items="${page.result}" var="brsplan" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${brsplan.resourceid}" autocomplete="off" /></td>
							<td align="left"><a target="navTab" rel="view_brspalan"
								href="${baseUrl}/edu3/recruit/recruitmanage/branchschoolmajor-form.html?resourceid=${brsplan.resourceid }&tabIndex=${condition['tabIndex']}">${brsplan.recruitplanName}</a></td>
							<td align="left">${brsplan.branchSchool.unitName}</td>
							<td align="left">${brsplan.documentCode}</td>
							<td align="left">${brsplan.fillinMan}</td>
							<td align="left"><fmt:formatDate
									value="${brsplan.fillinDate}" pattern="yyyy-MM-dd HH:mm:ss" />
							</td>
							<!-- 根据新需求将原来所有审核状态为N的显示为批复中 -->
							<%-- <td  align="left"><font <c:if test="${brsplan.isAudited eq 'Y' }"> color="green" </c:if>> ${ghfn:dictCode2Val('CodeAuditStatus',brsplan.isAudited)}</font></td>--%>
							<c:choose>
								<c:when test="${brsplan.isAudited eq 'Y' }">
									<td align="left"><font color="green">通过</font></td>
								</c:when>
								<c:when test="${brsplan.isAudited eq 'W' }">
									<td align="left">批复中</td>
								</c:when>
								<c:when test="${brsplan.isAudited eq 'N' }">
									<td align="left"><font color="red">批复中 </font></td>
								</c:when>
							</c:choose>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/recruit/recruitmanage/branchschoolmajor-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>
