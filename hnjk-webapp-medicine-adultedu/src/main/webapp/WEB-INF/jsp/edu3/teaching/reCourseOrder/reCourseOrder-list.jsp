<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>补预约管理-列表</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="reOrderSetting_searchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/recourseorder/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBranchSchool}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="reOrderSetting_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:55%" /></li>
						</c:if>
						<li><label>补预约类型</label> <gh:select name="reOrderType"
								dictionaryCode="CodeCourseOrderStatus"
								value="${condition['reOrderType']}" style="width:55%" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_REBOOKING_CONFIG_LIST"
				pageType="list"></gh:resAuth>
			<table id="reOrderSettingTable" class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="reCourseOrder_list_check_all"
							onclick="checkboxAll('#reCourseOrder_list_check_all','resourceid','#reCourseOrderListBody')" /></th>
						<th width="30%">教学站</th>
						<th width="10%">补预约类型</th>
						<th width="25%">补预约批次</th>
						<th width="30%">补预约时间</th>
					</tr>
				</thead>
				<tbody id="reCourseOrderListBody">
					<c:forEach items="${page.result }" var="reOrder">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${reOrder.resourceid}" autocomplete="off" /></td>
							<td>${reOrder.brSchool.unitName }</td>
							<td>${ghfn:dictCode2Val('CodeCourseOrderStatus',reOrder.reOrderType)}</td>
							<td><c:choose>
									<c:when test="${null!=reOrder.orderCourseSetting }">
										<gh:selectModel id='reCourseOrderForm_orderCourseSetting'
											name='orderCourseSetting' bindValue='resourceid'
											displayValue='settingName'
											modelClass='com.hnjk.edu.teaching.model.OrderCourseSetting'
											orderBy='startDate desc' choose='Y'
											value="${reOrder.orderCourseSetting.resourceid }"
											disabled="disabled" />
									</c:when>
									<c:when test="${null!=reOrder.examSub }">
										<gh:selectModel id='reCourseOrderForm_examSub' name='examSub'
											bindValue='resourceid' displayValue='batchName'
											modelClass='com.hnjk.edu.teaching.model.ExamSub'
											orderBy='batchName desc' choose='Y'
											value="${reOrder.examSub.resourceid }"
											condition="batchType='exam'" style="width:55%"
											disabled="disabled" />
									</c:when>
								</c:choose></td>
							<td><fmt:formatDate value="${reOrder.startTime }"
									pattern="yyyy年MM月dd日 HH:mm:ss" />至 <fmt:formatDate
									value="${reOrder.endTime }" pattern="yyyy年MM月dd日 HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/recourseorder/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
	//新增
	function reBookIng_add(){
		navTab.openTab('RES_TEACHING_REBOOKING_CONFIG_ADD', '${baseUrl}/edu3/teaching/recourseorder/edit.html', '新增补预约权限');
	}
	//修改
	function reBookIng_edit(){
		var url = "${baseUrl}/edu3/teaching/recourseorder/edit.html";
		if(isCheckOnlyone('resourceid','#reCourseOrderListBody')){
			navTab.openTab('RES_TEACHING_REBOOKING_CONFIG_EDIT', url+'?resourceid='+$("#reCourseOrderListBody input[@name='resourceid']:checked").val(), '编辑补预约权限');
		}		
	}
	//删除
	function reBookIng_del(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/recourseorder/del.html","#reCourseOrderListBody");
	}
</script>
</body>
</html>
