<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>论文预约信息</title>
<script type="text/javascript">
	//新增
	function GPOrder_add(){
		navTab.openTab('navTab', '${baseUrl}/edu3/teaching/graduatePapers/edit.html', '新增预约信息');
	}
	
	//修改
	function GPOrder_edit(){
		var url = "${baseUrl}/edu3/teaching/graduatePapers/edit.html";
		if(isCheckOnlyone('resourceid','#gpoBody158')){
			navTab.openTab('_blank', url+'?resourceid='+$("#gpoBody158 input[name='resourceid']:checked").val(), '编辑预约信息');
		}			
	}
		
	//删除
	function GPOrder_del(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/graduatePapers/delete.html","#gpoBody158");
	}
	
	function GPOrder_audit(){
		pageBarHandle("确定要审核吗？","${baseUrl}/edu3/teaching/graduatePapers/audit.html?auditType=1","#gpoBody158");
	}
	function GPOrder_cancelaudit(){
		pageBarHandle("确定要撤销审核吗？","${baseUrl}/edu3/teaching/graduatePapers/audit.html?auditType=0","#gpoBody158");
	}
	//导出
	function exportGraduatePapersOrder(){
		if($("#graduatePapersOrder_batchId").val()==""){
			alertMsg.warn("请选择一个论文批次");
	    	return false;
		}
		var url = "${baseUrl}/edu3/teaching/graduatepapersorder/export.html?batchId="+$("#graduatePapersOrder_batchId").val();
		downloadFileByIframe(url,"graduatePapersOrderIframe");
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/graduatePapers/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>批次：</label>
						<gh:selectModel id="graduatePapersOrder_batchId" name="batchId"
								bindValue="resourceid" displayValue="batchName"
								value="${condition['batchId']}"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								style="width:120px" condition="batchType='thesis'" /></li>
						<c:if test="${showCenter eq 'show'}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="graduateno_eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:120px" /></li>
						</c:if>
						<li><label>年级：</label>
						<gh:selectModel name="grade" bindValue="resourceid"
								displayValue="gradeName" style="width:120px"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								orderBy="gradeName desc" value="${condition['grade']}" /></li>
						<li><label>指导老师：</label><input type="text" name="teacherName"
							value="${condition['teacherName']}" style="width: 115px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>专业：</label>
						<gh:selectModel name="major" bindValue="resourceid"
								displayValue="majorCodeName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:120px" orderBy="majorCode" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:120px" /></li>
						<li><label>学生姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 115px" /></li>
						<li><label>学生学号：</label><input type="text" name="stuStudyNo"
							value="${condition['stuStudyNo']}" style="width: 115px" /></li>
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
			<gh:resAuth parentCode="RES_TEACHING_THESIS_ORDER" pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_gpo"
							onclick="checkboxAll('#check_all_gpo','resourceid','#gpoBody158')" /></th>
						<th width="18%">论文批次</th>
						<th width="15%">学号</th>
						<th width="8%">姓名</th>
						<th width="10%">年级</th>
						<th width="10%">专业</th>
						<th width="9%">层次</th>
						<th width="15%">教学站</th>
						<th width="10%">指导老师</th>
						<%--      
		            <th width="8%">审核人</th>                
		            <th width="8%">审核时间</th>              
		            <th width="8%">审核状态</th>    --%>
					</tr>
				</thead>
				<tbody id="gpoBody158">
					<c:forEach items="${ordercList.result}" var="o" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${o.resourceid }" autocomplete="off" /></td>
							<td>${o.examSub.batchName }</td>
							<td>${o.studentInfo.studyNo }</td>
							<td>${o.studentInfo.studentName }</td>
							<td>${o.studentInfo.grade }</td>
							<td>${o.studentInfo.major }</td>
							<td>${o.studentInfo.classic }</td>
							<td>${o.studentInfo.branchSchool }</td>
							<td>${o.guidTeacherName }</td>
							<%-- 
			            <td >${o.auditMan }</td>
			            <td ><fmt:formatDate value="${o.auditTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			            <td >
			            	<c:choose>
			            		<c:when test="${o.status eq 1}">通过</c:when>
			            		<c:otherwise>未审核</c:otherwise>
			            	</c:choose>
			            </td> --%>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${ordercList}"
				goPageUrl="${baseUrl }/edu3/teaching/graduatePapers/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>