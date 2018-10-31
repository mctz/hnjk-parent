<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生批次列表</title>

</head>
<body>
	<script type="text/javascript">
	//新增
	function addRecruitplan(){		
		var url = "${baseUrl}/edu3/recruit/recruitplan/plan-input.html";
		navTab.openTab('RES_RECRUIT_PLAN_INPUT', url, '新增招生批次');
	}
	//修改
	function modifyRecruitplan(){
		
		var url = "${baseUrl}/edu3/recruit/recruitplan/plan-input.html";
		if(isCheckOnlyone('resourceid','#recruitplanBody')){
			navTab.openTab('RES_RECRUIT_PLAN_EDIT', url+'?resourceid='+$("#recruitplanBody input[@name='resourceid']:checked").val(), '修改招生批次');
		}			
	}
		
	//删除
	function delRecruitplan(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/recruit/recruitplan/delplan.html","#recruitplanBody");
	}
    
    //制定招生专业
    function listMajor(){
		var url = "${baseUrl}/edu3/recruit/recruitplan/major-list.html";
		if(isCheckOnlyone('resourceid','#recruitplanBody')){
			navTab.openTab('RES_RECRUIT_MAJOR_LIST', url+'?planid='+$("#recruitplanBody input[@name='resourceid']:checked").val(), '制定招生专业');
		}			
	}
	//复制		
	  function copyRecruitplan(){
		if(isCheckOnlyone('resourceid',"#recruitplanBody")){
			pageBarHandle("您确定要复制这个招生计划吗？","${baseUrl}/edu3/recruit/recruitplan/copyplan.html","#recruitplanBody");
		}					
	}
	//查看
	  function intoPlan(planId){
	  	var url = "${baseUrl}/edu3/recruit/recruitplan/into-plan.html";
		navTab.openTab('RES_RECRUIT_PLAN_INTO', url+'?resourceid='+planId, '查看招生批次');
		
	  }
	//查看批次专业  
	  function intoPlanMajor(planName,planId){
	  	var url = "${baseUrl}/edu3/recruit/recruitplan/planMajor.html?planid="+planId;
	  	$.pdialog.open(url, 'RES_RECRUIT_MAJOR_EDIT_WINDOW', planName+'-招生专业', {width: 800, height: 600});
	  	//navTab.openTab('RES_RECRUIT_PLAN_EDIT', url+'?planid='+planId, planName+'招生专业');
	  }
	//设置分数线
		function setRecruitPlanMatriculateLine(){
			var url = "${baseUrl}/edu3/recruit/recruitplan/matriculateLine-form.html";
			if(isCheckOnlyone('resourceid','#recruitplanBody')){
				navTab.openTab('RES_RECRUIT_MATRICULATELINE_FORM', url+'?resourceid='+$("#recruitplanBody input[@name='resourceid']:checked").val(), '分数线、X系数设置');
			}			
		}
	//审批招生专业
	function auditRecruitMajor(){
		if(isCheckOnlyone('resourceid',"#recruitplanBody")){
			$.pdialog.open("${baseUrl}/edu3/framework/recruit/exameeinfo/upload.html?from=RecruitMajor&planid="+$("#recruitplanBody input[@name='resourceid']:checked").val(), 'RES_RECRUIT_MAJOR_AUDIT', '招生专业审批', {width: 800, height: 600});
		}
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				id="recruitPlanSearchForm"
				action="${baseUrl }/edu3/recruit/recruitplan/recruitplan-list.html"
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
					</ul>
					<ul class="searchContent">
						<li style="width: 280px"><label style="width: 100px">状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态：</label>
							<select name="isPublished" style="width: 150px">
								<option value="">请选择</option>
								<option value="Y"
									<c:if test="${condition['isPublished'] eq 'Y'}"> selected="selected"</c:if>>发布</option>
								<option value="N"
									<c:if test="${condition['isPublished'] eq 'N'}"> selected="selected"</c:if>>关闭</option>
						</select></li>
						<li style="width: 280px"><label style="width: 100px">批次发布日期：</label>
							<input type="text" name="publishDate" readonly="readonly"
							value="${condition['publishDate'] }"
							onfocus="WdatePicker({isShowWeek:true})" style="width: 150px" />
						</li>
						<li style="width: 280px"><label style="width: 100px">报名开始时间：</label>
							<input id="d1" type="text" name="startDate" readonly="readonly"
							value="${condition['startDate'] }"
							onfocus="WdatePicker({isShowWeek:true})" style="width: 150px" />
						</li>
					</ul>
					<ul class="searchContent">
						<li style="width: 280px"><label style="width: 100px">报名结束时间：</label>
							<input id="d2" type="text" name="endDate" readonly="readonly"
							value="${condition['endDate'] }"
							onfocus="WdatePicker({isShowWeek:true,minDate:'#F{$dp.$D(\'d1\')}'})"
							style="width: 150px" /></li>
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
			<gh:resAuth parentCode="RES_RECRUIT_PLAN_LIST" pageType="list"></gh:resAuth>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th width="5%" align="center"><input type="checkbox"
							name="checkall" id="check_all_plan"
							onclick="checkboxAll('#check_all_plan','resourceid','#recruitplanBody')" /></th>
						<th width="25%" align="center">招生批次</th>
						<th width="10%" align="center">报名开始时间</th>
						<th width="10%" align="center">报名截止时间</th>
						<th width="15%" align="center">批次发布日期</th>
						<th width="5%" align="center">状态</th>
						<th width="20%" align="center">办学模式</th>
						<th width="10%" align="center">操作</th>
					</tr>
				</thead>
				<tbody id="recruitplanBody">
					<c:forEach items="${planlist.result}" var="plan" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${plan.resourceid }" autocomplete="off" /></td>
							<td><a href="javascript:void(0)"
								onclick="intoPlan('${plan.resourceid }')">${plan.recruitPlanname}</a></td>
							<td>${plan.startDate }</td>
							<td>${plan.endDate }</td>
							<td>${plan.publishDate }</td>

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
							<td><a href="javascript:void(0)"
								onclick="intoPlanMajor('${plan.recruitPlanname}','${plan.resourceid }')">查看专业</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${planlist}"
				goPageUrl="${baseUrl }/edu3/recruit/recruitplan/recruitplan-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>
