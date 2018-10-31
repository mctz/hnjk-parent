<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生专业列表</title>

</head>
<body>
	<script type="text/javascript">

	$(document).ready(function(){
		$("#major").flexselect({});
	});
	//新增
	function addRecruitMajor(){
		var url = "${baseUrl}/edu3/recruit/recruitplan/major-input.html?planid=${planid}";
		navTab.openTab('major', url, '新增招生专业');
	}
	//编辑
	function modifyRecruitMajor(){
		var url = "${baseUrl}/edu3/recruit/recruitplan/major-input.html";
		if(isCheckOnlyone('resourceid','#majorBody')){
			var recruitMajorId = "";
			$("#majorBody input[@name='resourceid']:checked").each(function(){
				recruitMajorId = $(this).val();
			});
			navTab.openTab('major', url+'?resourceid='+recruitMajorId, '编辑招生专业');
		}			
	}
	//删除
	function delRecruitMajor(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/recruit/recruitplan/delmajor.html?planid=${planid}","#majorBody");
	}
	//审核通过
	function checkmajor(check){	
		var show = check == 1 ? "" : "不";
		pageBarHandle("您确定要审核"+show+"通过这些记录吗？","${baseUrl}/edu3/recruit/recruitplan/checkmajor.html?planid=${planid}&check="+check,"#majorBody");
	}
	//导出model
	function exportMajorModel(){
		var url = baseUrl+"/edu3/recruit/recruitplan/majormodel.html";
		alertMsg.confirm("你确定要导出招生专业模板吗？",{
			okCall:function(){
				downloadFileByIframe(url,'tgradeIframe');
			}
		});
	}
	//导入招生专业
	function imputMajor(){
		/*var isBrschool = '${isBrschool}';
		if(isBrschool=="false"||!isBrschool){
			alertMsg.warn("只有教学站才有权限导入！");
			return false;
		}*/
		$.pdialog.open(baseUrl+"/edu3/recruit/recruitplan/major-imput.html", 'RES_TEACHING_TEACHINGPLANCOURSETIMETABLE_INPUT', '招生专业导入', {width: 600, height: 360});
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/recruitplan/major-list.html?planid=${planid}"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>招生专业：</label><input type="text"
							name="recruitMajorName" value="${recruitMajorName}" /></li>
						<li><label>教学站：</label>
						<gh:brSchoolAutocomplete name="branchSchool" tabindex="1"
								id="examResultsManagerForNetWorkStudyTeachType_brSchoolName"
								displayType="code" defaultValue="${branchSchool}"
								style="width:55%" /></li>
						<li><label>基础专业：</label> <gh:selectModel id="major"
								name="major" bindValue="resourceid" defaultOptionText=""
								displayValue="majorName" isSubOptionText="N"
								modelClass="com.hnjk.edu.basedata.model.Major" value="${major}"
								style='width:60%;' /></li>

					</ul>

					<ul class="searchContent">

						<li><label>层次：</label>
						<gh:selectModel name="classicid" bindValue="resourceid"
								displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${classicid}" /></li>
						<li><label>学习形式：</label> <gh:select name="teachingType"
								dictionaryCode="CodeTeachingType" value="${teachingType}" /></li>
						<li><label>审批状态：</label><select name="status">
								<option <c:if test="${empty status}">selected='selected'</c:if>
									value=""></option>
								<option <c:if test="${status eq '1'}">selected='selected'</c:if>
									value="1">审批通过</option>
								<option <c:if test="${status eq '0'}">selected='selected'</c:if>
									value="0">未审批</option>
						</select></li>
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
					<gh:resAuth parentCode="RES_RECRUIT_PLAN_LIST" pageType="other"></gh:resAuth>
				</ul>
			</div>
			<input type="hidden" id="planid" name="planid" value="${planid}" />
			<table class="table" layouth="162">
				<thead>
					<tr>
						<th width="2%"><input type="checkbox" name="checkall"
							id="check_all_major"
							onclick="checkboxAll('#check_all_major','resourceid','#majorBody')" /></th>
						<th width="6%">招生批次</th>
						<th width="6%">总学费</th>
						<th width="10%">教学站</th>
						<th width="3%">层次</th>
						<th width="7%">招生专业编码</th>
						<th width="14%">招生专业名称</th>
						<th width="10%">专业</th>
						<th width="5%">学习形式</th>
						<th width="5%">学制</th>
						<th width="7%">指标数</th>
						<th width="7%">下限人数</th>
						<th width="5%">审批状态</th>
					</tr>
				</thead>
				<tbody id="majorBody">
					<c:forEach items="${majorlist.result}" var="major" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${major.resourceid}" autocomplete="off" /></td>
							<td align="left">${major.recruitPlan.recruitPlanname }</td>
							<td align="left">${major.tuitionFee }</td>
							<td align="left">${major.brSchool.unitName }</td>
							<td align="left">${major.classic.classicName }</td>
							<td align="left">${major.recruitMajorCode }</td>
							<td align="left">${major.recruitMajorName }</td>
							<td align="left">${major.major.majorCode }-${major.major.majorName }</td>
							<td align="left">${ghfn:dictCode2Val('CodeTeachingType',major.teachingType)}</td>
							<td align="left">${major.studyperiod }</td>
							<td align="left">${major.limitNum }</td>
							<td align="left">${major.lowerNum }</td>
							<td align="left"><c:choose>
									<c:when test="${major.status eq 1 }">审批通过</c:when>
									<c:otherwise>未审批</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${majorlist}"
				goPageUrl="${baseUrl }/edu3/recruit/recruitplan/major-list.html?planid=${planid}"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>
