<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置录取查询时间-招生批次列表</title>

</head>
<body>
	<script type="text/javascript">

	//设置录取查询时间
	function settingEnrollStartTimeAndEndTime(){
		
		var url = "${baseUrl}/edu3/recruit/matriculate/enrollStartTimeAndEndTime-form.html";
		if(isCheckOnlyone('resourceid','#enrollStartTimeAndEndTimeRecruitplanBody')){
			navTab.openTab('RES_RECRUIT_PLAN_EDIT_FOR_ENROLLSTARTTIMEANDENDTIME', url+'?recruitPlanId='+$("#enrollStartTimeAndEndTimeRecruitplanBody input[@name='resourceid']:checked").val(), '设置录取查询时间');
		}			
	}
	//上传录取文件
	function uploadPackageOffer(){
		var url = "${baseUrl}/edu3/recruit/matriculate/uploadPackageOffer-form.html";
		$.pdialog.open(url, 'uploadPackageOffer_form','上传录取文件', {width: 800, height: 400});
	}
	
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="matriculateSearchStartTimeAndEndTimeForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/matriculate/enrollStartTimeAndEndTime-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li style="width: 280px"><label style="width: 100px">年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;级：</label>
							<gh:selectModel name="grade" bindValue="resourceid"
								displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['grade']}" orderBy="yearInfo.firstYear desc"
								style="width:150px" /></li>
						<li style="width: 280px"><label style="width: 100px">招生批次名称：</label>
							<input type="text" name="recruitPlanname"
							value="${condition['recruitPlanname']}" style="width: 150px" /></li>
						<li style="width: 280px"><label style="width: 100px">办学模式：</label>
							<gh:select name="teachingType" dictionaryCode="CodeTeachingType"
								value="${condition['teachingType']}" style="width:150px" /></li>
						<li style="width: 280px"><label style="width: 100px">状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态：</label>
							<select name="isPublished" style="width: 150px">
								<option value="">请选择</option>
								<option value="Y"
									<c:if test="${condition['isPublished'] eq 'Y'}"> selected="selected"</c:if>>发布</option>
								<option value="N"
									<c:if test="${condition['isPublished'] eq 'N'}"> selected="selected"</c:if>>关闭</option>
						</select></li>
						<li style="width: 280px"><label style="width: 100px">报名开始时间：</label>
							<input id="d1" type="text" name="startDate" readonly="readonly"
							value="${condition['startDate'] }" class="Wdate"
							onfocus="WdatePicker({isShowWeek:true})" style="width: 150px" />
						</li>
						<li style="width: 280px"><label style="width: 100px">报名结束时间：</label>
							<input id="d2" type="text" name="endDate" readonly="readonly"
							value="${condition['endDate'] }" class="Wdate"
							onfocus="WdatePicker({isShowWeek:true,minDate:'#F{$dp.$D(\'d1\')}'})"
							style="width: 150px" /></li>
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
			<gh:resAuth
				parentCode="RES_MATRICALATE_BATCHMATRICULATE_ENROLLEEDATE"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%" align="center"><input type="checkbox"
							name="checkall" id="check_all_enrollTimeplan"
							onclick="checkboxAll('#check_all_enrollTimeplan','resourceid','#enrollStartTimeAndEndTimeRecruitplanBody')" /></th>
						<th width="20%" align="center">招生批次</th>
						<th width="10%" align="center">批次发布日期</th>
						<th width="10%" align="center">报名开始时间</th>
						<th width="10%" align="center">报名截止时间</th>
						<th width="15%" align="center">录取查询时间</th>
						<!-- <th width="10%" align="center">录取查询结束时间</th> -->
						<th width="15%" align="center">报到时间</th>
						<th width="5%" align="center">状态</th>
						<th width="20%" align="center">办学模式</th>
					</tr>
				</thead>
				<tbody id="enrollStartTimeAndEndTimeRecruitplanBody">
					<c:forEach items="${planlist.result}" var="plan" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${plan.resourceid }" autocomplete="off" /></td>
							<td>${plan.recruitPlanname}</td>
							<td style="color: green">${plan.publishDate }</td>
							<td>${plan.startDate }</td>
							<td>${plan.endDate }</td>
							<td style="color: blue">${plan.enrollStartTime }至 ${plan.enrollEndTime }</td>
							<%-- <td style="color: blue">${plan.enrollEndTime }</td> --%>
							<td style="color: blue">${plan.reportTime }至 ${plan.endTime }
							</td>
							<c:choose>
								<c:when test="${plan.isPublished eq 'Y' }">
									<td style="color: green">发布</td>
								</c:when>
								<c:when test="${plan.isPublished eq 'N' }">
									<td style="color: red">关闭</td>
								</c:when>
								<c:otherwise>
									<td style="color: black">未设置状态</td>
								</c:otherwise>
							</c:choose>
							<td>
								${ghfn:dictCode2Val("CodeTeachingType",plan.teachingType) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${planlist}"
				goPageUrl="${baseUrl }/edu3/recruit/matriculate/enrollStartTimeAndEndTime-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>
