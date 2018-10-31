<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>指导老师设置</title>
<script type="text/javascript">
	//新增
	function graduateMentor_add(){
		navTab.openTab('RES_TEACHING_THESIS_MENTOR_EDIT', '${baseUrl}/edu3/teaching/graduateMentor/edit.html', '新增指导老师');
	}
	
	//修改
	function graduateMentor_edit(){
		var url = "${baseUrl}/edu3/teaching/graduateMentor/edit.html";
		if(isCheckOnlyone('resourceid','#gMentorBody')){
			navTab.openTab('RES_TEACHING_THESIS_MENTOR_EDIT', url+'?resourceid='+$("#gMentorBody input[@name='resourceid']:checked").val(), '编辑指导老师');
		}			
	}
		
	//删除
	function graduateMentor_del(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/graduateMentor/delete.html","#gMentorBody");
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/graduateMentor/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBrschool }">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="graduateMentor_brSchoolName"
									defaultValue="${condition['branchSchool']}" style="width:120px"
									displayType="code" /></li>
						</c:if>
						<li><label>批次：</label>
						<gh:selectModel name="batchId" bindValue="resourceid"
								displayValue="batchName" value="${condition['batchId']}"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								style="width:125px" condition="batchType='thesis'" /></li>
						<li><label>指导老师名称：</label><input type="text" name="mentor"
							value="${condition['mentor']}" style="width: 120px" /></li>
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
			<gh:resAuth parentCode="RES_TEACHING_THESIS_MENTOR" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_gm"
							onclick="checkboxAll('#check_all_gm','resourceid','#gMentorBody')" /></th>
						<th width="15%">毕业论文批次</th>
						<th width="15%">指导老师姓名</th>
						<th width="65%">学生名单</th>
					</tr>
				</thead>
				<tbody id="gMentorBody">
					<c:forEach items="${mentorList.result}" var="mentor" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${mentor.resourceid }" autocomplete="off" /></td>
							<td>${mentor.examSub.batchName }</td>
							<td>${mentor.edumanager.cnName }</td>
							<td>${mentor.studentDetail }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${mentorList}"
				goPageUrl="${baseUrl }/edu3/teaching/graduateMentor/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>