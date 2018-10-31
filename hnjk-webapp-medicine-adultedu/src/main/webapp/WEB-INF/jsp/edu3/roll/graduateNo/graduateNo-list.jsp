<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>推迟毕业申请</title>
<script type="text/javascript">
	//新增
	function NoGraduate_add(){
		var url= "${baseUrl}/edu3/roll/graduateNo/validateDuringApply.html";
		$.ajax({
			type:"post",
			url:url,
			dataType:"json",
			success:function(data){
				if(data['isPromiss']==false){
					alertMsg.warn("目前不是申请延迟毕业的时间,如有需要,请与学籍办联系.");
					return false;
				}else{
					navTab.openTab('navTab', '${baseUrl}/edu3/roll/graduateNo/edit.html', '新增推迟毕业');
				}
			}
		});
		
	}
	/*
	//修改
	function NoGraduate_edit(){
		var url = "${baseUrl}/edu3/teaching/graduateNo/edit.html";
		if(isCheckOnlyone('resourceid','#nogBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#nogBody input[@name='resourceid']:checked").val(), '编辑推迟毕业');
		}			
	}
	*/
	//删除
	function NoGraduate_del(){	
		var url= "${baseUrl}/edu3/roll/graduateNo/validateDuringApply.html";
		$.ajax({
			type:"post",
			url:url,
			dataType:"json",
			success:function(data){
				if(data['isPromiss']==false){
					alertMsg.warn("目前不是申请延迟毕业的时间,如有需要,请与学籍办联系.");
					return false;
				}else{
					pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/roll/graduateNo/delete.html","#nogBody");
				}
			}
		});
		
	}
	/*
	//审核
	function NoGraduate_audit(){	
		pageBarHandle("您确定要审核这些记录吗？","${baseUrl}/edu3/teaching/graduateNo/audit.html","#nogBody");
	}
	*/
	//延迟毕业的申请时间段页面
	function setNoGraduateTime(){
		var url ="${baseUrl}/edu3/roll/graduateNo/noGraduateApplyArrangement.html";
		navTab.openTab('NOGRADUATE_TIME', url, '延迟毕业申请时间段');
	}
	function batchEffective(){
		pageBarHandle("您确定要生效这些记录吗？","${baseUrl}/edu3/roll/graduateNo/batchEffect.html?operation=Y","#nogBody");
	}
	function batchNoEffective(){
		pageBarHandle("您确定要撤销这些记录的生效状态吗？","${baseUrl}/edu3/roll/graduateNo/batchEffect.html?operation=N","#nogBody");
	}
	function exportApplyNoGraduateList(){//导出延迟毕业申请结果(按查询条件导出)
		var branchSchool = $("#applyNoGraduateList #graduateno_eiinfo_brSchoolName").val();
		var major		 = $("#applyNoGraduateList #major_id").val();
		var classic		 = $("#applyNoGraduateList #classic_id").val();
		var grade		 = $("#applyNoGraduateList #grade_id").val();
		var name		 = $("#applyNoGraduateList #name_id").val();
		var studyNo	     = $("#applyNoGraduateList #studyNo_id").val();
		var active	     = $("#applyNoGraduateList #active_id").val();
		var applyNoGraduationPlan_sel = $("#applyNoGraduateList #applyNoGraduationPlan_sel").val();
		$('#frame_exportApplyNoGraduateList').remove();
		var iframe = document.createElement("iframe");
		iframe.id = "frame_exportApplyNoGraduateList";
		iframe.src = "${baseUrl }/edu3/roll/graduateNo/exportExcel.html?branchSchool="+branchSchool+"&major="+major+"&classic="+classic+"&name="+name+"&studyNo="+studyNo+"&grade="+grade+"&active="+active+"&applyNoGraduationPlan_sel="+applyNoGraduationPlan_sel;
		iframe.style.display = "none";
		//创建完成之后，添加到body中
		document.body.appendChild(iframe);
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/roll/graduateNo/list.html" method="post">
				<div class="searchBar" id="applyNoGraduateList">
					<ul class="searchContent">
						<c:if test="${!isBrschool}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="graduateno_eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" style="width:120px" />
							</li>
						</c:if>
						<li><label>年级：</label>
						<gh:selectModel name="grade" id="grade_id" bindValue="resourceid"
								displayValue="gradeName" style="width:120px"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								orderBy="gradeName desc" value="${condition['grade']}" /></li>
						<li><label>专业：</label>
						<gh:selectModel name="major" id="major_id" bindValue="resourceid"
								displayValue="majorName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:120px" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="classic" id="classic_id"
								bindValue="resourceid" displayValue="classicName"
								value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:120px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>姓名：</label><input type="text" name="name"
							id="name_id" value="${condition['name']}" style="width: 120px" />
						</li>
						<li><label>学号：</label><input type="text" name="studyNo"
							id="studyNo_id" value="${condition['studyNo']}"
							style="width: 120px" /></li>
						<li><label>关联年度：</label><select
							name="applyNoGraduationPlan_sel" id="applyNoGraduationPlan_sel"
							style="width: 120px">
								<option value="">请选择</option>
								<c:forEach items="${settings}" var="plan">
									<option value="${plan.resourceid}"
										<c:if test="${condition['applyNoGraduationPlan_sel']==plan.resourceid }"> selected="selected"</c:if>>${plan.yearInfo.yearName}${ghfn:dictCode2Val('CodeTerm',plan.term) }</option>
								</c:forEach>
						</select></li>
						<li><label>生效状态：</label><select name="active" id="active_id"
							style="width: 120px">
								<option value="">请选择</option>
								<option value="0"
									<c:if test="${condition['active'] eq 0}">selected="selected"</c:if>>未生效</option>
								<option value="1"
									<c:if test="${condition['active'] eq 1}">selected="selected"</c:if>>生效</option>
						</select></li>
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
			<gh:resAuth parentCode="RES_TEACHING_THESIS_NOGRADUATE"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_nog"
							onclick="checkboxAll('#check_all_nog','resourceid','#nogBody')" /></th>
						<th width="10%">学号</th>
						<th width="10%">姓名</th>
						<th width="10%">年级</th>
						<th width="10%">专业</th>
						<th width="10%">层次</th>
						<th width="15%">教学站</th>
						<th width="15%">关联年度学期</th>
						<th width="9%">申请日期</th>
						<th width="6%">生效状态</th>
						<!-- 
		            <th width="10%">是否通过</th>    
		            <th width="10%">审核人</th>    
		             -->
					</tr>
				</thead>
				<tbody id="nogBody">
					<c:forEach items="${nolist.result}" var="no" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${no.resourceid }" autocomplete="off" /></td>
							<td>${no.studentInfo.studyNo }</td>
							<td>${no.studentInfo.studentName }</td>
							<td>${no.studentInfo.grade }</td>
							<td>${no.studentInfo.major }</td>
							<td>${no.studentInfo.classic }</td>
							<td>${no.studentInfo.branchSchool }</td>
							<td>${no.graduateNograduateSetting.yearInfo.yearName}${ghfn:dictCode2Val('CodeTerm',no.graduateNograduateSetting.term)}</td>
							<td><fmt:formatDate pattern="yyyy-MM-dd"
									value="${no.applayDate }" /></td>
							<td><c:choose>
									<c:when
										test="${no.studentInfo.isApplyGraduate eq 'W' and currentDate < no.graduateNograduateSetting.revokeDate and currentDate > no.graduateNograduateSetting.endDate}">
										<font color='"blue'>生效</font>
									</c:when>
									<c:otherwise>未生效</c:otherwise>
								</c:choose></td>
							<!-- 
			            <td >
			            	<c:choose>
								<c:when test="${no.isPass eq 'Y'}">通过</c:when>
								<c:otherwise>未审核</c:otherwise>
							</c:choose>	
			            </td>
			            <td >${no.auditMan }</td>
			             -->
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${nolist}"
				goPageUrl="${baseUrl }/edu3/roll/graduateNo/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>